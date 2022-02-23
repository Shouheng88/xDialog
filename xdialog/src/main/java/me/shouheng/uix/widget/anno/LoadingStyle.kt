package me.shouheng.uix.widget.anno

import androidx.annotation.IntDef
import me.shouheng.uix.widget.anno.LoadingStyle.Companion.STYLE_ANDROID
import me.shouheng.uix.widget.anno.LoadingStyle.Companion.STYLE_IOS

/** 加载小圆圈的风格 å*/
@IntDef(value = [STYLE_ANDROID, STYLE_IOS])
@Target(allowedTargets = [AnnotationTarget.FIELD,
    AnnotationTarget.TYPE_PARAMETER,
    AnnotationTarget.VALUE_PARAMETER])
annotation class LoadingStyle {
    companion object {
        /** Android 风格 */
        const val STYLE_ANDROID                     = 0
        /** iOS 风格 */
        const val STYLE_IOS                         = 1
    }
}