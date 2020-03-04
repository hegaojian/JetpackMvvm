package me.hgj.mvvm_nb.ext.util

import androidx.lifecycle.MutableLiveData
import me.hgj.mvvm_nb.network.AppException
import me.hgj.mvvm_nb.network.BaseResponse
import me.hgj.mvvm_nb.network.ExceptionHandle
import me.hgj.mvvm_nb.state.ViewState

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