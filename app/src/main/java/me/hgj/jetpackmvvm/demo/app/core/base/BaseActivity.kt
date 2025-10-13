package me.hgj.jetpackmvvm.demo.app.core.base

import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding
import me.hgj.jetpackmvvm.base.ui.BaseVbActivity
import me.hgj.jetpackmvvm.base.vm.BaseViewModel
import me.hgj.jetpackmvvm.core.net.LoadingEntity
import me.hgj.jetpackmvvm.demo.app.core.ext.dismissAppLoadingExt
import me.hgj.jetpackmvvm.demo.app.core.ext.initClose
import me.hgj.jetpackmvvm.demo.app.core.ext.showAppLoadingExt
import me.hgj.jetpackmvvm.demo.databinding.IncludeToolbarBinding

/**
 * 时间　: 2019/12/21
 * 作者　: hegaojian
 * 描述　: 你项目中的Activity基类，在这里实现显示弹窗，吐司，还有加入自己的需求操作，可以参考 BaseIView
 *
 */
abstract class BaseActivity<VM : BaseViewModel, DB : ViewBinding> : BaseVbActivity<VM, DB>() {

    lateinit var mToolbar: Toolbar

    open val title = ""

    /**
     * 定义了自己的头部 ，仅供参考
     */
    override fun getTitleBarView(): View? {
        mToolbar = IncludeToolbarBinding.inflate(layoutInflater).toolbar
        // 初始化 Toolbar（默认返回按钮）
        mToolbar.initClose(title) {
            finish()
        }
        return mToolbar
    }

    /**
     * 这里我自定义了自己项目的 loading ，仅供参考 ，BaseFragment我暂时没有定义，使用的是框架中的默认loading, 如果要改loading，ac/fm都要修改重写
     */
    override fun showLoading(setting: LoadingEntity) {
        showAppLoadingExt(setting.loadingMessage,setting.coroutineScope)
    }

    /**
     * 关闭(与showLoading配套使用),BaseFragment我暂时没有定义，使用的是框架中的默认loading , 如果要改loading，ac/fm都要修改重写
     */
    override fun dismissLoading(setting: LoadingEntity) {
        dismissAppLoadingExt()
    }
}