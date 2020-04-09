package me.hgj.jetpackmvvm.ext

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import me.hgj.jetpackmvvm.*
import me.hgj.jetpackmvvm.ext.util.logd
import me.hgj.jetpackmvvm.ext.util.loge
import me.hgj.jetpackmvvm.ext.util.toJson
import me.hgj.jetpackmvvm.network.AppException
import me.hgj.jetpackmvvm.network.BaseResponse
import me.hgj.jetpackmvvm.network.ExceptionHandle
import me.hgj.jetpackmvvm.state.ResultState
import me.hgj.jetpackmvvm.state.paresException
import me.hgj.jetpackmvvm.state.paresResult

/**
 * 作者　: hegaojian
 * 时间　: 2020/4/8
 * 描述　:BaseViewModel请求协程封装
 */

/**
 * 显示页面状态，这里有个技巧，成功回调在第一个，其后两个带默认值的回调可省
 * @param resultState 接口返回值
 * @param onLoading 加载中
 * @param onSuccess 成功回调
 * @param onError 失败回调
 *
 */
fun <T> BaseVmDbActivity<*, *>.parseState(
    resultState: ResultState<T>,
    onSuccess: (T) -> Unit,
    onError: ((AppException) -> Unit)? = null,
    onLoading: (() -> Unit)? = null
) {
    when (resultState) {
        is ResultState.Loading -> {
            showLoading(resultState.loadingMessage)
            onLoading?.run { this }
        }
        is ResultState.Success -> {
            dismissLoading()
            onSuccess(resultState.data)
        }
        is ResultState.Error -> {
            dismissLoading()
            onError?.run { this(resultState.error) }
        }
    }
}
/**
 * 显示页面状态，这里有个技巧，成功回调在第一个，其后两个带默认值的回调可省
 * @param resultState 接口返回值
 * @param onLoading 加载中
 * @param onSuccess 成功回调
 * @param onError 失败回调
 *
 */
fun <T> BaseVmActivity<*>.parseState(
    resultState: ResultState<T>,
    onSuccess: (T) -> Unit,
    onError: ((AppException) -> Unit)? = null,
    onLoading: (() -> Unit)? = null
) {
    when (resultState) {
        is ResultState.Loading -> {
            showLoading(resultState.loadingMessage)
            onLoading?.run { this }
        }
        is ResultState.Success -> {
            dismissLoading()
            onSuccess(resultState.data)
        }
        is ResultState.Error -> {
            dismissLoading()
            onError?.run { this(resultState.error) }
        }
    }
}

/**
 * 显示页面状态，这里有个技巧，成功回调在第一个，其后两个带默认值的回调可省
 * @param resultState 接口返回值
 * @param onLoading 加载中
 * @param onSuccess 成功回调
 * @param onError 失败回调
 *
 */
fun <T> BaseVmFragment<*>.parseState(
    resultState: ResultState<T>,
    onSuccess: (T) -> Unit,
    onError: ((AppException) -> Unit)? = null,
    onLoading: (() -> Unit)? = null
) {
    when (resultState) {
        is ResultState.Loading -> {
            showLoading(resultState.loadingMessage)
            onLoading?.run { this }
        }
        is ResultState.Success -> {
            dismissLoading()
            onSuccess(resultState.data)
        }
        is ResultState.Error -> {
            dismissLoading()
            onError?.run { this(resultState.error) }
        }
    }
}

/**
 * 显示页面状态，这里有个技巧，成功回调在第一个，其后两个带默认值的回调可省
 * @param resultState 接口返回值
 * @param onLoading 加载中
 * @param onSuccess 成功回调
 * @param onError 失败回调
 *
 */
fun <T> BaseVmDbFragment<*, *>.parseState(
    resultState: ResultState<T>,
    onSuccess: (T) -> Unit,
    onError: ((AppException) -> Unit)? = null,
    onLoading: (() -> Unit)? = null
) {
    when (resultState) {
        is ResultState.Loading -> {
            showLoading(resultState.loadingMessage)
            onLoading?.run { this }
        }
        is ResultState.Success -> {
            dismissLoading()
            onSuccess(resultState.data)
        }
        is ResultState.Error -> {
            dismissLoading()
            onError?.run { this(resultState.error) }
        }
    }
}
/**
 * net request 不校验请求结果数据是否是成功
 * @param block 请求体方法0
 * @param resultState 请求回调的ResultState数据
 * @param isShowDialog 是否显示加载框
 * @param loadingMessage 加载框提示内容
 */
fun <T> BaseViewModel.request(
    block: suspend () -> BaseResponse<T>,
    resultState: MutableLiveData<ResultState<T>>,
    isShowDialog: Boolean = false,
    loadingMessage: String="请求网络中..."
) {
    viewModelScope.launch {
        runCatching {
            if (isShowDialog) resultState.value = ResultState.onAppLoading(loadingMessage)
            withContext(Dispatchers.IO) { block() }
        }.onSuccess {
            it.toJson().logd("hgj")
            resultState.paresResult(it)
        }.onFailure {
            it.message?.loge("JetpackMvvm")
            resultState.paresException(it)
        }
    }
}

/**
 * net request 不校验请求结果数据是否是成功
 * @param block 请求体方法
 * @param resultState 请求回调的ResultState数据
 * @param isShowDialog 是否显示加载框
 * @param loadingMessage 加载框提示内容
 */
fun <T> BaseViewModel.requestNoCheck(
    block: suspend () -> T,
    resultState: MutableLiveData<ResultState<T>>,
    isShowDialog: Boolean = false,
    loadingMessage: String="请求网络中..."
) {
    viewModelScope.launch {
        runCatching {
            if (isShowDialog) resultState.value = ResultState.onAppLoading(loadingMessage)
            withContext(Dispatchers.IO) { block() }
        }.onSuccess {
            resultState.paresResult(it)
        }.onFailure {
            it.message?.loge("JetpackMvvm")
            resultState.paresException(it)
        }
    }
}

/**
 * 过滤服务器结果，失败抛异常
 * @param block 请求体方法，必须要用suspend关键字修饰
 * @param success 成功回调
 * @param error 失败回调 可不传
 * @param isShowDialog 是否显示加载框
 * @param loadingMessage 加载框提示内容
 */
fun <T> BaseViewModel.request(
    block: suspend () -> BaseResponse<T>,
    success: (T) -> Unit,
    error: (AppException) -> Unit = {},
    isShowDialog: Boolean = false,
    loadingMessage: String = "请求网络中..."
) {
    //如果需要弹窗 通知Activity/fragment弹窗
    if (isShowDialog) uiChange.showDialog.postValue(loadingMessage)
    viewModelScope.launch {
        runCatching {
            //请求代码块调度在Io线程中
            withContext(Dispatchers.IO) { block() }
        }.onSuccess {
            //网络请求成功 关闭弹窗
            uiChange.dismissDialog.call()
            try {
                //因为要判断请求的数据结果是否成功，失败会抛出自定义异常，所以在这里try一下
                executeResponse(it) { tIt -> success(tIt) }
            }catch (e:Exception){
                //打印错误消息
                e.message?.loge("JetpackMvvm")
                //失败回调
                error(ExceptionHandle.handleException(e))
            }
        }.onFailure {
            //网络请求异常 关闭弹窗
            uiChange.dismissDialog.call()
            //打印错误消息
            it.message?.loge("JetpackMvvm")
            //失败回调
            error(ExceptionHandle.handleException(it))
        }
    }
}

/**
 *  不过滤请求结果
 * @param block 请求体 必须要用suspend关键字修饰
 * @param success 成功回调
 * @param error 失败回调 可不给
 * @param isShowDialog 是否显示加载框
 * @param loadingMessage 加载框提示内容
 */
fun <T> BaseViewModel.requestNoCheck(
    block: suspend () -> T,
    success: (T) -> Unit,
    error: (AppException) -> Unit = {},
    isShowDialog: Boolean = false,
    loadingMessage: String = "请求网络中..."
) {
    //如果需要弹窗 通知Activity/fragment弹窗
    if (isShowDialog) uiChange.showDialog.postValue(loadingMessage)
    viewModelScope.launch {
        runCatching {
            //请求时调度在Io线程中
            withContext(Dispatchers.IO) { block() }
        }.onSuccess {
            //网络请求成功 关闭弹窗
            uiChange.dismissDialog.call()
            //成功回调
            success(it)
        }.onFailure {
            //网络请求异常 关闭弹窗
            uiChange.dismissDialog.call()
            //打印错误消息
            it.message?.loge("JetpackMvvm")
            //失败回调
            error(ExceptionHandle.handleException(it))
        }
    }
}

/**
 * 请求结果过滤，判断请求服务器请求结果是否成功，不成功则会抛出异常
 */
suspend fun <T> BaseViewModel.executeResponse(response: BaseResponse<T>,success: suspend CoroutineScope.(T) -> Unit) {
    coroutineScope {
        if (response.isSucces()) success(response.getResponseData())
        else throw AppException(response.getResponseCode(), response.getResponseMsg(), response.getResponseMsg())
    }
}