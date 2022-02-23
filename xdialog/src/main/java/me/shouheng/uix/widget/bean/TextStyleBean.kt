package me.shouheng.uix.widget.bean

import androidx.annotation.ColorInt
import androidx.annotation.RestrictTo
import androidx.annotation.Size
import me.shouheng.uix.widget.anno.BeautyDialogDSL
import java.io.Serializable

/** Bean of text style. */
data class TextStyleBean(
    @ColorInt var textColor: Int? = null,
    @Size var textSize: Float?    = null,
    var typeFace: Int?            = null,
    var gravity: Int?             = null
): Serializable

@BeautyDialogDSL class TextStyleBeanBuilder {
    @ColorInt private var textColor: Int? = null
    @Size private var textSize: Float?    = null
    private var typeFace: Int?            = null
    private var gravity: Int?             = null

    /** Specify text color for text view. */
    fun withColor(textColor: Int) {
        this.textColor = textColor
    }

    /** Specify text size for text view. */
    fun withSize(textSize: Float) {
        this.textSize = textSize
    }

    /** Specify text typeface for text view. */
    fun withTypeFace(typeFace: Int) {
        this.typeFace = typeFace
    }

    /** Specify gravity for text view. */
    fun withGravity(gravity: Int) {
        this.gravity = gravity
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY) fun build(): TextStyleBean {
        return TextStyleBean(textColor, textSize, typeFace, gravity)
    }
}

/** Get a text style bean. */
inline fun textStyle(
    init: TextStyleBeanBuilder.() -> Unit
): TextStyleBean {
    val builder = TextStyleBeanBuilder()
    builder.init()
    return builder.build()
}
