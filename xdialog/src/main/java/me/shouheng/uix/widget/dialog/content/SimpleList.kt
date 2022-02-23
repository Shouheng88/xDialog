package me.shouheng.uix.widget.dialog.content

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.LayoutRes
import androidx.annotation.RestrictTo
import androidx.recyclerview.widget.LinearLayoutManager
import me.shouheng.uix.widget.anno.BeautyDialogDSL
import me.shouheng.uix.widget.bean.TextStyleBean
import me.shouheng.uix.widget.databinding.UixDialogContentListSimpleBinding
import me.shouheng.uix.widget.dialog.BeautyDialog
import me.shouheng.uix.widget.utils.goneIf
import me.shouheng.uix.widget.text.NormalTextView
import me.shouheng.xadapter.createAdapter
import me.shouheng.xadapter.viewholder.onItemClick
import me.shouheng.uix.widget.R

/**
 * Simple list dialog content
 *
 * @author <a href="mailto:shouheng2020@gmail.com">Shouheng Wang</a>
 * @version 2019-10-15 19:06
 */
class SimpleList private constructor(): ViewBindingDialogContent<UixDialogContentListSimpleBinding>() {

    private lateinit var dialog: BeautyDialog

    private var list: List<Item> = emptyList()
    private var showIcon = true
    private var itemClickListener: ((dialog: BeautyDialog, item: Item) -> Unit)? = null
    private var textStyle = GlobalConfig.textStyle
    private var itemLayout: Int = R.layout.uix_dialog_content_list_simple_item

    override fun doCreateView(ctx: Context) {
        val adapter = createAdapter<Item> {
            withType(Item::class.java, itemLayout) {
                onBind { helper, item ->
                    val tv = helper.getView<NormalTextView>(R.id.tv)
                    tv.text = item.content
                    tv.setStyle(textStyle, GlobalConfig.textStyle)
                    item.gravity?.let { tv.gravity = it }
                    item.icon?.let { helper.setImageDrawable(R.id.iv, it) }
                    helper.goneIf(R.id.iv, !showIcon)
                }
                this.onItemClick { adapter, _, position ->
                    itemClickListener?.invoke(dialog, adapter.data[position] as Item)
                }
            }
        }
        binding.rv.adapter = adapter
        binding.rv.layoutManager = LinearLayoutManager(ctx)
        adapter.setNewData(list)
    }

    override fun setDialog(dialog: BeautyDialog) {
        this.dialog = dialog
    }

    @BeautyDialogDSL
    class Builder {
        private var list: List<Item> = emptyList()
        private var showIcon = true
        private var textStyle = GlobalConfig.textStyle
        private var onItemSelected: ((dialog: BeautyDialog, item: Item) -> Unit)? = null
        private var itemLayout: Int = R.layout.uix_dialog_content_list_simple_item

        /** Item layout id */
        fun withItemLayout(@LayoutRes layoutId: Int) {
            this.itemLayout = layoutId
        }

        /** Specify data list. */
        fun withList(list: List<Item>) {
            this.list = list
        }

        /** Specify list item text style. */
        fun withTextStyle(textStyle: TextStyleBean) {
            this.textStyle = textStyle
        }

        /** Specify if list should show icon. */
        fun withShowIcon(showIcon: Boolean) {
            this.showIcon = showIcon
        }

        /** Specify callback when item selected. */
        fun onItemSelected(listener: (dialog: BeautyDialog, item: Item) -> Unit) {
            this.onItemSelected = listener
        }

        @RestrictTo(RestrictTo.Scope.LIBRARY)  fun build(): SimpleList {
            val simpleList = SimpleList()
            simpleList.list = list
            simpleList.textStyle = textStyle
            simpleList.showIcon = showIcon
            simpleList.itemClickListener = onItemSelected
            simpleList.itemLayout = itemLayout
            return simpleList
        }
    }

    object GlobalConfig {
        /** Global text style for list item. */
        var textStyle = TextStyleBean()
    }

    data class Item(
        val id: Int,
        var content: CharSequence?,
        var icon: Drawable?,
        var gravity: Int? = null
    )
}

/** Create a simple list by DSL. */
inline fun simpleList(
    init: SimpleList.Builder.() -> Unit
): SimpleList = SimpleList.Builder().apply(init).build()
