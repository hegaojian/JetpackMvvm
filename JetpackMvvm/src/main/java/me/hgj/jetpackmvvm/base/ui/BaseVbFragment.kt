package me.hgj.jetpackmvvm.base.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import me.hgj.jetpackmvvm.base.vm.BaseViewModel
import me.hgj.jetpackmvvm.ext.util.inflateBinding

/**
 * 作者　: hegaojian
 * 时间　: 2020/11/18
 * 描述　:
 */
abstract class BaseVbFragment<VM : BaseViewModel, VB : ViewBinding> : BaseVmFragment<VM>(),BaseIView {

    //使用了 ViewBinding 就不需要 layoutId了，因为 会从 VB 泛型 找到相关的view
    override val layoutId: Int = 0

    private var _binding: VB? = null
    /** 视图绑定 */
    protected val mBind: VB
        get() = requireNotNull(_binding) { "Fragment 视图未创建或已销毁，无法访问 mBind，请检查调用时机" }

    /**
     * 子类可选择自己实现获取 VB 的方法，传入 inflater、container ，不实现则默认使用框架的反射方式获取
     */
    open fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB {
        // 默认通过反射创建
        return inflateBinding(inflater,container,false)
    }

    override fun initViewDataBind(inflater: LayoutInflater, container: ViewGroup?): View? {
        _binding = createViewBinding(inflater, container)
        return mBind.root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}