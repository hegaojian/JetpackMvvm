package me.hgj.jetpackmvvm.ext

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import me.hgj.jetpackmvvm.*
import java.lang.reflect.ParameterizedType

/**
 * 获取vm clazz
 */
@Suppress("UNCHECKED_CAST")
fun <VM> getVmClazz(obj: Any): VM {
    return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as VM
}

fun <VM : BaseViewModel> AppCompatActivity.getViewmodel():VM{
    return ViewModelProvider(this).get(getVmClazz(this) as Class<VM>)
}





