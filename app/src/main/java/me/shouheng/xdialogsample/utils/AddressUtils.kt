package me.shouheng.xdialogsample.utils

import com.google.gson.Gson
import com.google.gson.JsonParser
import me.shouheng.utils.app.ResUtils
import me.shouheng.utils.store.IOUtils
import me.shouheng.xdialogsample.data.AddressBean

/**
 * UIX Biz 工具类
 *
 * @author <a href="mailto:shouheng2020@gmail.com">Shouheng Wang</a>
 * @version 2019-10-13 11:57
 */
object AddressUtils {

    private var addresses: List<AddressBean>? = null

    /** 获取地址信息 */
    fun getAddressList(): List<AddressBean> {
        if (addresses != null) return addresses!!
        val ins = ResUtils.getAssets().open("province.json")
        val bytes = IOUtils.is2Bytes(ins)
        val content = String(bytes)
        val list = ArrayList<AddressBean>()
        val gson = Gson()
        try {
            val array = JsonParser().parse(content).asJsonArray
            for (jsonElement in array) {
                list.add(gson.fromJson(jsonElement, AddressBean::class.java))
            }
            addresses = list
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }
}
