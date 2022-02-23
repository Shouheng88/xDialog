package me.shouheng.uix.widget.dialog.footer

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.StateListDrawable
import androidx.annotation.ColorInt
import androidx.annotation.RestrictTo
import me.shouheng.uix.widget.anno.BeautyDialogDSL
import me.shouheng.uix.widget.anno.BottomButtonStyle
import me.shouheng.uix.widget.anno.BottomButtonStyle.Companion.BUTTON_STYLE_DOUBLE
import me.shouheng.uix.widget.anno.BottomButtonStyle.Companion.BUTTON_STYLE_SINGLE
import me.shouheng.uix.widget.bean.TextStyleBean
import me.shouheng.uix.widget.databinding.UixDialogFooterSimpleBinding
import me.shouheng.uix.widget.dialog.BeautyDialog
import me.shouheng.uix.widget.dialog.content.IDialogContent
import me.shouheng.uix.widget.dialog.title.IDialogTitle
import me.shouheng.uix.widget.utils.DialogUtils
import me.shouheng.uix.widget.utils.gone

/**
 * Simple dialog footer
 *
 * @author <a href="mailto:shouheng2020@gmail.com">Shouheng Wang</a>
 * @version 2019-10-15 11:44
 */
class SimpleFooter private constructor(): ViewBindingDialogFooter<UixDialogFooterSimpleBinding>() {

    private lateinit var dialog: BeautyDialog
    private var dialogContent: IDialogContent? = null
    private var dialogTitle: IDialogTitle? = null

    @BottomButtonStyle
    private var bottomStyle: Int? = BUTTON_STYLE_DOUBLE

    private var leftText:  CharSequence? = null
    private var middleText: CharSequence? = null
    private var rightText:  CharSequence? = null

    private var leftTextStyle   = GlobalConfig.leftTextStyle
    private var middleTextStyle = GlobalConfig.middleTextStyle
    private var rightTextStyle  = GlobalConfig.rightTextStyle

    private var dividerColor: Int? = null
    private var onLeft:   ((dlg: BeautyDialog, title: IDialogTitle?, content: IDialogContent?) -> Unit)? = null
    private var onMiddle: ((dlg: BeautyDialog, title: IDialogTitle?, content: IDialogContent?) -> Unit)? = null
    private var onRight:  ((dlg: BeautyDialog, title: IDialogTitle?, content: IDialogContent?) -> Unit)? = null

    override fun doCreateView(ctx: Context) {
        binding.tvLeft.text = leftText
        binding.tvMiddle.text = middleText
        binding.tvRight.text = rightText

        binding.tvLeft.setStyle(  leftTextStyle,   GlobalConfig.leftTextStyle)
        binding.tvMiddle.setStyle(middleTextStyle, GlobalConfig.middleTextStyle)
        binding.tvRight.setStyle( rightTextStyle,  GlobalConfig.rightTextStyle)

        binding.tvLeft.setOnClickListener {
            onLeft?.invoke(dialog, dialogTitle, dialogContent)
        }
        binding.tvMiddle.setOnClickListener {
            onMiddle?.invoke(dialog, dialogTitle, dialogContent)
        }
        binding.tvRight.setOnClickListener {
            onRight?.invoke(dialog, dialogTitle, dialogContent)
        }

        val cornerRadius = dialog.dialogCornerRadius.toFloat()
        val normalColor = if (dialog.dialogDarkStyle) BeautyDialog.GlobalConfig.darkBGColor else BeautyDialog.GlobalConfig.lightBGColor
        val selectedColor = DialogUtils.computeColor(normalColor, if (dialog.dialogDarkStyle) Color.WHITE else Color.BLACK, .1f)

        binding.tvLeft.background = StateListDrawable().apply {
            val normalDrawable = DialogUtils.getDrawable(normalColor, 0f, 0f, cornerRadius, 0f)
            val selectedDrawable = DialogUtils.getDrawable(selectedColor, 0f, 0f, cornerRadius, 0f)
            addState(intArrayOf(android.R.attr.state_pressed), selectedDrawable)
            addState(intArrayOf(-android.R.attr.state_pressed), normalDrawable)
        }
        binding.tvMiddle.background = StateListDrawable().apply {
            val radius = if (bottomStyle == BUTTON_STYLE_SINGLE) cornerRadius else 0f
            val normalDrawable = DialogUtils.getDrawable(normalColor, 0f, 0f, radius, radius)
            val selectedDrawable = DialogUtils.getDrawable(selectedColor, 0f, 0f, radius, radius)
            addState(intArrayOf(android.R.attr.state_pressed), selectedDrawable)
            addState(intArrayOf(-android.R.attr.state_pressed), normalDrawable)
        }
        binding.tvRight.background = StateListDrawable().apply {
            val normalDrawable = DialogUtils.getDrawable(normalColor, 0f, 0f, 0f, cornerRadius)
            val selectedDrawable = DialogUtils.getDrawable(selectedColor, 0f, 0f, 0f, cornerRadius)
            addState(intArrayOf(android.R.attr.state_pressed), selectedDrawable)
            addState(intArrayOf(-android.R.attr.state_pressed), normalDrawable)
        }

        // 优先使用构建者模式中传入的颜色，如果没有再使用全局配置的颜色，还是没有就使用默认颜色
        val finalDividerColor =
                if (dividerColor == null) {
                    if (GlobalConfig.dividerColor == null) selectedColor
                    else GlobalConfig.dividerColor!!
                } else dividerColor!!
        binding.v1.setBackgroundColor(finalDividerColor)
        binding.v2.setBackgroundColor(finalDividerColor)
        binding.h.setBackgroundColor(finalDividerColor)

        when(bottomStyle) {
            BUTTON_STYLE_SINGLE -> {
                binding.tvLeft.gone()
                binding.tvRight.gone()
                binding.v1.gone()
                binding.v2.gone()
            }
            BUTTON_STYLE_DOUBLE -> {
                binding.tvMiddle.gone()
                binding.v2.gone()
            }
            else -> { /* do nothing */ }
        }
    }

    override fun setDialog(dialog: BeautyDialog) {
        this.dialog = dialog
    }

    override fun setDialogTitle(dialogTitle: IDialogTitle?) {
        this.dialogTitle = dialogTitle
    }

    override fun setDialogContent(dialogContent: IDialogContent?) {
        this.dialogContent = dialogContent
    }

    @BeautyDialogDSL
    class Builder {

        @BottomButtonStyle
        private var style: Int? = BUTTON_STYLE_DOUBLE

        private var leftText:   CharSequence? = null
        private var middleText: CharSequence? = null
        private var rightText:  CharSequence? = null

        private var leftTextStyle   = GlobalConfig.leftTextStyle
        private var middleTextStyle = GlobalConfig.middleTextStyle
        private var rightTextStyle  = GlobalConfig.rightTextStyle

        private var dividerColor: Int? = null

        private var onLeft:   ((dlg: BeautyDialog, title: IDialogTitle?, content: IDialogContent?) -> Unit)? = null
        private var onMiddle: ((dlg: BeautyDialog, title: IDialogTitle?, content: IDialogContent?) -> Unit)? = null
        private var onRight:  ((dlg: BeautyDialog, title: IDialogTitle?, content: IDialogContent?) -> Unit)? = null

        /** Specify footer style. */
        fun withStyle(@BottomButtonStyle style: Int) {
            this.style = style
        }

        /** Specify left button text. */
        fun withLeft(leftText: CharSequence) {
            this.leftText = leftText
        }

        /** Specify left button text tyle. */
        fun withLeftStyle(textStyle: TextStyleBean) {
            this.leftTextStyle = textStyle
        }

        /** Specify middle button text. */
        fun withMiddle(middleText: CharSequence) {
            this.middleText = middleText
        }

        /** Specify middle button text tyle. */
        fun withMiddleStyle(textStyle: TextStyleBean) {
            this.middleTextStyle = textStyle
        }

        /** Specify right button text. */
        fun withRight(rightText: CharSequence) {
            this.rightText = rightText
        }

        /** Specify right button text style. */
        fun withRightStyle(textStyle: TextStyleBean) {
            this.rightTextStyle = textStyle
        }

        /** Callback when left button is clicked. */
        fun onLeft(callback: (dlg: BeautyDialog, title: IDialogTitle?, content: IDialogContent?) -> Unit) {
            this.onLeft = callback
        }

        /** Callback when middle button is clicked. */
        fun onMiddle(callback: (dlg: BeautyDialog, title: IDialogTitle?, content: IDialogContent?) -> Unit) {
            this.onMiddle = callback
        }

        /** Callback when right button is clicked. */
        fun onRight(callback: (dlg: BeautyDialog, title: IDialogTitle?, content: IDialogContent?) -> Unit) {
            this.onRight = callback
        }

        /** Specify divider color. */
        fun withDivider(@ColorInt dividerColor: Int) {
            this.dividerColor = dividerColor
        }

        @RestrictTo(RestrictTo.Scope.LIBRARY) fun build(): SimpleFooter {
            val bottom = SimpleFooter()
            bottom.leftText = leftText
            bottom.leftTextStyle = leftTextStyle
            bottom.middleText = middleText
            bottom.middleTextStyle = middleTextStyle
            bottom.rightText = rightText
            bottom.rightTextStyle = rightTextStyle
            bottom.bottomStyle = style
            bottom.dividerColor = dividerColor
            bottom.onLeft = onLeft
            bottom.onMiddle = onMiddle
            bottom.onRight = onRight
            return bottom
        }
    }

    object GlobalConfig {
        var leftTextStyle = TextStyleBean()
        var middleTextStyle = TextStyleBean()
        var rightTextStyle = TextStyleBean()
        /** 按钮底部的分割线的颜色 */
        @ColorInt var dividerColor: Int? = null
    }
}

/** Create a simple footer by DSL style. */
inline fun simpleFooter(
    init: SimpleFooter.Builder.() -> Unit
): SimpleFooter {
    val builder = SimpleFooter.Builder()
    builder.init()
    return builder.build()
}
