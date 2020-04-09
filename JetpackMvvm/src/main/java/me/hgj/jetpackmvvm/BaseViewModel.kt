package me.hgj.jetpackmvvm

import androidx.lifecycle.ViewModel
import me.hgj.jetpackmvvm.state.SingleLiveEvent

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/12
 * 描述　: ViewModel的基类
 */
open class BaseViewModel : ViewModel() {

    val uiChange: UiChange by lazy { UiChange() }

    /**
     * 可通知Activity/fragment 做相关Ui操作
     */
    inner class UiChange {
        //显示加载框
        val showDialog by lazy { SingleLiveEvent<String>() }
        //隐藏
        val dismissDialog by lazy { SingleLiveEvent<Void>() }
    }

}