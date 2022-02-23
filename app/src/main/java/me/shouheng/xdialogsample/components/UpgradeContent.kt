package me.shouheng.xdialogsample.components

import android.content.Context
import android.view.View
import me.shouheng.uix.widget.dialog.content.IDialogContent
import me.shouheng.xdialogsample.R

/**
 * @author <a href="mailto:shouheng2020@gmail.com">Shouheng Wang</a>
 * @version 2019-10-13 17:51
 */
class UpgradeContent : IDialogContent {

    override fun getView(ctx: Context): View = View.inflate(
        ctx, R.layout.layout_dialog_content_upgrade, null
    )

}