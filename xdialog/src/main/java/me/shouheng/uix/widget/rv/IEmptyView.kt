package me.shouheng.uix.widget.rv

import android.view.View

/**
 * 列表为空的控件的接口
 *
 * @author <a href="mailto:shouheng2020@gmail.com">Shouheng Wang</a>
 * @version 2019-10-20 15:46
 */
interface IEmptyView {

    /** 设置列表为空的控件为"加载"状态 */
    fun showLoading()

    /** 设置列表为空的控件为"列表为空"状态 */
    fun showEmpty()

    /** 显示控件 */
    fun show()

    /** 隐藏控件 */
    fun hide()

    /** 获取控件 */
    fun getView(): View
}