package me.hgj.jetpackmvvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import me.hgj.jetpackmvvm.ext.getVmClazz

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/12
 * 描述　: 包含Viewmodel 和Databind ViewModelActivity基类，把ViewModel 和Databind注入进来了
 * 需要使用Databind的清继承它
 */
abstract class BaseVmDbActivity<VM : BaseViewModel, DB : ViewDataBinding> : AppCompatActivity() {

    lateinit var mViewModel: VM

    lateinit var mDatabind: DB

    abstract fun layoutId(): Int

    abstract fun initView()

    /**
     * 绑定该视图的点击事件 需要给view设置普通的点击事件时可在activity中重写使用 例子
     *   override fun onViewClicked() {
            setOnclick(listOf(viewId1,viewId2)) {
                when (it.id) {
                    R.id.viewId1 -> {

                                }
                    R.id.viewId2 -> {

                                 }
                            }
                    }
            }
     */
    open fun onViewClicked(){}

    abstract fun showLoading(message:String = "请求网络中...")

    abstract fun dismissLoading()

    abstract fun showToast(message:String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createViewDataBinding()
        mViewModel = createViewModel()
        registorDefUIChange()
        initView()
        createObserver()
        onViewClicked()
    }

    /**
     * 创建DataBinding
     */
    private fun createViewDataBinding() {
        mDatabind = DataBindingUtil.setContentView(this, layoutId())
        mDatabind.lifecycleOwner = this
    }

    /**
     * 创建viewModel
     */
    private fun createViewModel(): VM {
        return ViewModelProvider(this).get(getVmClazz(this) as Class<VM>)
    }


    /**
     * 创建观察者
     */
    abstract fun createObserver()

    /**
     * 注册 UI 事件
     */
    private fun registorDefUIChange() {
        mViewModel.uiChange.showDialog.observe(this, Observer {
            showLoading(if(it.isEmpty()) {
                "请求网络中..."
            } else it)
        })
        mViewModel.uiChange.dismissDialog.observe(this, Observer {
            dismissLoading()
        })
    }
}