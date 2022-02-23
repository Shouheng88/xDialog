package me.shouheng.uix.widget.dialog.content

import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import androidx.annotation.RestrictTo
import me.shouheng.uix.widget.anno.BeautyDialogDSL
import me.shouheng.uix.widget.bean.TextStyleBean
import me.shouheng.uix.widget.bean.textStyle
import me.shouheng.uix.widget.databinding.UixDialogContentSimpleBinding

/**
 * Simple dialog content for text
 *
 * @author <a href="mailto:shouheng2020@gmail.com">Shouheng Wang</a>
 * @version 2019-10-13 09:46
 */
class SimpleContent private constructor(): ViewBindingDialogContent<UixDialogContentSimpleBinding>() {

    private var content: CharSequence? = null
    private var textStyle: TextStyleBean = GlobalConfig.textStyle

    override fun doCreateView(ctx: Context) {
        binding.tv.text = content
        binding.tv.setStyle(textStyle)
    }

    @BeautyDialogDSL
    class Builder {
        private var content: CharSequence? = null
        private var textStyle: TextStyleBean = GlobalConfig.textStyle

        /** Specify content. */
        fun withContent(content: CharSequence) {
            this.content = content
        }

        /** Specify content text style. */
        fun withStyle(textStyle: TextStyleBean) {
            this.textStyle = textStyle
        }

        @RestrictTo(RestrictTo.Scope.LIBRARY)  fun build(): SimpleContent {
            val simpleContent = SimpleContent()
            simpleContent.content = content
            simpleContent.textStyle = textStyle
            return simpleContent
        }
    }

    /** Global and default configurations for simple content. */
    object GlobalConfig {
        var textStyle = textStyle {
            withSize(16f)
            withTypeFace(Typeface.NORMAL)
            withGravity(Gravity.CENTER)
        }
    }
}

/** Create a simple content by DSL. */
inline fun simpleContent(
    init: SimpleContent.Builder.() -> Unit
): SimpleContent = SimpleContent.Builder().apply(init).build()
