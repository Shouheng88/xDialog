package me.shouheng.uix.widget.dialog.title

import android.content.Context
import android.view.Gravity
import androidx.annotation.RestrictTo
import me.shouheng.uix.widget.bean.TextStyleBean
import me.shouheng.uix.widget.databinding.UixDialogTitleSimpleBinding
import me.shouheng.uix.widget.anno.BeautyDialogDSL
import me.shouheng.uix.widget.bean.textStyle

/**
 * Simple dialog title
 *
 * @author <a href="mailto:shouheng2020@gmail.com">Shouheng Wang</a>
 * @version 2019-10-13 09:46
 */
class SimpleTitle private constructor(): ViewBindingDialogTitle<UixDialogTitleSimpleBinding>() {

    private var title: CharSequence? = null
    private var titleStyle: TextStyleBean = GlobalConfig.titleStyle

    override fun doCreateView(ctx: Context) {
        binding.tv.text = title
        binding.tv.setStyle(titleStyle, GlobalConfig.titleStyle)
    }

    @BeautyDialogDSL
    class Builder {
        private var title: CharSequence? = null
        private var titleStyle: TextStyleBean = GlobalConfig.titleStyle

        /** Specify dialog title text. */
        fun withTitle(title: CharSequence) {
            this.title = title
        }

        /** Specify dialog title style. */
        fun withStyle(titleStyle: TextStyleBean) {
            this.titleStyle = titleStyle
        }

        @RestrictTo(RestrictTo.Scope.LIBRARY) fun build(): SimpleTitle {
            val simpleTitle = SimpleTitle()
            simpleTitle.title = title
            simpleTitle.titleStyle = titleStyle
            return simpleTitle
        }
    }

    object GlobalConfig {
        var titleStyle = textStyle {
            withGravity(Gravity.CENTER)
        }
    }
}

/** Create a simple title dialog title by DSL. */
inline fun simpleTitle(
    init: SimpleTitle.Builder.() -> Unit
): SimpleTitle {
    val builder = SimpleTitle.Builder()
    builder.init()
    return builder.build()
}