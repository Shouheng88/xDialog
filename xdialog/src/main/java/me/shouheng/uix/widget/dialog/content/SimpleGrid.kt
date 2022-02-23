package me.shouheng.uix.widget.dialog.content

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.annotation.RestrictTo
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseViewHolder
import me.shouheng.uix.widget.anno.BeautyDialogDSL
import me.shouheng.uix.widget.databinding.UixDialogContentListSimpleBinding
import me.shouheng.uix.widget.dialog.BeautyDialog
import me.shouheng.xadapter.createAdapter
import me.shouheng.xadapter.viewholder.onItemClick
import java.lang.reflect.ParameterizedType

/** Simple grid content for dialog. */
class SimpleGrid<T> private constructor(): ViewBindingDialogContent<UixDialogContentListSimpleBinding>() {

    private lateinit var dialog: BeautyDialog

    internal var itemLayout: Int? = null
    internal var binder: ((helper: BaseViewHolder, item: T) -> Unit)? = null
    internal var onItemSelected: ((dialog: BeautyDialog, item: T) -> Unit)? = null
    internal var list: List<T> = emptyList()
    internal var spanCount = 4

    override fun doCreateView(ctx: Context) {
        val typeClass: Class<T> = ((this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments)
            .firstOrNull { ViewBinding::class.java.isAssignableFrom(it as Class<*>) } as? Class<T>
            ?: throw IllegalStateException("You must specify a class type for simple grid.")
        if (itemLayout == null) throw IllegalStateException("You must specify item layout.")
        val adapter = createAdapter<T> {
            withType(typeClass, itemLayout!!) {
                onBind { helper, item ->
                    binder?.invoke(helper, item)
                }
                this.onItemClick { adapter, view, position ->
                    onItemSelected?.invoke(dialog, adapter.data[position] as T)
                }
            }
        }
        binding.rv.adapter = adapter
        binding.rv.layoutManager = GridLayoutManager(ctx, spanCount)
        adapter.setNewData(list)
    }

    override fun setDialog(dialog: BeautyDialog) {
        this.dialog = dialog
    }

    @BeautyDialogDSL
    class Builder<T> {
        @LayoutRes
        private var itemLayout: Int? = null
        private var list: List<T> = emptyList()
        private var onItemSelected: ((dialog: BeautyDialog, item: T) -> Unit)? = null
        private var spanCount = 4
        private var binder: ((helper: BaseViewHolder, item: T) -> Unit)? = null

        /** Specify layout for item. */
        fun withLayout(@LayoutRes layout: Int) {
            this.itemLayout = layout
        }

        /** Specify bind logic for item. */
        fun onBind(binder: (helper: BaseViewHolder, item: T) -> Unit) {
            this.binder = binder
        }

        /** Specify span count for item. */
        fun withSpanCount(spanCount: Int) {
            this.spanCount = spanCount
        }

        /** Specify data list. */
        fun withList(list: List<T>) {
            this.list = list
        }

        /** Specify callback when item selected. */
        fun onItemSelected(listener: (dialog: BeautyDialog, item: T) -> Unit) {
            this.onItemSelected = listener
        }

        @RestrictTo(RestrictTo.Scope.LIBRARY) fun build(): SimpleGrid<T> {
            val simpleList = SimpleGrid<T>()
            simpleList.list = list
            simpleList.spanCount = spanCount
            simpleList.onItemSelected = onItemSelected
            simpleList.itemLayout = itemLayout
            simpleList.binder = binder
            return simpleList
        }
    }
}

/** Create a simple grid by DSL. */
inline fun <T> simpleGrid(
    init: SimpleGrid.Builder<T>.() -> Unit
): SimpleGrid<T> = SimpleGrid.Builder<T>().apply(init).build()
