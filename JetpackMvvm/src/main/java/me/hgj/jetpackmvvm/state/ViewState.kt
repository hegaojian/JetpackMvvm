package me.hgj.jetpackmvvm.state
import me.hgj.jetpackmvvm.network.AppException

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/17
 * 描述　: 自定义请求状态类
 */
sealed class ViewState<out T> {
    companion object {
        fun <T> onAppSuccess(data: T): ViewState<T> = Success(data)
        fun <T> onAppLoading(loadingMessage:String): ViewState<T> = Loading(loadingMessage)
        fun <T> onAppError(error: AppException): ViewState<T> = Error(error)
    }

    data class Loading(val loadingMessage:String) : ViewState<Nothing>()
    data class Success<out T>(val data: T) : ViewState<T>()
    data class Error(val error: AppException) : ViewState<Nothing>()
}

