package me.hgj.jetpackmvvm.core.data

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kunminx.architecture.domain.message.MutableResult
import me.hgj.jetpackmvvm.base.ui.BaseVmActivity
import me.hgj.jetpackmvvm.core.net.LoadStatusEntity
import me.hgj.jetpackmvvm.core.net.LoadingType
import me.hgj.jetpackmvvm.ext.util.toast

/**
 * 作者　：hegaojian
 * 时间　：2025/9/24
 * 描述　：自定义结果集封装类
 */
class ApiResultObserver<T> {
    /** 当成功时回调 */
    var onSuccess: (T) -> Unit = {}

    /** 当失败时回调，注意这里是可以不传的，如果不传走activity/fragment 中的默认错误处理 */
    var onError: ((LoadStatusEntity) -> Unit)? = null

    /** 这里封装了一下，方便大家好写一点，可以直接 onSuccess { } */
    fun onSuccess(block: (T) -> Unit) {
        onSuccess = block
    }

    /** 这里封装了一下， 方便大家好写一点，可以直接 onError { }*/
    fun onError(block: (LoadStatusEntity) -> Unit) {
        onError = block
    }
}

/**
 * 为 LiveData<ApiResult<T>> 添加扩展函数，用于监听数据回调。
 *
 * 功能说明：
 * 1. 自动订阅 LiveData，并根据 ApiResult 类型调用对应回调：
 *    - Success -> 调用 observerBuilder 中的 onSuccess 方法
 *    - Error   -> 调用 observerBuilder 中的 onError 方法 可不传递 onError 方法，不传就走默认处理
 *
 * 使用示例：
 * ```
 * xx.obs(viewLifecycleOwner)  {
 *     onSuccess { data ->
 *         // 处理成功数据
 *     }
 *     onError { status ->
 *         // 处理错误状态
 *     }
 * }
 * ```
 *
 * 也可以
 *
 * ```
 *  * xx.obs(viewLifecycleOwner)  {
 *  *     onSuccess { data ->
 *  *         // 处理成功数据
 *  *     }
 *  * }
 *  * ```
 *
 * 参数：
 * @param lifecycle LifecycleOwner
 * @param observerBuilder 配置 Success/Error 回调的构建器
 */
fun <T> LiveData<ApiResult<T>>.obs(
    lifecycle: LifecycleOwner,
    observerBuilder: ApiResultObserver<T>.() -> Unit
) {
    val observer = ApiResultObserver<T>().apply(observerBuilder)
    this.observe(lifecycle) { result ->
        when (result) {
            is ApiResult.Success -> observer.onSuccess(result.data)
            is ApiResult.Error -> {
                if (observer.onError != null) {
                    observer.onError?.invoke(result.loadStatus)
                } else {
                    //不传onErr时， 走默认错误流程，吐司错误信息
                    if (result.loadStatus.loadingType != LoadingType.LOADING_NULL) {
                        result.loadStatus.msg.toast()
                    }
                }
            }
        }
    }
}

/**
 * 让 MutableLiveData 支持类似属性赋值的写法：
 * ```kotlin
 * liveData.postValue = data
 * ```
 */
inline var <reified T> MutableLiveData<T>.postValue: T?
    get() {
        return this.value
    }
    set(value) {
        this.postValue(value)
    }

/**
 * 让 MutableLiveData 支持类似属性赋值的写法：
 * ```kotlin
 * liveData.postValue = data
 * ```
 */
inline var <reified T> MutableResult<T>.postValue: T?
    get() {
        return this.value
    }
    set(value) {
        this.postValue(value)
    }


/**
 * 用于统一封装接口请求的返回结果
 * - 使用 sealed class，保证返回结果只会是 Success 或 Error 两种情况
 * @param T 接口请求成功时返回的数据类型
 */
sealed class ApiResult<out T> {

    /**
     * 请求成功
     * @param data 请求成功返回的数据
     */
    data class Success<out T>(val data: T) : ApiResult<T>()

    /**
     * 请求失败
     *
     * @param loadStatus 封装错误状态的信息（例如错误码、错误提示、异常信息等）
     */
    data class Error(val loadStatus: LoadStatusEntity) : ApiResult<Nothing>()
}

