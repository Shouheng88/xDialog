package me.shouheng.uix.widget.text

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import me.shouheng.uix.widget.R

/**
 * 带有清除选项的文本编辑器
 *
 * @author [Shouheng Wang](mailto:shouheng2020@gmail.com)
 * @version 2019-10-03 22:55
 */
class ClearEditText : RegexEditText, View.OnTouchListener, View.OnFocusChangeListener, TextWatcher {

    private var mClearDrawable: Drawable? = null

    private var mOnTouchListener: OnTouchListener? = null

    private var mOnFocusChangeListener: OnFocusChangeListener? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    @SuppressLint("ClickableViewAccessibility")
    override fun initialize(context: Context, attrs: AttributeSet?) {
        super.initialize(context, attrs)

        val array = context.obtainStyledAttributes(attrs, R.styleable.ClearEditText)
        val imageTintColor = array.getColor(R.styleable.ClearEditText_clear_image_tint_color, -1)
        array.recycle()

        // Wrap the drawable so that it can be tinted pre Lollipop
        val clearDrawable = ContextCompat.getDrawable(context, R.drawable.uix_close_black_24dp)!!
        mClearDrawable = DrawableCompat.wrap(
                if (imageTintColor == -1) clearDrawable
                else tintDrawable(clearDrawable, imageTintColor)
        )
        mClearDrawable!!.setBounds(0, 0, mClearDrawable!!.intrinsicWidth, mClearDrawable!!.intrinsicHeight)
        setDrawableVisible(false)

        super.setOnTouchListener(this)
        super.setOnFocusChangeListener(this)
        super.addTextChangedListener(this)
    }

    private fun setDrawableVisible(visible: Boolean) {
        if (mClearDrawable!!.isVisible == visible) {
            return
        }

        mClearDrawable!!.setVisible(visible, false)
        val drawables = compoundDrawables
        setCompoundDrawables(drawables[0], drawables[1], if (visible) mClearDrawable else null, drawables[3])
    }

    fun setClearDrawable(drawable: Drawable) {
        this.mClearDrawable = drawable
        mClearDrawable!!.setBounds(0, 0, mClearDrawable!!.intrinsicWidth, mClearDrawable!!.intrinsicHeight)
        setDrawableVisible(text?.isEmpty()?:false)
    }

    override fun setOnFocusChangeListener(onFocusChangeListener: OnFocusChangeListener) {
        mOnFocusChangeListener = onFocusChangeListener
    }

    override fun setOnTouchListener(onTouchListener: OnTouchListener) {
        mOnTouchListener = onTouchListener
    }

    override fun onFocusChange(view: View, hasFocus: Boolean) {
        if (hasFocus && text != null) {
            setDrawableVisible(text!!.isNotEmpty())
        } else {
            setDrawableVisible(false)
        }
        if (mOnFocusChangeListener != null) {
            mOnFocusChangeListener!!.onFocusChange(view, hasFocus)
        }
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        val x = motionEvent.x.toInt()
        if (mClearDrawable!!.isVisible && x > width - paddingRight - mClearDrawable!!.intrinsicWidth) {
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                setText("")
            }
            return true
        }
        return mOnTouchListener != null && mOnTouchListener!!.onTouch(view, motionEvent)
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (isFocused) {
            setDrawableVisible(s.isNotEmpty())
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun afterTextChanged(s: Editable) {}

    private fun tintDrawable(drawable: Drawable, @ColorInt color: Int): Drawable {
        val wrappedDrawable = DrawableCompat.wrap(drawable.mutate())
        DrawableCompat.setTintList(wrappedDrawable, ColorStateList.valueOf(color))
        return wrappedDrawable
    }
}
