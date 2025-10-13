package me.hgj.jetpackmvvm.base.ui

import android.view.View
import androidx.viewbinding.ViewBinding
import com.noober.background.BackgroundLibrary
import me.hgj.jetpackmvvm.base.vm.BaseViewModel
import me.hgj.jetpackmvvm.ext.util.inflateBinding

/**
 * 作者　: hegaojian
 * 时间　: 2021/8/10
 * 描述　:
 */
abstract class BaseVbActivity<VM : BaseViewModel,VB: ViewBinding> : BaseVmActivity<VM>(), BaseIView {

    //使用了 ViewBinding 就不需要 layoutId了，因为 会从 VB 泛型 找到相关的view
    override val layoutId: Int = 0
    lateinit var mBind: VB

    /**
     * 子类可选择自己实现获取 VB 的方法， 不实现则默认使用框架的反射方式获取
     */
    open fun createViewBinding(): VB {
        // 默认通过反射创建
        return inflateBinding()
    }

    override fun initViewDataBind(): View? {
        mBind = createViewBinding()
        BackgroundLibrary.inject(this)
        return mBind.root
    }
}