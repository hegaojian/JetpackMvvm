package me.hgj.jetpackmvvm.base.ui

import android.view.View
import me.hgj.jetpackmvvm.R
import me.hgj.jetpackmvvm.core.net.LoadingEntity
import me.hgj.jetpackmvvm.ext.util.getStringExt
import me.hgj.jetpackmvvm.widget.loadsir.callback.Callback

/**
 * 作者　: hegaojian
 * 时间　: 2020/11/4
 * 描述　:
 */
interface BaseIView {

    fun createObserver()

    /**
     * 子类可传入需要被包裹的View，做状态显示-空、错误、加载
     * 如果子类不覆盖该方法 那么会将整个当前Activity(toolbar头部除外)/Fragment界面都当做View包裹
     */
    fun getLoadingView(): View? {
        return null
    }

    /**
     * 子类可传入自己的标题栏 不给默认是null
     * @return View?
     */
    fun getTitleBarView(): View? {
        return null
    }

    /**
     * 展示加载中界面
     */
    fun showEmptyUi(message: String = getStringExt(R.string.helper_loading_empty_tip))

    /**
     * 展示加载中界面
     */
    fun showLoadingUi(message: String = getStringExt(R.string.helper_loading_tip))

    /**
     * 展示错误界面
     * @param message String
     */
    fun showErrorUi(message: String = getStringExt(R.string.helper_loading_error_tip))

    /**
     * 界面显示加载成功
     */
    fun showSuccessUi()

    /**
     * 当界面是错误界面，空界面时，点击触发重试
     */
    fun onLoadRetry()

    /**
     * 显示通用loading弹窗dialog
     */
    fun showLoading(setting: LoadingEntity)

    /**
     * 隐藏通用loading弹窗dialog
     */
    fun dismissLoading(setting: LoadingEntity)

    /**
     *  返回当前Activity/Fragment 自定义 空状态布局
     */
    fun getEmptyStateLayout(): Callback?

    /**
     *  返回当前Activity/Fragment 自定义 加载中状态布局
     */
    fun getLoadingStateLayout(): Callback?

    /**
     *  返回当前Activity/Fragment 自定义 错误状态布局
     */
    fun getErrorStateLayout(): Callback?

}