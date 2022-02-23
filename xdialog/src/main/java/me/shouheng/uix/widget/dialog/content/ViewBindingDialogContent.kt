package me.shouheng.uix.widget.dialog.content

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.viewbinding.ViewBinding
import me.shouheng.uix.widget.utils.DialogUtils
import java.lang.reflect.ParameterizedType

abstract class ViewBindingDialogContent<T : ViewBinding> : IDialogContent {

    protected lateinit var binding: T
        private set

    override fun getView(ctx: Context): View {
        val vbClass: Class<T> = ((this.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments)
                .firstOrNull { ViewBinding::class.java.isAssignableFrom(it as Class<*>) } as? Class<T>
                ?: throw IllegalStateException("You must specify a view binding class.")
        val method = vbClass.getDeclaredMethod("inflate", LayoutInflater::class.java)
        try {
            binding = method.invoke(null, LayoutInflater.from(ctx)) as T
            doCreateView(ctx)
        } catch (e: Exception) {
            DialogUtils.e("Failed to inflate view binding.")
        }
        return binding.root
    }

    abstract fun doCreateView(ctx: Context)
}
