package me.hgj.jetpackmvvm.base.vm

import androidx.lifecycle.ViewModel
import com.kunminx.architecture.domain.message.MutableResult
import me.hgj.jetpackmvvm.core.net.LoadStatusEntity
import me.hgj.jetpackmvvm.core.net.LoadingEntity

/**
 * 作者　：hegaojian
 * 时间　：2025/9/24
 * 描述　：BaseViewModel基类
 */
open class BaseViewModel : ViewModel() {

    val loadingChange: UiLoadingChange by lazy { UiLoadingChange() }

    /**
     * 内置封装好的可通知Activity/fragment 显示隐藏加载框 因为需要跟网络请求显示隐藏loading配套
     */
    //显示加载框
    inner class UiLoadingChange {

        /** 请求时loading */
        val loading by lazy { MutableResult<LoadingEntity>() }

        /** 界面显示错误布局 */
        val showError by lazy { MutableResult<LoadStatusEntity>() }

        /** 界面显示成功 */
        val showSuccess by lazy { MutableResult<Boolean>() }
    }
}