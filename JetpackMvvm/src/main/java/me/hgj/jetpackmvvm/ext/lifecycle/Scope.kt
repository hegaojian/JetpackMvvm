package me.hgj.jetpackmvvm.ext.lifecycle

import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/**
 * 作者　: hegaojian
 * 时间　: 2023/4/3
 * 描述　:
 */

fun runMain(block: () -> Unit) {
    if (Looper.myLooper() == Looper.getMainLooper()) {
        block()
    } else {
        Handler(Looper.getMainLooper()).post { block() }
    }
}

fun scope(
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    block: suspend CoroutineScope.() -> Unit
): AndroidScope {
    return AndroidScope(dispatcher = dispatcher).launch(block)
}

fun LifecycleOwner.scopeLife(
    lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    block: suspend CoroutineScope.() -> Unit
) = AndroidScope(this, lifeEvent, dispatcher).launch(block)

fun Fragment.scopeLife(
    lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_STOP,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    block: suspend CoroutineScope.() -> Unit
) = AndroidScope(this, lifeEvent, dispatcher).launch(block)