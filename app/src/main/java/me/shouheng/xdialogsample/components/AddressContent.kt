package me.shouheng.xdialogsample.components

import android.content.Context
import android.view.View
import androidx.annotation.RestrictTo
import me.shouheng.uix.widget.anno.BeautyDialogDSL
import me.shouheng.uix.widget.R
import me.shouheng.uix.widget.dialog.content.ViewBindingDialogContent
import me.shouheng.utils.ktx.gone
import me.shouheng.utils.ktx.onDebouncedClick
import me.shouheng.utils.ktx.visible
import me.shouheng.xadapter.createAdapter
import me.shouheng.xdialogsample.data.AddressBean
import me.shouheng.xdialogsample.data.AddressSelectLevel
import me.shouheng.xdialogsample.data.AddressSelectLevel.Companion.LEVEL_AREA
import me.shouheng.xdialogsample.data.AddressSelectLevel.Companion.LEVEL_CITY
import me.shouheng.xdialogsample.databinding.UixDialogContentAddressBinding
import me.shouheng.xdialogsample.utils.AddressUtils
import me.shouheng.xdialogsample.utils.onItemDebouncedClick

/**
 * Address pick dialog content
 *
 * @author <a href="mailto:shouheng2020@gmail.com">Shouheng Wang</a>
 * @version 2019-10-05 10:44
 */
class AddressContent: ViewBindingDialogContent<UixDialogContentAddressBinding>() {

    private var dialog: me.shouheng.uix.widget.dialog.BeautyDialog? = null
    private val rvList = mutableListOf<View>()

    @AddressSelectLevel
    private var maxLevel: Int = LEVEL_CITY
    private var listener: OnAddressSelectedListener? = null

    override fun doCreateView(ctx: Context) {
        val list = AddressUtils.getAddressList()
        val pAdapter = createAdapter<AddressBean> {
            withType(AddressBean::class.java, R.layout.uix_dialog_content_address_item) {
                onBind { helper, item ->
                    helper.setText(R.id.tv, item.name)
                }
            }
        }
        binding.rvProvince.adapter = pAdapter
        pAdapter.setNewData(list)

        val cityAdapter = createAdapter<AddressBean.CityBean> {
            withType(AddressBean.CityBean::class.java, R.layout.uix_dialog_content_address_item) {
                onBind { helper, item ->
                    helper.setText(R.id.tv, item.name)
                }
            }
        }
        binding.rvCity.adapter = cityAdapter

        val areaAdapter = createAdapter<String> {
            withType(String::class.java, R.layout.uix_dialog_content_address_item) {
                onBind { helper, item ->
                    helper.setText(R.id.tv, item)
                }
            }
        }
        binding.rvArea.adapter = areaAdapter

        rvList.addAll(listOf(binding.rvProvince, binding.rvCity, binding.rvArea))

        switchToRv(binding.rvProvince)

        pAdapter.onItemDebouncedClick { _, _, pos ->
            val province = list[pos].name
            val cityBeans = list[pos].city
            cityAdapter.setNewData(cityBeans)
            binding.tvProvince.text = province
            switchToRv(binding.rvCity)
            cityAdapter.onItemDebouncedClick { _, _, cityPos ->
                val city = cityBeans!![cityPos].name
                val areaBeans = cityBeans[cityPos].area
                areaAdapter.setNewData(areaBeans)
                binding.tvCity.text = city
                switchToRv(binding.rvArea)
                if (maxLevel == LEVEL_CITY) {
                    listener?.onSelected(dialog!!, province!!, city, null)
                }
                areaAdapter.onItemDebouncedClick { _, _, areaPos ->
                    val area = areaBeans!![areaPos]
                    binding.tvArea.text = area
                    if (maxLevel == LEVEL_AREA) {
                        listener?.onSelected(dialog!!, province!!, city, area)
                    }
                }
            }
        }

        binding.tvProvince.onDebouncedClick {
            binding.tvCity.text = ""
            binding.tvArea.text = ""
            switchToRv(binding.rvProvince)
        }
        binding.tvCity.onDebouncedClick {
            binding.tvArea.text = ""
            switchToRv(binding.rvCity)
        }
        binding.tvArea.onDebouncedClick {
            switchToRv(binding.rvArea)
        }
    }

    private fun switchToRv(rv: View) {
        rv.visible()
        rvList.filter { it != rv }.forEach { it.gone() }
    }

    override fun setDialog(dialog: me.shouheng.uix.widget.dialog.BeautyDialog) {
        this.dialog = dialog
    }

    /** Get current selection. */
    fun getSelection(): Address {
        return Address(
            binding.tvProvince.text.toString(),
            binding.tvCity.text.toString(),
            binding.tvArea.text.toString()
        )
    }

    internal interface OnAddressSelectedListener {
        fun onSelected(dialog: me.shouheng.uix.widget.dialog.BeautyDialog, province: String, city: String?, area: String?)
    }

    data class Address(
        var province: String,
        var city: String?,
        var area: String?
    )

    @BeautyDialogDSL
    class Builder {
        private var maxLevel: Int = LEVEL_CITY

        private var onAddressSelectedListener: OnAddressSelectedListener? = null

        /** Specify max select level for dialog. */
        fun withLevel(@AddressSelectLevel maxLevel: Int) {
            this.maxLevel = maxLevel
        }

        /** Set address selected callback. */
        fun onSelected(
            listener: (dialog: me.shouheng.uix.widget.dialog.BeautyDialog, province: String, city: String?, area: String?) -> Unit
        ): Builder {
            this.onAddressSelectedListener = object : OnAddressSelectedListener {
                override fun onSelected(dialog: me.shouheng.uix.widget.dialog.BeautyDialog, province: String, city: String?, area: String?) {
                    listener.invoke(dialog, province, city, area)
                }
            }
            return this
        }

        @RestrictTo(RestrictTo.Scope.LIBRARY)  fun build(): AddressContent {
            val content = AddressContent()
            content.maxLevel = maxLevel
            content.listener = onAddressSelectedListener
            return content
        }
    }
}

/** Create address content by DSL. */
inline fun addressContent(
    init: AddressContent.Builder.() -> Unit
): AddressContent {
    val builder = AddressContent.Builder()
    builder.init()
    return builder.build()
}
