package me.hgj.jetpackmvvm.ext.util

import androidx.lifecycle.MutableLiveData
import me.hgj.jetpackmvvm.network.AppException
import me.hgj.jetpackmvvm.network.BaseResponse
import me.hgj.jetpackmvvm.network.ExceptionHandle
import me.hgj.jetpackmvvm.state.ViewState

/**
 * 处理返回值
 * @param result 请求结果
 */
fun <T> MutableLiveData<ViewState<T>>.paresResult(result: BaseResponse<T>) {
    value = if (result.isSucces()) ViewState.onAppSuccess(result.data) else
        ViewState.onAppError(AppException(result.errorCode, result.errorMsg))
}

/**
 * 不处理返回值 直接返回请求结果
 * @param result 请求结果
 */
fun <T> MutableLiveData<ViewState<T>>.paresResult(result: T) {
    value = ViewState.onAppSuccess(result)
}

/**
 * 异常转换异常处理
 */
fun <T> MutableLiveData<ViewState<T>>.paresException(e: Throwable) {
    this.value = ViewState.onAppError(ExceptionHandle.handleException(e))
}