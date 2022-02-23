package me.shouheng.xdialogsample.utils

import android.view.View
import me.shouheng.utils.ktx.onDebouncedClick

inline fun onClick(v: View, crossinline onClicked: (v: View) -> Unit) {
    v.setOnClickListener {
        onClicked(it)
    }
}

inline fun onDebouncedClick(v: View, crossinline onClicked: (v: View) -> Unit) {
    v.onDebouncedClick {
        onClicked(it)
    }
}
