package me.shouheng.uix.widget.dialog.content

import android.content.Context
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.annotation.RestrictTo
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.shouheng.uix.widget.anno.BeautyDialogDSL
import me.shouheng.uix.widget.databinding.UixDialogContentListCustomBinding
import me.shouheng.uix.widget.dialog.BeautyDialog
import me.shouheng.uix.widget.rv.IEmptyView

/**
 * Custom list dialog content
 *
 * @author <a href="mailto:shouheng2020@gmail.com">Shouheng Wang</a>
 * @version 2019-10-21 13:59
 */
class CustomList private constructor(): ViewBindingDialogContent<UixDialogContentListCustomBinding>() {

    private lateinit var dialog: BeautyDialog

    private var emptyView: IEmptyView? = null
    private var adapter: RecyclerView.Adapter<*>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null

    override fun doCreateView(ctx: Context) {
        emptyView?.getView()?.let {
            binding.flContainer.addView(it, ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT))
            binding.rv.setEmptyView(it)
        }
        binding.rv.adapter = adapter
        binding.rv.layoutManager = layoutManager?:LinearLayoutManager(ctx)
    }

    override fun setDialog(dialog: BeautyDialog) {
        this.dialog = dialog
    }

    fun getDialog(): BeautyDialog = dialog

    /** Show loading view manually. */
    fun showLoading() {
        emptyView?.show()
        emptyView?.showLoading()
    }

    /** Show empty view manually. */
    fun showEmpty() {
        emptyView?.show()
        emptyView?.showEmpty()
    }

    /** Hide empty view manually. */
    fun hideEmptyView() {
        emptyView?.hide()
    }

    @BeautyDialogDSL
    class Builder {
        private var emptyView: IEmptyView? = null
        private var adapter: RecyclerView.Adapter<*>? = null
        private var layoutManager: RecyclerView.LayoutManager? = null

        /** Specify empty view for custom list. */
        fun withEmptyView(emptyView: IEmptyView) {
            this.emptyView = emptyView
        }

        /** Specify recyclerview adapter for custom list. */
        fun withAdapter(adapter: RecyclerView.Adapter<*>) {
            this.adapter = adapter
        }

        /** Specify a layout manager for list. */
        fun withLayoutManager(layoutManager: RecyclerView.LayoutManager) {
            this.layoutManager = layoutManager
        }

        @RestrictTo(RestrictTo.Scope.LIBRARY)  fun build(): CustomList {
            val customList = CustomList()
            customList.adapter = adapter
            customList.emptyView = emptyView
            return customList
        }
    }
}

/** Create a custom list content by DSL. */
inline fun customList(
    init: CustomList.Builder.() -> Unit
): CustomList {
    return CustomList.Builder().apply(init).build()
}
