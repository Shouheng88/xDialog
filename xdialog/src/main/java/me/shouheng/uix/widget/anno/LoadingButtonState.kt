package me.shouheng.uix.widget.anno

import androidx.annotation.IntDef
import me.shouheng.uix.widget.anno.LoadingButtonState.Companion.LOADING_STATE_DISABLE
import me.shouheng.uix.widget.anno.LoadingButtonState.Companion.LOADING_STATE_LOADING
import me.shouheng.uix.widget.anno.LoadingButtonState.Companion.LOADING_STATE_NORMAL

/** 带进度条的按钮的状态 */
@IntDef(value = [LOADING_STATE_NORMAL, LOADING_STATE_LOADING, LOADING_STATE_DISABLE])
@Target(allowedTargets = [AnnotationTarget.FIELD,
    AnnotationTarget.TYPE_PARAMETER,
    AnnotationTarget.VALUE_PARAMETER])
annotation class LoadingButtonState {
    companion object {
        /** 正常状态 */
        const val LOADING_STATE_NORMAL              = 0
        /** 加载状态 */
        const val LOADING_STATE_LOADING             = 1
        /** 一般状态 */
        const val LOADING_STATE_DISABLE             = 2
    }
}