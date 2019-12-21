package me.hgj.mvvm_nb.state

import me.hgj.mvvm_nb.network.AppException


/**
 * @author :Reginer in  2019/7/8 9:42.
 *         联系方式:QQ:282921012
 *         功能描述:
 */

sealed class ViewState<out T> {
    companion object {
        fun <T> onAppSuccess(data: T): ViewState<T> = Success(data)
        fun <T> onAppLoading(): ViewState<T> = Loading
        fun <T> onAppError(error: AppException): ViewState<T> = Error(error)
    }

    object Loading : ViewState<Nothing>()
    data class Success<out T>(val data: T) : ViewState<T>()
    data class Error(val error: AppException) : ViewState<Nothing>()
}

