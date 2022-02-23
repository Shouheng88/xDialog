package me.shouheng.xdialogsample.components

import android.graphics.Color
import android.graphics.Typeface
import me.shouheng.uix.widget.bean.textStyle
import me.shouheng.uix.widget.dialog.content.SimpleEditor
import me.shouheng.uix.widget.dialog.footer.SimpleFooter
import me.shouheng.uix.widget.dialog.footer.simpleFooter
import me.shouheng.utils.ktx.toast

/** Single 'Ok' styled footer. */
fun singleOk(): SimpleFooter {
    return simpleFooter {
        withStyle(me.shouheng.uix.widget.anno.BottomButtonStyle.BUTTON_STYLE_SINGLE)
        withMiddle("OK")
        withMiddleStyle(textStyle {
            withColor(Color.RED)
            withTypeFace(Typeface.BOLD)
        })
        onMiddle { dlg, _, _ ->
            dlg.dismiss()
        }
    }
}

/** confirm cancel styled footer. */
fun confirmCancel(): SimpleFooter {
    return simpleFooter {
        withStyle(me.shouheng.uix.widget.anno.BottomButtonStyle.BUTTON_STYLE_DOUBLE)
        withLeft("取消")
        withRight("确定")
        withDivider(Color.LTGRAY)
        withLeftStyle(textStyle {
            withColor(Color.GRAY)
        })
        withRightStyle(textStyle {
            withColor(Color.RED)
        })
        onLeft { dlg, _, _ ->
            dlg.dismiss()
        }
        onRight { _, _, content ->
            (content as? SimpleEditor)?.let { toast(it.getContent()?:"") }
        }
    }
}

/** Dialog footer of 'left-middle-right' */
fun leftMiddleRightFooterWhite(): SimpleFooter {
    return simpleFooter {
        withStyle(me.shouheng.uix.widget.anno.BottomButtonStyle.BUTTON_STYLE_TRIPLE)
        withLeft("Left")
        withMiddle("Middle")
        withRight("Right")
        withRightStyle(textStyle {
            withColor(Color.RED)
        })
        onLeft { _, _, _ ->
            toast("Left")
        }
        onMiddle { _, _, _ ->
            toast("Middle")
        }
        onRight { _, _, _ ->
            toast("Right")
        }
    }
}

/** Dialog footer of 'left-middle-right' */
fun leftMiddleRightFooter(): SimpleFooter {
    return simpleFooter {
        withStyle(me.shouheng.uix.widget.anno.BottomButtonStyle.BUTTON_STYLE_TRIPLE)
        withLeft("左")
        withMiddle("中")
        withRight("右")
        withLeftStyle(textStyle {
            withColor(Color.WHITE)
            withSize(14f)
        })
        withMiddleStyle(textStyle {
            withColor(Color.WHITE)
            withSize(16f)
        })
        withRightStyle(textStyle {
            withColor(Color.WHITE)
            withSize(18f)
        })
    }
}


