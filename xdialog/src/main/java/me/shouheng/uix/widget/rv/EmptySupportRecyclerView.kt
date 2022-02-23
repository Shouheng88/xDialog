package me.shouheng.uix.widget.rv

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.view.View

/** The recyclerview support empty state. */
class EmptySupportRecyclerView : RecyclerView {

    private var emptyView: View? = null

    private var emptyCount = 0

    private val observer = object : RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            updateEmptyState()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            updateEmptyState()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            updateEmptyState()
        }
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    private fun updateEmptyState() {
        val adapter = adapter
        if (adapter != null) {
            emptyView?.visibility = if (adapter.itemCount != emptyCount) View.GONE else View.VISIBLE
        }
    }

    override fun setAdapter(adapter: Adapter<*>?) {
        super.setAdapter(adapter)
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer)
            observer.onChanged()
        }
    }

    /**
     * 设置列表为空时展示的控件，这里的 emptyCount 用来指定，
     * 当 adapter 中当数据为多少当时候表示列表为空（因如果为 adapter 设置了头部或底部当情况）
     */
    fun setEmptyView(emptyView: View, emptyCount: Int = 0) {
        this.emptyView = emptyView
        this.emptyCount = emptyCount
    }
}
