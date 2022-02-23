package me.shouheng.xdialogsample

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import androidx.appcompat.widget.AppCompatImageView
import me.shouheng.uix.widget.anno.DialogPosition
import me.shouheng.uix.widget.anno.DialogStyle
import me.shouheng.uix.widget.anno.EmptyViewState
import me.shouheng.uix.widget.anno.LoadingStyle
import me.shouheng.uix.widget.bean.textStyle
import me.shouheng.uix.widget.dialog.content.*
import me.shouheng.uix.widget.dialog.showDialog
import me.shouheng.uix.widget.rv.emptyView
import me.shouheng.utils.ktx.*
import me.shouheng.utils.ui.ImageUtils
import me.shouheng.utils.ui.ViewUtils
import me.shouheng.vmlib.base.ViewBindingActivity
import me.shouheng.vmlib.comn.EmptyViewModel
import me.shouheng.xadapter.createAdapter
import me.shouheng.xadapter.viewholder.onItemClick
import me.shouheng.xdialogsample.components.*
import me.shouheng.xdialogsample.data.AddressSelectLevel.Companion.LEVEL_AREA
import me.shouheng.xdialogsample.databinding.ActivityMainBinding
import me.shouheng.xdialogsample.utils.Utils
import me.shouheng.xdialogsample.utils.onDebouncedClick

class MainActivity: ViewBindingActivity<EmptyViewModel, ActivityMainBinding>() {

    private var customList: CustomList? = null
    private var draggable: Boolean = false

    override fun doCreateView(savedInstanceState: Bundle?) {
        val builder = createDialogBuilder()
        binding.sw.setOnCheckedChangeListener { _, isChecked ->
            draggable = isChecked
        }
        binding.btnNoBg.setOnClickListener {
            showDialog("normal") {
                withBottomSheet(draggable)
                withStyle(DialogStyle.STYLE_WRAP)
                withContent(UpgradeContent())
                withDialogBackground(null)
                onDismiss { toast("Dismissed") }
                onShow { toast("Showed") }
            }
        }
        binding.btnNoBgOneThird.onDebouncedClick {
            builder.withBottomSheet(draggable)
            builder.withStyle(DialogStyle.STYLE_TWO_THIRD)
            builder.build().show(supportFragmentManager, "normal")
        }
        binding.btnNoBgHalf.onDebouncedClick {
            builder.withBottomSheet(draggable)
            builder.withStyle(DialogStyle.STYLE_HALF)
            builder.build().show(supportFragmentManager, "normal")
        }
        binding.btnBlurBg.onDebouncedClick {
            showDialog("normal") {
                withBottomSheet(draggable)
                withStyle(DialogStyle.STYLE_WRAP)
                withNoAnimation(true)
                withBlurBackground(me.shouheng.uix.widget.dialog.BlurEngine.BlurEffect().apply {
                    this.enable = true
                })
                withContent(UpgradeContent())
                withDialogBackground(null)
                onDismiss { toast("Dismissed") }
                onShow { toast("Showed") }
            }
        }
        binding.btnNormal.setOnClickListener {
            showDialog("normal") {
                withBottomSheet(draggable)
                withStyle(DialogStyle.STYLE_WRAP)
                withTitle(red18fTitle("测试标题 [RED|18f]"))
                withContent(MultipleContent())
                withBottom(singleOk())
            }
        }
        onDebouncedClick(binding.btnNormalTop) {
            me.shouheng.uix.widget.dialog.createDialog {
                withBottomSheet(draggable)
                withPosition(DialogPosition.POS_TOP)
                withTitle(boldLeftTitle("测试标题 [BOLD|LEFT]"))
                withContent(MultipleContent())
                withBottom(SampleFooter())
            }.show(supportFragmentManager, "normal_top")
        }
        onDebouncedClick(binding.btnNormalBottom) {
            me.shouheng.uix.widget.dialog.createDialog {
                withBottomSheet(draggable)
                darkMode(true)
                withPosition(DialogPosition.POS_BOTTOM)
                withTitle(simpleTitle("测试标题 [WHITE]"))
                withContent(MultipleContent())
                withBottom(leftMiddleRightFooter())
            }.show(supportFragmentManager, "normal_bottom")
        }
        onDebouncedClick(binding.btnEditorNormal) {
            // The DSL styled way to create dialog.
            showDialog("editor") {
                withBottomSheet(draggable)
                withStyle(DialogStyle.STYLE_HALF)
                withDialogCornerRadius(4f.dp2px())
                withTitle(simpleTitle("普通编辑对话框 [无限制]"))
                withContent(simpleEditor {
                    withClearDrawable(
                        drawableOf(R.drawable.ic_baseline_cancel_24)
                    )
                })
                withBottom(leftMiddleRightFooterWhite())
            }
        }
        binding.btnEditorNumeric.setOnClickListener {
            showDialog("editor") {
                withBottomSheet(draggable)
                withStyle(DialogStyle.STYLE_TWO_THIRD)
                withTitle(simpleTitle("编辑对话框（数字|单行|长度10）"))
                withContent(simpleEditor {
                    withSingleLine(true)
                    withNumeric(true)
                    withContent("10086")
                    withHint("在这里输入数字...")
                    withBottomLineColor(Color.LTGRAY)
                    withMaxLength(10)
                })
                withBottom(confirmCancel())
            }
        }
        binding.btnListNormal.onDebouncedClick {
            showDialog {
                withBottomSheet(draggable)
                withPosition(DialogPosition.POS_BOTTOM)
                withTitle(simpleTitle("简单的列表"))
                withContent(simpleList {
                    withTextStyle(textStyle {
                        withColor(Color.BLACK)
                        withSize(16f)
                        withTypeFace(Typeface.BOLD)
                        withGravity(Gravity.CENTER)
                    })
                    withShowIcon(true)
                    withList(Utils.getSimpleListData())
                    onItemSelected { dialog, item ->
                        toast("${item.id} : ${item.content}")
                        dialog.dismiss()
                    }
                })
            }
        }
        binding.btnAddress.onDebouncedClick {
            showDialog("list") {
                withBottomSheet(draggable)
                withPosition(DialogPosition.POS_BOTTOM)
                withMargin(8f.dp2px())
                withTitle(simpleTitle("地址对话框"))
                withContent(addressContent {
                    withLevel(LEVEL_AREA)
                    onSelected { dialog, province, city, area ->
                        toast("$province - $city - $area")
                        dialog.dismiss()
                    }
                })
            }
        }
        binding.btnContent.onDebouncedClick {
            showDialog("list") {
                withBottomSheet(draggable)
                withStyle(DialogStyle.STYLE_TWO_THIRD)
                withPosition(DialogPosition.POS_BOTTOM)
                outCancelable(true)
                withTitle(simpleTitle("简单内容对话框"))
                withContent(simpleContent {
                    withContent(stringOf(R.string.sample_long_content))
                })
            }
        }
        binding.btnCustomList.onDebouncedClick { showCustomListDialog() }
        binding.btnNotCancelable.onDebouncedClick {
            showDialog("not cancelable") {
                withBottomSheet(draggable)
                withStyle(DialogStyle.STYLE_WRAP)
                outCancelable(false)
                backCancelable(false)
                withDialogBackground(null)
                withContent(UpgradeContent())
            }
        }
        binding.btnSimpleGrid.onDebouncedClick {
            showDialog("grid") {
                withBottomSheet(draggable)
                withMargin(0)
                withPosition(DialogPosition.POS_BOTTOM)
                withTitle(simpleTitle("简单的网格"))
                withContent(simpleGrid<Int> {
                    withLayout(R.layout.item_tool_color)
                    withSpanCount(5)
                    onBind { helper, item ->
                        helper.getView<AppCompatImageView>(R.id.iv).setImageDrawable(ImageUtils.getDrawable(item, 5f.dp().toFloat()))
                    }
                    onItemSelected { dialog, item ->
                        toast("$item")
                        dialog.dismiss()
                    }
                    withList(Utils.getSimpleGridData())
                })
            }
        }
        binding.btnBottomSheet.onDebouncedClick {
            showDialog {
                withBottomSheet(true)
                withFullscreen(true)
                withFitToContents(false)
                withPeekHeight(200f.dp())
                withHalfExpandedRatio(.65f)
                withMargin(0)
                withPosition(DialogPosition.POS_BOTTOM)
                withTitle(simpleTitle("简单的网格"))
                withContent(simpleGrid<Int> {
                    withLayout(R.layout.item_tool_color)
                    withSpanCount(5)
                    onBind { helper, item ->
                        helper.getView<AppCompatImageView>(R.id.iv).setImageDrawable(ImageUtils.getDrawable(item, 5f.dp().toFloat()))
                    }
                    onItemSelected { dialog, item ->
                        toast("$item")
                        dialog.dismiss()
                    }
                    withList(Utils.getSimpleGridData())
                })
            }
        }
    }

    private fun showCustomListDialog() {
        val adapter = createAdapter<SimpleList.Item> {
            withType(SimpleList.Item::class.java, R.layout.uix_dialog_content_list_simple_item) {
                onBind { helper, item ->
                    val tv = helper.getView<me.shouheng.uix.widget.text.NormalTextView>(R.id.tv)
                    tv.text = item.content
                    item.gravity?.let { tv.gravity = it }
                    item.icon?.let { helper.setImageDrawable(R.id.iv, it) }
                }
                this.onItemClick { adapter, _, position ->
                    val item = adapter.data[position] as SimpleList.Item
                    toast("${item.id} : ${item.content}")
                    customList?.getDialog()?.dismiss()
                }
            }
        }
        customList = customList {
            withAdapter(adapter)
            withEmptyView(emptyView(context) {
                withStyle(LoadingStyle.STYLE_IOS)
                withState(EmptyViewState.STATE_LOADING)
                withLoadingTips(stringOf(R.string.common_loading))
                withLoadingTipsColor(Color.BLUE)
            })
        }
        me.shouheng.uix.widget.dialog.createDialog {
            withBottomSheet(draggable)
            withPosition(DialogPosition.POS_BOTTOM)
            withHeight(ViewUtils.getScreenHeight() / 2)
            outCancelable(true)
            withTitle(simpleTitle("自定义列表对话框"))
            withContent(customList!!)
        }.show(supportFragmentManager, "custom-list")
        // 先显示对话框再加载数据的情形
        Handler().postDelayed({
            customList?.hideEmptyView()
            adapter.setNewData(Utils.getCustomListData())
        }, 3000)
    }

    private fun createDialogBuilder(): me.shouheng.uix.widget.dialog.BeautyDialog.Builder {
        val builder = me.shouheng.uix.widget.dialog.BeautyDialog.Builder()
        builder.darkMode(true)
        builder.withPosition(DialogPosition.POS_BOTTOM)
        builder.withContent(MultipleContent())
        builder.withTitle(whiteSimpleTitle("测试标题 [WHITE]"))
        builder.withBottom(leftMiddleRightFooter())
        return builder
    }
}