package me.shouheng.uix.widget.anno

import androidx.annotation.IntDef
import me.shouheng.uix.widget.anno.BottomButtonPosition.Companion.BUTTON_POS_LEFT
import me.shouheng.uix.widget.anno.BottomButtonPosition.Companion.BUTTON_POS_MIDDLE
import me.shouheng.uix.widget.anno.BottomButtonPosition.Companion.BUTTON_POS_RIGHT

/** Positions of bottom button. */
@IntDef(value = [BUTTON_POS_LEFT, BUTTON_POS_MIDDLE, BUTTON_POS_RIGHT])
@Target(allowedTargets = [AnnotationTarget.FIELD,
    AnnotationTarget.TYPE_PARAMETER,
    AnnotationTarget.VALUE_PARAMETER])
annotation class BottomButtonPosition {
    companion object {
        const val BUTTON_POS_LEFT                   = 1
        const val BUTTON_POS_MIDDLE                 = 2
        const val BUTTON_POS_RIGHT                  = 3

        fun isLeft(position: Int): Boolean = position == BUTTON_POS_LEFT
        fun isMiddle(position: Int): Boolean = position == BUTTON_POS_MIDDLE
        fun isRight(position: Int): Boolean = position == BUTTON_POS_RIGHT
    }
}
