package me.shouheng.uix.widget.dialog

import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import me.shouheng.uix.widget.R
import me.shouheng.uix.widget.anno.BeautyDialogDSL
import me.shouheng.uix.widget.anno.DialogPosition
import me.shouheng.uix.widget.anno.DialogPosition.Companion.POS_BOTTOM
import me.shouheng.uix.widget.anno.DialogPosition.Companion.POS_CENTER
import me.shouheng.uix.widget.anno.DialogPosition.Companion.POS_TOP
import me.shouheng.uix.widget.anno.DialogStyle
import me.shouheng.uix.widget.anno.DialogStyle.Companion.STYLE_HALF
import me.shouheng.uix.widget.anno.DialogStyle.Companion.STYLE_MATCH
import me.shouheng.uix.widget.anno.DialogStyle.Companion.STYLE_TWO_THIRD
import me.shouheng.uix.widget.anno.DialogStyle.Companion.STYLE_WRAP
import me.shouheng.uix.widget.dialog.content.IDialogContent
import me.shouheng.uix.widget.dialog.footer.IDialogFooter
import me.shouheng.uix.widget.dialog.title.IDialogTitle
import me.shouheng.uix.widget.utils.DialogUtils
import me.shouheng.uix.widget.utils.dp
import me.shouheng.uix.widget.utils.colorOf
import me.shouheng.uix.widget.utils.nowString

/**
 * 对话框
 *
 * @author <a href="mailto:shouheng2020@gmail.com">Shouheng Wang</a>
 * @version 2019-10-12 18:35
 */
class BeautyDialog : DialogFragment() {

    private var iDialogTitle: IDialogTitle? = null
    private var iDialogContent: IDialogContent? = null
    private var iDialogFooter: IDialogFooter? = null

    private var noAnimation: Boolean = false
    private var dialogPosition: Int = POS_CENTER
    private var dialogStyle: Int = STYLE_MATCH
    var dialogDarkStyle: Boolean = false
        private set

    private var outCancelable = GlobalConfig.outCancelable
    private var backCancelable = GlobalConfig.backCancelable
    private var customBackground = false

    private var onDismissListener: ((dialog: BeautyDialog) -> Unit)? = null
    private var onShowListener: ((dialog: BeautyDialog) -> Unit)? = null

    private var fixedHeight = 0
    private var dialogMargin = GlobalConfig.margin
    private var dialogBackground: Drawable? = null
    var dialogCornerRadius = GlobalConfig.cornerRadius
        private set

    private var blurEngine: BlurEngine = BlurEngine()
    private var blurEffect: BlurEngine.BlurEffect? = null

    private var bottomSheet: Boolean = false
    private var halfExpandedRatio: Float? = null
    private var isFitToContents: Boolean? = null
    private var peekHeight: Int? = null
    private var fullscreen: Boolean? = null

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        blurEngine.onAttach(activity)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val theme = when(dialogStyle) {
            STYLE_WRAP      -> R.style.BeautyDialogWrap
            STYLE_HALF      -> R.style.BeautyDialogHalf
            STYLE_TWO_THIRD -> R.style.BeautyDialogTwoThird
            else            -> R.style.BeautyDialog
        }
        val dialog = if (bottomSheet) {
            BottomSheetDialog(requireContext(), theme).apply {
                halfExpandedRatio?.let { this.behavior.halfExpandedRatio = it }
                isFitToContents  ?.let { this.behavior.isFitToContents   = it }
                peekHeight       ?.let { this.behavior.peekHeight        = it }
            }
        } else {
            AlertDialog.Builder(requireContext(), theme).create()
        }

        blurEffect?.let {
            blurEngine.setBlurEffect(it)
        }

        val content = View.inflate(context, R.layout.uix_dialog_layout, null)
        if (customBackground) {
            content.background = dialogBackground
        } else {
            val dialogColor = if (dialogDarkStyle) GlobalConfig.darkBGColor else GlobalConfig.lightBGColor
            content.background = DialogUtils.getDrawable(dialogColor, dialogCornerRadius.toFloat())
        }
        val llTitle   = content.findViewById<LinearLayout>(R.id.ll_title)
        val llContent = content.findViewById<LinearLayout>(R.id.ll_content)
        val llFooter  = content.findViewById<LinearLayout>(R.id.ll_footer)

        iDialogTitle  ?.setDialog(this)
        iDialogContent?.setDialog(this)
        iDialogFooter ?.setDialog(this)
        iDialogTitle  ?.setDialogContent(iDialogContent)
        iDialogTitle  ?.setDialogFooter(iDialogFooter)
        iDialogContent?.setDialogTitle(iDialogTitle)
        iDialogContent?.setDialogFooter(iDialogFooter)
        iDialogFooter ?.setDialogTitle(iDialogTitle)
        iDialogFooter ?.setDialogContent(iDialogContent)

        if (iDialogTitle != null) {
            val titleView = iDialogTitle!!.getView(requireContext())
            llTitle.addView(titleView)
            val lp = titleView.layoutParams
            lp.height = WRAP_CONTENT
            titleView.layoutParams = lp
        } else {
            llTitle.visibility = View.GONE
        }
        if (iDialogContent != null) {
            val contentView = iDialogContent!!.getView(requireContext())
            llContent.addView(contentView)
            val lp = contentView.layoutParams
            lp.height = if (fixedHeight == 0) WRAP_CONTENT else fixedHeight
            contentView.layoutParams = lp
        } else {
            llContent.visibility = View.GONE
        }
        if (iDialogFooter != null) {
            val footerView = iDialogFooter!!.getView(requireContext())
            llFooter.addView(footerView)
            val lp = footerView.layoutParams
            lp.height = WRAP_CONTENT
            footerView.layoutParams = lp
        } else {
            llFooter.visibility = View.GONE
        }

        when(dialogPosition) {
            POS_TOP -> {
                dialog.window?.setGravity(Gravity.TOP)
                if (!noAnimation) {
                    dialog.window?.setWindowAnimations(R.style.DialogTopAnimation)
                }
            }
            POS_CENTER -> {
                dialog.window?.setGravity(Gravity.CENTER)
                if (!noAnimation) {
                    dialog.window?.setWindowAnimations(R.style.DialogCenterAnimation)
                }
            }
            POS_BOTTOM -> {
                dialog.window?.setGravity(Gravity.BOTTOM)
                if (!noAnimation) {
                    dialog.window?.setWindowAnimations(R.style.DialogBottomAnimation)
                }
            }
        }

        // fix 2020-07-05 : invalid
        // dialog.setOnDismissListener { onDismissListener?.onOnDismiss(this) }
        dialog.setOnShowListener { onShowListener?.invoke(this) }

        dialog.setCanceledOnTouchOutside(outCancelable)
        dialog.setCancelable(backCancelable)
        dialog.setOnKeyListener { _, keyCode, _ ->
            if (!backCancelable && keyCode == KeyEvent.KEYCODE_BACK) {
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }

        if (bottomSheet) {
            dialog.setContentView(content, ViewGroup.MarginLayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                setMargins(dialogMargin, dialogMargin, dialogMargin, dialogMargin)
                if (fullscreen == true) {
                    height = DialogUtils.getScreenHeight()
                }
            })
        } else {
            (dialog as AlertDialog).setView(content, dialogMargin, dialogMargin, dialogMargin, dialogMargin)
        }
        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissListener?.invoke(this)
        blurEngine.onDismiss()
    }

    override fun onStart() {
        blurEngine.onStart(dialog)
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        blurEngine.onResume(false)
    }

    override fun onDetach() {
        super.onDetach()
        blurEngine.onDetach()
    }

    @BeautyDialogDSL
    class Builder {
        private var dialogTitle: IDialogTitle?      = null
        private var dialogContent: IDialogContent?  = null
        private var dialogFooter: IDialogFooter?    = null

        @DialogPosition private var position: Int   = POS_CENTER
        @DialogStyle private var style: Int         = STYLE_MATCH
        private var dark: Boolean                   = false
        private var noAnimation: Boolean            = false

        private var outCancelable                   = GlobalConfig.outCancelable
        private var backCancelable                  = GlobalConfig.backCancelable
        private var customBackground                = false

        private var onDismiss: ((dialog: BeautyDialog) -> Unit)? = null
        private var onShow: ((dialog: BeautyDialog) -> Unit)?    = null

        private var height = 0
        private var margin: Int = GlobalConfig.margin
        private var background: Drawable? = null
            set(value) {
                field = value
                customBackground = true
            }
        private var cornerRadius: Int = GlobalConfig.cornerRadius
        private var blurEffect: BlurEngine.BlurEffect? = null

        private var bottomBheet: Boolean            = false
        /** Configurations when in draggable state. */
        private var halfExpandedRatio: Float?       = null
        private var isFitToContents: Boolean?       = null
        private var peekHeight: Int?                = null
        private var fullscreen: Boolean?            = null

        /** Should the dialog use bottom sheet. */
        fun withBottomSheet(bottomSheet: Boolean) {
            this.bottomBheet = bottomSheet
        }

        /** See [BottomSheetBehavior.halfExpandedRatio], valid when in draggable state. */
        fun withHalfExpandedRatio(halfExpandedRatio: Float) {
            this.halfExpandedRatio = halfExpandedRatio
        }

        /** See [BottomSheetBehavior.isFitToContents], valid when in draggable state. */
        fun withFitToContents(isFitToContents: Boolean) {
            this.isFitToContents = isFitToContents
        }

        /** See [BottomSheetBehavior.peekHeight], valid when in draggable state. */
        fun withPeekHeight(peekHeight: Int) {
            this.peekHeight = peekHeight
        }

        /** Is the dialog fullscreen, valid when in draggable state. */
        fun withFullscreen(fullscreen: Boolean) {
            this.fullscreen = fullscreen
        }

        /** Specify dialog title. */
        fun withTitle(iDialogTitle: IDialogTitle) {
            this.dialogTitle = iDialogTitle
        }

        /** Specify dialog content. */
        fun withContent(iDialogContent: IDialogContent) {
            this.dialogContent = iDialogContent
        }

        /** Specify dialog footer. */
        fun withBottom(iDialogFooter: IDialogFooter) {
            this.dialogFooter = iDialogFooter
        }

        /** Specify dialog position. */
        fun withPosition(@DialogPosition dialogPosition: Int) {
            this.position = dialogPosition
        }

        /** Specify dialog style. */
        fun withStyle(@DialogStyle dialogStyle: Int) {
            this.style = dialogStyle
        }

        /** Specify dialog out cancelable. */
        fun outCancelable(outCancelable: Boolean) {
            this.outCancelable = outCancelable
        }

        /** Specify dialog back cancelable. */
        fun backCancelable(backCancelable: Boolean) {
            this.backCancelable = backCancelable
        }

        /** Callback when dialog dismiss. */
        fun onDismiss(onDismissListener: (dialog: BeautyDialog) -> Unit) {
            this.onDismiss = onDismissListener
        }

        /** Callback when dialog show. */
        fun onShow(onShowListener: (dialog: BeautyDialog) -> Unit) {
            this.onShow = onShowListener
        }

        /** 对话框"内容"的固定高度，单位 px */
        fun withHeight(@Px fixedHeight: Int) {
            this.height = fixedHeight
        }

        /** 对话框边距，单位 px */
        fun withMargin(dialogMargin: Int) {
            this.margin = dialogMargin
        }

        /** Specify dialog dark mode. */
        fun darkMode(darkDialog: Boolean) {
            this.dark = darkDialog
        }

        /**
         * 设置对话框的背景：
         * 1. 如果不调用这个方法将根据主题使用默认的背景，否则将会直接使用设置的背景，即使是 null.
         * 2. 当传入的参数为 null 的时候对话框将不使用任何背景。
         */
        fun withDialogBackground(dialogBackground: Drawable?) {
            this.background = dialogBackground
            customBackground = true
        }

        /** 对话框的默认背景的边角的大小，单位：px */
        fun withDialogCornerRadius(dialogCornerRadius: Int) {
            this.cornerRadius = dialogCornerRadius
        }

        /** Use blur background for dialog or not. */
        fun withBlurBackground(blurEffect: BlurEngine.BlurEffect) {
            this.blurEffect = blurEffect
        }

        /** Don't use animation when displaying dialog. */
        fun withNoAnimation(noAnimation: Boolean) {
            this.noAnimation = noAnimation
        }

        fun build(): BeautyDialog {
            val dialog = BeautyDialog()
            dialog.iDialogTitle = dialogTitle
            dialog.iDialogContent = dialogContent
            dialog.iDialogFooter = dialogFooter
            dialog.dialogPosition = position
            dialog.dialogStyle = style
            dialog.dialogDarkStyle = dark
            dialog.outCancelable = outCancelable
            dialog.backCancelable = backCancelable
            dialog.onDismissListener = onDismiss
            dialog.onShowListener = onShow
            dialog.fixedHeight = height
            dialog.dialogMargin = margin
            dialog.dialogBackground = background
            dialog.customBackground = customBackground
            dialog.dialogCornerRadius = cornerRadius
            dialog.blurEffect = blurEffect
            dialog.noAnimation = noAnimation
            dialog.bottomSheet = bottomBheet
            dialog.halfExpandedRatio = halfExpandedRatio
            dialog.isFitToContents = isFitToContents
            dialog.peekHeight = peekHeight
            dialog.fullscreen = fullscreen
            return dialog
        }
    }

    object GlobalConfig {
        /** 对话框边距，单位 px */
        @Px var margin: Int                     = 20f.dp()
        /** 对话框圆角，单位 px */
        @Px var cornerRadius: Int               = 15f.dp()
        /** 对话框明主题背景色 */
        @ColorInt var lightBGColor: Int         = colorOf(R.color.uix_default_light_bg_color)
        /** 对话框暗主题背景色 */
        @ColorInt var darkBGColor: Int          = colorOf(R.color.uix_default_dark_bg_color)
        /** 点击对话框外部是否可以取消对话框的全局配置 */
        var outCancelable                       = true
        /** 点击返回按钮是否可以取消对话框的全局配置 */
        var backCancelable                      = true
    }
}

/** Create a beauty dialog by DSL style creator. */
inline fun createDialog(
    init: BeautyDialog.Builder.() -> Unit
): BeautyDialog = BeautyDialog.Builder().apply(init).build()

/** Create and show dialog in activity. */
inline fun AppCompatActivity.showDialog(
    tag: String = nowString(),
    init: BeautyDialog.Builder.() -> Unit
): BeautyDialog = BeautyDialog.Builder().apply(init).build().apply {
    show(supportFragmentManager, tag)
}
