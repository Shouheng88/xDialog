package me.shouheng.uix.widget.dialog

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.app.Activity
import android.app.Dialog
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.os.AsyncTask
import android.os.Build
import android.util.TypedValue
import android.view.*
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.FloatRange
import androidx.appcompat.app.AppCompatActivity
import me.shouheng.uix.widget.R
import me.shouheng.uix.widget.utils.DialogUtils
import kotlin.math.ceil

/**
 * Dialog blur behind engine.
 *
 * @Author wangshouheng
 * @Time 2021/10/15
 */
class BlurEngine {

    private var enable: Boolean = false
    private var dimmingEffect = false
    private var blurredActionBar = true
    private var statusBarTranslucent: Boolean? = null
    @FloatRange(from = 0f.toDouble(), to = 1f.toDouble(), fromInclusive = false)
    private var scale: Float = .5f
    @FloatRange(from = 0f.toDouble(), to = 25f.toDouble(), fromInclusive = false)
    private var radius: Float = 12f

    private var blurredBackgroundView: ImageView? = null
    private var blurredBackgroundLayoutParams: FrameLayout.LayoutParams? = null
    private val animationDuration = 0

    private var blurTask: BlurTask? = null

    private var host: Activity? = null

    fun setEnable(enable: Boolean) {
        this.enable = enable
    }

    fun onAttach(host: Activity) {
        this.host = host
    }

    fun onStart(dialog: Dialog?) {
        if (!enable) return
        if (dialog != null) {
            // enable or disable dimming effect.
            if (!dimmingEffect) {
                dialog.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            }
            // add default fade to the dialog if no window animation has been set.
            val currentAnimation = dialog.window?.attributes?.windowAnimations
            if (currentAnimation == 0) {
                dialog.window?.setWindowAnimations(R.style.DialogAlphaAnimation)
            }
        }
    }

    fun onResume(retainedInstance: Boolean) {
        if (!enable) return
        if (blurredBackgroundView == null || retainedInstance) {
            if (host?.window?.decorView?.isShown == true) {
                blurTask = BlurTask()
                blurTask?.execute()
            } else {
                host?.window?.decorView?.viewTreeObserver?.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        // dialog can have been closed before being drawn
                        if (host != null) {
                            host?.window?.decorView?.viewTreeObserver?.removeOnPreDrawListener(this)
                            blurTask = BlurTask()
                            blurTask?.execute()
                        }
                        return true
                    }
                })
            }
        }
    }

    fun onDismiss() {
        if (!enable) return
        blurTask?.cancel(true)
        blurredBackgroundView
            ?.animate()
            ?.alpha(0f)
            ?.setDuration(animationDuration.toLong())
            ?.setInterpolator(AccelerateInterpolator())
            ?.setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    removeBlurredView()
                }

                override fun onAnimationCancel(animation: Animator) {
                    super.onAnimationCancel(animation)
                    removeBlurredView()
                }
            })?.start()
    }

    fun onDetach() {
        blurTask?.cancel(true)
        blurTask = null
        host = null
    }

    fun setBlurEffect(blurEffect: BlurEffect) {
        this.enable = blurEffect.enable
        this.scale = blurEffect.scale
        this.radius = blurEffect.radius
        this.dimmingEffect = blurEffect.dimmingEffect
        this.blurredActionBar = blurEffect.blurredActionBar
        this.statusBarTranslucent = blurEffect.statusBarTranslucent
    }

    private fun removeBlurredView() {
        val parent = blurredBackgroundView?.parent as? ViewGroup
        parent?.removeView(blurredBackgroundView)
        blurredBackgroundView = null
    }

    private fun blur(bkg: Bitmap, view: View) {
        // define layout params to the previous imageView in order to match its parent
        blurredBackgroundLayoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )

        // overlay used to build scaled preview and blur background
        var overlay: Bitmap?

        // evaluate top offset due to action bar, 0 if the actionBar should be blurred.
        val actionBarHeight: Int = if (blurredActionBar) 0 else DialogUtils.getActionBarHeight()

        // evaluate top offset due to status bar
        var statusBarHeight = 0
        if (host != null && DialogUtils.isStatusBarVisible(host!!)) {
            statusBarHeight = DialogUtils.getStatusBarHeight()
        }
        if (isStatusBarTranslucent()) {
            statusBarHeight = 0
        }
        val topOffset = actionBarHeight + statusBarHeight

        // evaluate bottom or right offset due to navigation bar.
        var bottomOffset = 0
        var rightOffset = 0
        if (host != null && DialogUtils.isNavBarVisible(host!!)) {
            val navBarSize: Int = DialogUtils.getNavBarHeight()
            if (host?.resources?.getBoolean(R.bool.uix_dialog_has_bottom_navigation_bar) == true) {
                bottomOffset = navBarSize
            } else {
                rightOffset = navBarSize
            }
        }

        // in order to keep the same ratio as the one which will be used for rendering, also
        // add the offset to the overlay.
        val height = ceil((view.height - topOffset - bottomOffset).toDouble())
        val width = ceil((view.width - rightOffset) * height / (view.height - topOffset - bottomOffset))

        val y = (topOffset*bkg.height*1f/view.height).toInt()
        val w = ((bkg.width*width/view.width).toInt().coerceAtMost(bkg.width))
        val h = (bkg.height*height/view.height).toInt().coerceAtMost(bkg.height)

        DialogUtils.d("Screen: (${DialogUtils.getScreenWidth()}, ${DialogUtils.getScreenHeight()})\n" +
            "H,W: ($width, $height)\n" +
            "BKG: (${bkg.width}, ${bkg.height})\n" +
            "Size: (0, $y, $w, $h)")

        overlay = DialogUtils.clip(bkg, 0, y, w, h)
        try {
            if (host is AppCompatActivity) {
                // add offset as top margin since actionBar height must also considered when we display
                // the blurred background. Don't want to draw on the actionBar.
                blurredBackgroundLayoutParams?.setMargins(0, actionBarHeight, 0, 0)
                blurredBackgroundLayoutParams?.gravity = Gravity.TOP
            }
        } catch (e: NoClassDefFoundError) {
            // no dependency to appcompat, that means no additional top offset due to actionBar.
            blurredBackgroundLayoutParams?.setMargins(0, 0, 0, 0)
        }

        // apply fast blur on overlay
        overlay = DialogUtils.fastBlur(overlay, scale, radius)
        //set bitmap in an image view for final rendering
        blurredBackgroundView = ImageView(host)
        blurredBackgroundView?.scaleType = ImageView.ScaleType.CENTER_CROP
        blurredBackgroundView?.setImageDrawable(BitmapDrawable(host?.resources, overlay))
    }

    private fun isStatusBarTranslucent(): Boolean {
        if (statusBarTranslucent != null)
            return statusBarTranslucent!!
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && isStatusBarTranslucentKitkat()
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private fun isStatusBarTranslucentKitkat(): Boolean {
        val typedValue = TypedValue()
        val attribute = intArrayOf(android.R.attr.windowTranslucentStatus)
        val array = host?.obtainStyledAttributes(typedValue.resourceId, attribute)
        val isStatusBarTranslucent = array?.getBoolean(0, false)
        array?.recycle()
        return isStatusBarTranslucent == true
    }

    inner class BlurTask : AsyncTask<Unit, Unit, Unit>() {

        private var mBackground: Bitmap? = null
        private var mBackgroundView: View? = null

        override fun onPreExecute() {
            mBackgroundView = host?.window?.decorView

            /**
             * After rotation, the DecorView has no height and no width. Therefore
             * .getDrawingCache() return null. That's why we  have to force measure and layout.
             */
            val rect = Rect()
            mBackgroundView?.getWindowVisibleDisplayFrame(rect)
            mBackgroundView?.destroyDrawingCache()
            mBackgroundView?.isDrawingCacheEnabled = true
            mBackgroundView?.buildDrawingCache(true)
            mBackground = mBackgroundView?.getDrawingCache(true)

            if (mBackground == null) {
                mBackgroundView?.measure(
                    View.MeasureSpec.makeMeasureSpec(rect.width(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(rect.height(), View.MeasureSpec.EXACTLY)
                )
                mBackgroundView?.layout(0, 0, mBackgroundView!!.measuredWidth, mBackgroundView!!.measuredHeight)
                mBackgroundView?.destroyDrawingCache()
                mBackgroundView?.isDrawingCacheEnabled = true
                mBackgroundView?.buildDrawingCache(true)
                mBackground = mBackgroundView!!.getDrawingCache(true)
            }
        }

        override fun onPostExecute(result: Unit?) {
            mBackgroundView?.destroyDrawingCache()
            mBackgroundView?.isDrawingCacheEnabled = false

            host?.window?.addContentView(blurredBackgroundView, blurredBackgroundLayoutParams)

            blurredBackgroundView?.alpha = 0f
            blurredBackgroundView
                ?.animate()
                ?.alpha(1f)
                ?.setDuration(animationDuration.toLong())
                ?.setInterpolator(LinearInterpolator())
                ?.start()
            mBackgroundView = null
            mBackground = null
        }

        override fun doInBackground(vararg params: Unit?) {
            if (!isCancelled) {
                try {
                    blur(mBackground!!, mBackgroundView!!)
                } catch (e: Throwable) {
                    // ignore
                }
            } else {
                return
            }
            mBackground?.recycle()
        }
    }

    class BlurEffect {
        var enable: Boolean = false
        @FloatRange(from = 0f.toDouble(), to = 1f.toDouble(), fromInclusive = false)
        var scale: Float = .5f
        @FloatRange(from = 0f.toDouble(), to = 25f.toDouble(), fromInclusive = false)
        var radius: Float = 12f
        var dimmingEffect = false
        var blurredActionBar = true
        var statusBarTranslucent: Boolean? = null
    }
}