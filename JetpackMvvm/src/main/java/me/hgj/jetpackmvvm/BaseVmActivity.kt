package me.hgj.jetpackmvvm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import me.hgj.jetpackmvvm.ext.getVmClazz

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/12
 * 描述　: ViewModelActivity基类，把ViewModel注入进来了
 */
abstract class BaseVmActivity<VM : BaseViewModel> : AppCompatActivity() {

    lateinit var mViewModel: VM

    abstract fun layoutId(): Int

    abstract fun initView()

    abstract fun showLoading(message:String = "请求网络中...")

    abstract fun dismissLoading()

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())
        mViewModel = createViewModel()
        registerUiChange()
        initView()
        createObserver()
        onViewClicked()
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
    private fun registerUiChange() {
        //显示弹窗
        mViewModel.uiChange.showDialog.observe(this, Observer {
            showLoading(if(it.isEmpty()) {
                "请求网络中..."
            } else it)
        })
        //关闭弹窗
        mViewModel.uiChange.dismissDialog.observe(this, Observer {
            dismissLoading()
        })
    }
}