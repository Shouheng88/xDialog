package me.shouheng.xdialogsample.utils

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import me.shouheng.utils.ktx.NoDoubleClickListener

abstract class OnItemNoDoubleClickListener: BaseQuickAdapter.OnItemClickListener {

    private var lastClickTime: Long = 0

    override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > NoDoubleClickListener.MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime
            onNoDoubleClick(adapter, view, position)
        }
    }

    protected abstract fun onNoDoubleClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int)
}

fun <T, K : BaseViewHolder> BaseQuickAdapter<T, K>.onItemDebouncedClick(
    click: (adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) -> Unit
) {
    onItemClickListener = object : OnItemNoDoubleClickListener() {
        override fun onNoDoubleClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
            click(adapter, view, position)
        }
    }
}
