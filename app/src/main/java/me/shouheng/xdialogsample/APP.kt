package me.shouheng.xdialogsample

import android.app.Application
import me.shouheng.uix.widget.xDialog
import me.shouheng.vmlib.VMLib

class APP: Application() {

    override fun onCreate() {
        super.onCreate()
        VMLib.onCreate(this)
        xDialog.init(this)
    }
}