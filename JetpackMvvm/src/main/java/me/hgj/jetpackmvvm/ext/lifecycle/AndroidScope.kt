package me.hgj.jetpackmvvm.ext.lifecycle

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.*
import kotlinx.coroutines.launch
import java.io.Closeable
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * 作者　: hegaojian
 * 时间　: 2023/4/3
 * 描述　:
 */
@Suppress("unused", "MemberVisibilityCanBePrivate", "NAME_SHADOWING")
open class AndroidScope(
    lifecycleOwner: LifecycleOwner? = null,
    lifeEvent: Lifecycle.Event = Lifecycle.Event.ON_DESTROY,
    val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : CoroutineScope, Closeable {

    init {
        runMain {
            lifecycleOwner?.lifecycle?.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (lifeEvent == event) cancel()
                }
            })
        }
    }

    protected var catch: (AndroidScope.(Throwable) -> Unit)? = null
    protected var finally: (AndroidScope.(Throwable?) -> Unit)? = null
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        catch(throwable)
    }

    val scopeGroup = exceptionHandler

    override val coroutineContext: CoroutineContext =
        dispatcher + exceptionHandler + SupervisorJob()


    open fun launch(block: suspend CoroutineScope.() -> Unit): AndroidScope {
        launch(EmptyCoroutineContext) {
            block()
        }.invokeOnCompletion {
            finally(it)
        }
        return this
    }

    protected open fun catch(e: Throwable) {
        catch?.invoke(this@AndroidScope, e) ?: handleError(e)
    }

    /**
     * @param e 如果发生异常导致作用域执行完毕, 则该参数为该异常对象, 正常结束则为null
     */
    protected open fun finally(e: Throwable?) {
        finally?.invoke(this@AndroidScope, e)
    }

    /**
     * 当作用域内发生异常时回调
     */
    open fun catch(block: AndroidScope.(Throwable) -> Unit = {}): AndroidScope {
        this.catch = block
        return this
    }

    /**
     * 无论正常或者异常结束都将最终执行
     */
    open fun finally(block: AndroidScope.(Throwable?) -> Unit = {}): AndroidScope {
        this.finally = block
        return this
    }


    /**
     * 错误处理
     */
    open fun handleError(e: Throwable) {
        Log.e("AndroidScope","handleError: e")
    }

    open fun cancel(cause: CancellationException? = null) {
        val job = coroutineContext[Job]
            ?: error("Scope cannot be cancelled because it does not have a job: $this")
        job.cancel(cause)
    }

    open fun cancel(
        message: String,
        cause: Throwable? = null
    ) = cancel(CancellationException(message, cause))

    override fun close() {
        cancel()
    }

}
