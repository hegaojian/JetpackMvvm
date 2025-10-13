package me.hgj.jetpackmvvm.ext.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import me.hgj.jetpackmvvm.base.ui.BaseVmFragment
import me.hgj.jetpackmvvm.base.vm.BaseViewModel
import me.hgj.jetpackmvvm.base.ui.BaseVmActivity
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.ParameterizedType

/**
 * 作者　: hegaojian
 * 时间　: 2021/12/21
 * 描述　: ViewBinding DataBinding 反射
 */

@JvmName("inflateBinding")
fun <VB : ViewBinding> AppCompatActivity.inflateBinding(): VB =
    bindingClass<VB>(this) { clazz ->
        clazz.getMethod("inflate", LayoutInflater::class.java).invoke(null, layoutInflater) as VB
    }.also { binding ->
        if (binding is ViewDataBinding) {
            binding.lifecycleOwner = this
        }
    }

@JvmName("inflateBinding")
fun <VB : ViewBinding> Fragment.inflateBinding(layoutInflater: LayoutInflater, parent: ViewGroup?, attachToParent: Boolean): VB =
    bindingClass<VB>(this) { clazz ->
        clazz.getMethod("inflate", LayoutInflater::class.java, ViewGroup::class.java, Boolean::class.java)
            .invoke(null, layoutInflater, parent, attachToParent) as VB
    }.also { binding ->
        if (binding is ViewDataBinding) {
            binding.lifecycleOwner = viewLifecycleOwner
        }
    }

private fun <VB : ViewBinding> bindingClass(any: Any, block: (Class<VB>) -> VB): VB {
    var genericSuperclass = any.javaClass.genericSuperclass
    var superclass = any.javaClass.superclass
    while (superclass != null) {
        if (genericSuperclass is ParameterizedType) {
            try {
                val types = genericSuperclass.actualTypeArguments
                for (type in types) {
                    if (type is Class<*> && ViewBinding::class.java.isAssignableFrom(type)) {
                        @Suppress("UNCHECKED_CAST")
                        return block.invoke(type as Class<VB>)
                    }
                }
            } catch (e: NoSuchMethodException) {
            } catch (e: ClassCastException) {
            } catch (e: InvocationTargetException) {
                throw e.targetException
            }
        }
        genericSuperclass = superclass.genericSuperclass
        superclass = superclass.superclass
    }
    throw IllegalArgumentException("没有找到ViewBinding泛型")
}

/**
 * 创建viewModel
 */
fun <VM: BaseViewModel> BaseVmActivity<VM>.createViewModel(): VM {
    val clazz = (this.javaClass.genericSuperclass as ParameterizedType)
        .actualTypeArguments[0] as Class<VM>
    return ViewModelProvider(this)[clazz]
}

/**
 * 创建viewModel
 */
fun <VM:BaseViewModel> BaseVmFragment<VM>.createViewModel(): VM {
    val clazz = (this.javaClass.genericSuperclass as ParameterizedType)
        .actualTypeArguments[0] as Class<VM>
    return ViewModelProvider(this)[clazz]
}