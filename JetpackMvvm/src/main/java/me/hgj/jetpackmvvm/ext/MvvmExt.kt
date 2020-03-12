package me.hgj.jetpackmvvm.ext

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.hgj.jetpackmvvm.*
import me.hgj.jetpackmvvm.ext.util.paresException
import me.hgj.jetpackmvvm.ext.util.paresResult
import me.hgj.jetpackmvvm.network.AppException
import me.hgj.jetpackmvvm.network.BaseResponse
import me.hgj.jetpackmvvm.state.ViewState
import java.lang.reflect.ParameterizedType

/**
 * 获取vm clazz
 */
@Suppress("UNCHECKED_CAST")
fun <VM> getVmClazz(obj: Any): VM {
    return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as VM
}

fun <VM : BaseViewModel> AppCompatActivity.getViewmodel():VM{
    return ViewModelProvider(this).get(getVmClazz(this) as Class<VM>)
}
/**
 * 显示页面状态，这里有个技巧，成功回调在第一个，其后两个带默认值的回调可省
 * @param viewState 接口返回值
 * @param onLoading 加载中
 * @param onSuccess 成功回调
 * @param onError 失败回调
 *
 */
fun <T> BaseVmDbActivity<*, *>.parseState(
    viewState: ViewState<T>,
    onSuccess: (T) -> Unit,
    onError: ((AppException) -> Unit)? = null,
    onLoading: (() -> Unit)? = null
) {
    when (viewState) {
        is ViewState.Loading -> {
            showLoading(viewState.loadingMessage)
            onLoading?.run { this }
        }
        is ViewState.Success -> {
            dismissLoading()
            onSuccess(viewState.data)
        }
        is ViewState.Error -> {
            dismissLoading()
            onError?.run { this(viewState.error) }
        }
    }
}
/**
 * 显示页面状态，这里有个技巧，成功回调在第一个，其后两个带默认值的回调可省
 * @param viewState 接口返回值
 * @param onLoading 加载中
 * @param onSuccess 成功回调
 * @param onError 失败回调
 *
 */
fun <T> BaseVmActivity<*>.parseState(
    viewState: ViewState<T>,
    onSuccess: (T) -> Unit,
    onError: ((AppException) -> Unit)? = null,
    onLoading: (() -> Unit)? = null
) {
    when (viewState) {
        is ViewState.Loading -> {
            showLoading(viewState.loadingMessage)
            onLoading?.run { this }
        }
        is ViewState.Success -> {
            dismissLoading()
            onSuccess(viewState.data)
        }
        is ViewState.Error -> {
            dismissLoading()
            onError?.run { this(viewState.error) }
        }
    }
}

/**
 * 显示页面状态，这里有个技巧，成功回调在第一个，其后两个带默认值的回调可省
 * @param viewState 接口返回值
 * @param onLoading 加载中
 * @param onSuccess 成功回调
 * @param onError 失败回调
 * @param loadingMessage 加载框的提示内容，默认 请求网络中...
 *
 */
fun <T> BaseVmFragment<*>.parseState(
    viewState: ViewState<T>,
    onSuccess: (T) -> Unit,
    onError: ((AppException) -> Unit)? = null,
    onLoading: (() -> Unit)? = null
) {
    when (viewState) {
        is ViewState.Loading -> {
            showLoading(viewState.loadingMessage)
            onLoading?.run { this }
        }
        is ViewState.Success -> {
            dismissLoading()
            onSuccess(viewState.data)
        }
        is ViewState.Error -> {
            dismissLoading()
            onError?.run { this(viewState.error) }
        }
    }
}
/**
 * 显示页面状态，这里有个技巧，成功回调在第一个，其后两个带默认值的回调可省
 * @param viewState 接口返回值
 * @param onLoading 加载中
 * @param onSuccess 成功回调
 * @param onError 失败回调
 *
 */
fun <T> BaseVmDbFragment<*, *>.parseState(
    viewState: ViewState<T>,
    onSuccess: (T) -> Unit,
    onError: ((AppException) -> Unit)? = null,
    onLoading: (() -> Unit)? = null
) {
    when (viewState) {
        is ViewState.Loading -> {
            showLoading(viewState.loadingMessage)
            onLoading?.run { this }
        }
        is ViewState.Success -> {
            dismissLoading()
            onSuccess(viewState.data)
        }
        is ViewState.Error -> {
            dismissLoading()
            onError?.run { this(viewState.error) }
        }
    }
}
/**
 *
 * net request 会校验请求结果数据是否是成功
 * @param request request method
 * @param viewState request result
 * @param showLoading 配置是否显示等待框
 */
fun <T> BaseViewModel.launchRequest(
    request: suspend () -> BaseResponse<T>,
    viewState: MutableLiveData<ViewState<T>>,
    showLoading: Boolean = false,
    loadingMessage: String="请求网络中..."
) {
    viewModelScope.launch {
        runCatching {
            if (showLoading) viewState.value = ViewState.onAppLoading(loadingMessage)
            withContext(Dispatchers.IO) { request() }
        }.onSuccess {
            viewState.paresResult(it)
        }.onFailure {
            Log.i("throwable",it.message)
            viewState.paresException(it)
        }
    }
}

/**
 * net request 不校验请求结果数据是否是成功
 * @param request request method
 * @param viewState request result
 * @param showLoading 配置是否显示等待框
 */
fun <T> BaseViewModel.launchRequestNoCheck(
    request: suspend () -> T,
    viewState: MutableLiveData<ViewState<T>>,
    showLoading: Boolean = false,
    loadingMessage: String="请求网络中..."
) {
    viewModelScope.launch {
        runCatching {
            if (showLoading) viewState.value = ViewState.onAppLoading(loadingMessage)
            withContext(Dispatchers.IO) { request() }
        }.onSuccess {
            viewState.paresResult(it)
        }.onFailure {
            viewState.paresException(it)
        }
    }
}




