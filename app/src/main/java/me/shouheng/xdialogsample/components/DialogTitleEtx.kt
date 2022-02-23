package me.shouheng.xdialogsample.components

import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import me.shouheng.uix.widget.bean.textStyle
import me.shouheng.uix.widget.dialog.title.SimpleTitle
import me.shouheng.uix.widget.dialog.title.simpleTitle

/** Simple default title. */
fun simpleTitle(title: String): SimpleTitle {
    return simpleTitle {
        withTitle(title)
    }
}

/** Simple title of white text color. */
fun whiteSimpleTitle(title: String): SimpleTitle {
    return simpleTitle {
        withTitle(title)
        withStyle(textStyle {
            withColor(Color.WHITE)
        })
    }
}

/** Red title with text size 18f. */
fun red18fTitle(title: String): SimpleTitle {
    return simpleTitle {
        withTitle(title)
        withStyle(textStyle {
            withSize(18f)
            withColor(Color.RED)
        })
    }
}

/** Bold left styled title. */
fun boldLeftTitle(title: String): SimpleTitle {
    return simpleTitle {
        withTitle(title)
        withStyle(textStyle {
            withTypeFace(Typeface.BOLD)
            withGravity(Gravity.START)
        })
    }
}
