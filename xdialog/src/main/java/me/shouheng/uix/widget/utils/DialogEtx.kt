package me.shouheng.uix.widget.utils

import android.app.Dialog
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.*
import com.chad.library.adapter.base.BaseViewHolder

/** Hide dialog. */
fun Dialog?.hide() {
    if (this?.isShowing == true) {
        this.dismiss()
    }
}

fun nowString(): String = DialogUtils.nowString()

fun Float.dp(): Int = DialogUtils.dp2px(this)

fun View.gone() { DialogUtils.setGone(this) }

fun View.gone(goneIf: Boolean) {
    this.visibility = if (goneIf) View.GONE else View.VISIBLE
}

@ColorInt fun colorOf(@ColorRes id: Int): Int = DialogUtils.getColor(id)

fun stringOf(@StringRes id: Int): String = DialogUtils.getString(id)

fun drawableOf(@DrawableRes id: Int): Drawable = DialogUtils.getDrawable(id)

/** Make given view gone if satisfy given condition defined by [goneIf]. */
fun BaseViewHolder.goneIf(@IdRes id: Int, goneIf: Boolean) {
    this.getView<View>(id).visibility = if (goneIf) View.GONE else View.VISIBLE
}
