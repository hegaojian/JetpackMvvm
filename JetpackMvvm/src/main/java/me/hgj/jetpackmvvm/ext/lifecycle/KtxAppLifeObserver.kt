package me.hgj.jetpackmvvm.ext.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import me.hgj.jetpackmvvm.ext.util.logi

/**
 * 作者　: hegaojian
 * 时间　: 20120/1/7
 * 描述　:
 */
object KtxAppLifeObserver : LifecycleObserver {
    var isForeground = false

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    private  fun onForeground() {
        isForeground = true
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    private fun onBackground() {
        isForeground = false
    }

}