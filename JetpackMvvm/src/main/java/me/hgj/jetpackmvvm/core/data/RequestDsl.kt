package me.hgj.jetpackmvvm.core.data

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import me.hgj.jetpackmvvm.R
import me.hgj.jetpackmvvm.base.vm.BaseViewModel
import me.hgj.jetpackmvvm.core.net.LoadStatusEntity
import me.hgj.jetpackmvvm.core.net.LoadingEntity
import me.hgj.jetpackmvvm.core.net.LoadingType
import me.hgj.jetpackmvvm.ext.util.code
import me.hgj.jetpackmvvm.ext.util.getStringExt
import me.hgj.jetpackmvvm.ext.util.logE
import me.hgj.jetpackmvvm.ext.util.msg

/**
 * 发起网络或本地请求并返回可观察的 LiveData<ApiResult<T>>。
 *
 * 功能说明：
 * 1. 封装请求参数和请求逻辑，支持同步或异步操作。
 * 2. 自动创建 MutableLiveData<ApiResult<T>> 并返回，便于在 UI 层直接监听。
 * 3. 可自定义请求 loading 类型、加载提示、分页刷新等参数。
 * 4. 请求成功时，LiveData 发射 ApiResult.Success(result)，result为 onRequest{} 中的最后一行数据。
 * 5. 请求失败时，LiveData 发射 ApiResult.Error(loadStatus) 并根据 loadingType 自动处理 loading 状态。
 * 使用示例：
 * ```
 * ViewModel中：
 * fun fetchUserData()  = request {
 *     onRequest {
 *         // 异步请求
 *         val data = repository.fetchUserName()
 *         // 可选同步处理
 *         data.toUpperCase()
 *     }
 *     loadingType = LoadingType.LOADING_DIALOG
 *     loadingMessage = "正在加载用户数据..."
 * }
 * Ui层中：
 * xx.obs(lifecycleOwner) {
 *     onSuccess { value ->
 *         // 处理成功数据
 *     }
 *     onError { status ->
 *         // 处理错误状态（可选，不提供则走默认处理）
 *     }
 * }
 * ```
 * 参数：
 *  * @param requestParameterDslClass 配置请求参数的 DSL，包括：
 *  *  - onRequest { }          // 协程请求方法体，返回 T 类型结果
 *  *  - loadingType             // 请求加载状态类型
 *  *  - loadingMessage          // 请求加载提示文本
 *  *
 *  * 返回值：
 *  * MutableLiveData<ApiResult<T>>，可直接在 UI 层通过扩展函数 obs 监听。
 */
fun <T> BaseViewModel.request(requestParameterDslClass: RequestParameterDsl<T>.() -> Unit): MutableLiveData<ApiResult<T>> {
    val resultLiveData = MutableLiveData<ApiResult<T>>()
    val dsl = RequestParameterDsl<T>().apply(requestParameterDslClass).apply {
        this.resultLiveData = resultLiveData
    }
    executeRequestWithResult(dsl)
    return resultLiveData
}

/**
 * 内部方法，执行 RequestParameterDsl 封装的请求逻辑。
 * 根据请求结果自动更新 resultLiveData 并处理 loading 状态。
 */
private fun <T> BaseViewModel.executeRequestWithResult(requestParameterDsl: RequestParameterDsl<T>): Job {
    return viewModelScope.launch(Dispatchers.IO) {
        supervisorScope {
            try {
                // ========== 显示 loading ==========
                if (requestParameterDsl.loadingType != LoadingType.LOADING_NULL) {
                    loadingChange.loading.postValue = LoadingEntity(
                        loadingType = requestParameterDsl.loadingType,
                        loadingMessage = requestParameterDsl.loadingMessage,
                        isShow = true,
                        coroutineScope = this
                    )
                }
                // ========== 在子线程中发起请求 ==========
                val result = requestParameterDsl.onRequest.invoke(this)

                // ========== 成功处理 ==========
                if (requestParameterDsl.loadingType != LoadingType.LOADING_NULL) {
                    loadingChange.loading.postValue = LoadingEntity(
                        loadingType = requestParameterDsl.loadingType,
                        loadingMessage = requestParameterDsl.loadingMessage,
                        isShow = false
                    )
                }
                if (requestParameterDsl.loadingType == LoadingType.LOADING_XML) {
                    loadingChange.showSuccess.postValue = true
                }
                requestParameterDsl.resultLiveData?.postValue = ApiResult.Success(result)

            } catch (e: CancellationException) {
                // 请求被取消（例如关闭弹窗取消请求）
                return@supervisorScope
            } catch (e: Exception) {
                // ========== 统一错误捕获 ==========
                e.printStackTrace()
                "抱歉！出错了----> ${e.message}".logE()
                if (requestParameterDsl.loadingType != LoadingType.LOADING_NULL) {
                    loadingChange.loading.postValue = LoadingEntity(
                        loadingType = requestParameterDsl.loadingType,
                        loadingMessage = requestParameterDsl.loadingMessage,
                        isShow = false
                    )
                }
                val loadStatus = LoadStatusEntity(
                    code = e.code,
                    msg = e.msg,
                    throwable = e,
                    loadingType = requestParameterDsl.loadingType
                )
                if (loadStatus.loadingType == LoadingType.LOADING_XML) {
                    loadingChange.showError.postValue = loadStatus
                }
                //发射错误
                requestParameterDsl.resultLiveData?.postValue = ApiResult.Error(loadStatus)
            }
        }
    }
}


/**
 * 请求参数统一封装类
 */
class RequestParameterDsl<T> {

    /** 请求成功 需要发射的数据 */
    var resultLiveData: MutableLiveData<ApiResult<T>>? = null

    /**
     * 协程请求方法体,执行在子线程中，可执行各种同步或异步操作。注意，这里面是子线程！！！子线程！！！子线程！！！不要在该方法中直接操作UI
     * 或者比如使用liveData.value = xxx，得用 postValue。或者你切换到主线程再操作
     * 在协程作用域（CoroutineScope）中运行，最终返回类型为 T 的结果。
     * 所以在这里面你可以随便去拿数据，不管是本地数据还是网络数据，最后返回最终结果就行了
     * 使用示例：
     * ```
     * onRequest {
     *     val data = getUser()              // 异步请求
     *     processData(data)                 // 同步处理
     *     data                              // 返回最终结果
     * }
     * ```
     */
    private var _onRequest: (suspend CoroutineScope.() -> T)? = null
    var onRequest: suspend CoroutineScope.() -> T
        get() = _onRequest ?: { throw IllegalStateException("onRequest 必须实现哦") }
        set(value) {
            _onRequest = value
        }

    /**执行请求 封装了一下，方便大家好写一点，可以直接 onRequest { }
     *
     * 协程请求方法体,执行在子线程中，可执行各种同步或异步操作。注意，这里面是子线程！！！子线程！！！子线程！！！不要在该方法中直接操作UI或者使用liveData.value = xxx，得用 postValue
     *
     * 在协程作用域（CoroutineScope）中运行，最终返回类型为 T 的结果 给 onSuccess。
     *
     * 所以在这里面你可以随便去拿数据，不管是本地数据还是网络数据，最后返回最终结果就行了
     *
     * 使用示例：
     * ```
     * onRequest {
     *     val data = getUser()              // 异步请求
     *     processData(data)                 // 同步处理
     *     data                              // 返回最终结果
     * }
     * ```
     */
    fun onRequest(block: suspend CoroutineScope.() -> T) {
        _onRequest = block
    }

    /** 目前这个在 loadingType != LOADING_NULL 的时候有效， 可以给不同接口定义不同的 加载提示 内容 */
    var loadingMessage: String = getStringExt(R.string.helper_loading_tip)

    /** 请求时loading类型 默认请求时不显示loading */
    @LoadingType
    var loadingType = LoadingType.LOADING_NULL
}

