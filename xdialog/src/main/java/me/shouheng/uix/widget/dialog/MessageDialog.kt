package me.shouheng.uix.widget.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import me.shouheng.uix.widget.anno.LoadingStyle
import me.shouheng.uix.widget.bean.TextStyleBean
import me.shouheng.uix.widget.R
import me.shouheng.uix.widget.databinding.UixMessageDialogBinding
import me.shouheng.uix.widget.utils.*

/**
 * Loading and message dialog.
 *
 * @author [Shouheng Wang](mailto:shouheng2020@gmail.com)
 * @version 2020-01-12 17:56
 */
class MessageDialog constructor(builder: Builder) {

    private val message: CharSequence? = builder.message
    private var textStyle: TextStyleBean = GlobalConfig.textStyle
    @LoadingStyle private val loadingStyle: Int = builder.style
    private val cancelable: Boolean = builder.cancelable
    private val loading: Boolean = builder.loading
    private val icon: Drawable? = builder.icon
    @ColorInt private val bgColor: Int = builder.bgColor
    private val borderRadius: Int = builder.borderRadius

    private fun build(context: Context): Dialog {
        val binding = UixMessageDialogBinding.inflate(LayoutInflater.from(context), null, false)
        binding.ll.background = DialogUtils.getDrawable(bgColor, borderRadius.toFloat())

        if (icon != null) binding.img.setImageDrawable(icon)
        else binding.img.visibility = View.GONE

        binding.pb.gone(!loading)
        if (loadingStyle == LoadingStyle.STYLE_IOS)
            binding.pb.indeterminateDrawable = drawableOf(R.drawable.uix_loading)

        binding.tv.text = message
        binding.tv.setStyle(textStyle, GlobalConfig.textStyle)

        val dlg = Dialog(context, if (cancelable) R.style.Dialog_Loading_Cancelable else R.style.Dialog_Loading)
        dlg.setCancelable(cancelable)
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        dlg.setContentView(binding.ll, params)
        return dlg
    }

    class Builder {
        internal var message: CharSequence?      = null
        internal var textStyle: TextStyleBean    = GlobalConfig.textStyle
        internal var style: Int                  = GlobalConfig.loadingStyle
        internal var cancelable: Boolean         = GlobalConfig.cancelable
        internal var icon: Drawable?             = null
        internal var bgColor: Int                = GlobalConfig.bgColor
        internal var loading: Boolean            = GlobalConfig.loading
        internal var borderRadius: Int           = GlobalConfig.bgBorderRadius

        /** Specify message of dialog. */
        fun withMessage(message: CharSequence) {
            this.message = message
        }

        /** Specify message of dialog. */
        fun withMessage(@StringRes msgRes: Int) {
            this.message = stringOf(msgRes)
        }

        /** Set message text style. */
        fun withTextStyle(style: TextStyleBean) {
            this.textStyle = style
        }

        /** Set dialog loading style. */
        fun withStyle(@LoadingStyle style: Int) {
            this.style = style
        }

        /** Set if dialog cancelable. */
        fun cancelable(cancelable: Boolean) {
            this.cancelable = cancelable
        }

        /** Set dialog loading or not. */
        fun withLoading(loading: Boolean) {
            this.loading = loading
        }

        /** Set message icon. */
        fun withIcon(icon: Drawable?) {
            this.icon = icon
        }

        /** Set dialog background color. */
        fun withBgColor(@ColorInt bgColor: Int) {
            this.bgColor = bgColor
        }

        /** Set background color. */
        fun withRadius(borderRadius: Int) {
            this.borderRadius = borderRadius
        }

        fun build(context: Context): Dialog {
            return MessageDialog(this).build(context)
        }
    }

    object GlobalConfig {
        var textStyle                        = TextStyleBean()
        @LoadingStyle var loadingStyle: Int  = LoadingStyle.STYLE_IOS
        var cancelable: Boolean              = true
        var loading: Boolean                 = false
        var bgColor: Int                     = Color.parseColor("#C0000000")
        var bgBorderRadius: Int              = 8f.dp()
    }
}

/** Create a message dialog by DSL. */
inline fun messageDialog(
    context: Context,
    init: MessageDialog.Builder.() -> Unit
): Dialog = MessageDialog.Builder().also(init).build(context)

/** Create a message dialog by DSL. */
@JvmName("showMessageDialog")
inline fun Context.showMessage(
    init: MessageDialog.Builder.() -> Unit
): Dialog = messageDialog(this, init).apply { show() }

/** Simple show message. */
fun Context.showMessage(
    msg: String,
    icon: Drawable? = null,
    cancelable: Boolean = true,
    textStyle: TextStyleBean = MessageDialog.GlobalConfig.textStyle
): Dialog {
    return showMessage {
        withLoading(false)
        withMessage(msg)
        withIcon(icon)
        cancelable(cancelable)
        withTextStyle(textStyle)
    }
}

/** Simple show message of string res. */
fun Context.showMessage(
    @StringRes msgRes: Int,
    icon: Drawable? = null,
    cancelable: Boolean = true,
    textStyle: TextStyleBean = MessageDialog.GlobalConfig.textStyle
): Dialog {
    return showMessage(stringOf(msgRes), icon, cancelable, textStyle)
}

/** Show simple loading dialog. */
fun Context.showLoading(
    msg: String,
    cancelable: Boolean = MessageDialog.GlobalConfig.cancelable,
    textStyle: TextStyleBean = MessageDialog.GlobalConfig.textStyle,
    @LoadingStyle style: Int = MessageDialog.GlobalConfig.loadingStyle
): Dialog {
    return showMessage {
        withMessage(msg)
        withLoading(true)
        withStyle(style)
        cancelable(cancelable)
        withTextStyle(textStyle)
    }
}

/** Show simple loading dialog. */
fun Context.showLoading(
    @StringRes msgRes: Int,
    cancelable: Boolean = MessageDialog.GlobalConfig.cancelable,
    textStyle: TextStyleBean = MessageDialog.GlobalConfig.textStyle,
    @LoadingStyle style: Int = MessageDialog.GlobalConfig.loadingStyle
): Dialog {
    return showMessage {
        withMessage(stringOf(msgRes))
        withLoading(true)
        withStyle(style)
        cancelable(cancelable)
        withTextStyle(textStyle)
    }
}
