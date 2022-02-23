package me.shouheng.xdialogsample.utils

import android.view.Gravity
import me.shouheng.uix.widget.dialog.content.SimpleList
import me.shouheng.utils.ktx.colorOf
import me.shouheng.utils.ktx.drawableOf
import me.shouheng.xdialogsample.R

/**
 * @Author wangshouheng
 * @Time 2021/9/20
 */
object Utils {

    fun getSimpleGridData(): List<Int> {
        return listOf(
            colorOf(R.color.tool_item_color_1),
            colorOf(R.color.tool_item_color_2),
            colorOf(R.color.tool_item_color_3),
            colorOf(R.color.tool_item_color_4),
            colorOf(R.color.tool_item_color_5),
            colorOf(R.color.tool_item_color_6),
            colorOf(R.color.tool_item_color_7),
            colorOf(R.color.tool_item_color_8),
            colorOf(R.color.tool_item_color_9),
            colorOf(R.color.tool_item_color_10),
            colorOf(R.color.tool_item_color_11),
            colorOf(R.color.tool_item_color_12)
        )
    }

    fun getSimpleListData(): List<SimpleList.Item> {
        return listOf(
            SimpleList.Item(
                0,
                "秦时明月汉时关，万里长征人未还。\n" + "但使龙城飞将在，不教胡马度阴山。",
                drawableOf(R.drawable.ic_android_black_24dp)
            ),
            SimpleList.Item(
                1,
                "春眠不觉晓，处处闻啼鸟。\n" + "夜来风雨声，花落知多少。",
                drawableOf(R.drawable.ic_baseline_account_balance_24)
            ),
            SimpleList.Item(
                2,
                "君自故乡来，应知故乡事。来日绮窗前，寒梅著花未？",
                drawableOf(R.drawable.uix_close_black_24dp),
                Gravity.START),
            SimpleList.Item(
                3,
                "松下问童子，言师采药去。只在此山中，云深不知处。",
                drawableOf(R.drawable.uix_loading),
                Gravity.END),
            SimpleList.Item(
                4,
                "明日复明日。",
                drawableOf(R.drawable.uix_loading),
                Gravity.START)
        )
    }

    fun getCustomListData(): List<SimpleList.Item> {
        return listOf(
            SimpleList.Item(0, "第 1 项", drawableOf(R.drawable.ic_android_black_24dp)),
            SimpleList.Item(1, "第 2 项", drawableOf(R.drawable.ic_baseline_account_balance_24)),
            SimpleList.Item(2, "第 3 项", drawableOf(R.drawable.uix_close_black_24dp)),
            SimpleList.Item(3, "第 4 项", drawableOf(R.drawable.uix_loading)),
            SimpleList.Item(4, "第 5 项", drawableOf(R.drawable.ic_android_black_24dp)),
            SimpleList.Item(5, "第 6 项", drawableOf(R.drawable.ic_baseline_account_balance_24)),
            SimpleList.Item(6, "第 7 项", drawableOf(R.drawable.uix_close_black_24dp)),
            SimpleList.Item(7, "第 8 项", drawableOf(R.drawable.uix_loading)),
            SimpleList.Item(8, "第 9 项", drawableOf(R.drawable.ic_android_black_24dp)),
            SimpleList.Item(9, "第 10 项", drawableOf(R.drawable.ic_baseline_account_balance_24)),
            SimpleList.Item(10, "第 11 项", drawableOf(R.drawable.uix_close_black_24dp)),
            SimpleList.Item(11, "第 12 项", drawableOf(R.drawable.uix_loading)),
            SimpleList.Item(12, "第 13 项", drawableOf(R.drawable.ic_android_black_24dp)),
            SimpleList.Item(13, "第 14 项", drawableOf(R.drawable.ic_baseline_account_balance_24)),
            SimpleList.Item(14, "第 15 项", drawableOf(R.drawable.uix_close_black_24dp)),
            SimpleList.Item(15, "第 16 项", drawableOf(R.drawable.uix_loading))
        )
    }
}