package me.hgj.mvvm_nb.network

import android.net.ParseException
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/17
 * 描述　: 根据异常返回相关的错误信息工具类
 */
object ExceptionHandle {

    fun handleException(e: Throwable?): AppException {
        val ex: AppException
        e?.let {
            when (it) {
                is HttpException -> {
                    ex = AppException(ERROR.NETWORD_ERROR)
                    return ex
                }
                is JsonParseException, is JSONException, is ParseException, is MalformedJsonException -> {
                    ex = AppException(ERROR.PARSE_ERROR)
                    return ex
                }
                is ConnectException -> {
                    ex = AppException(ERROR.NETWORD_ERROR)
                    return ex
                }
                is javax.net.ssl.SSLException -> {
                    ex = AppException(ERROR.SSL_ERROR)
                    return ex
                }
                is ConnectTimeoutException -> {
                    ex = AppException(ERROR.TIMEOUT_ERROR)
                    return ex
                }
                is java.net.SocketTimeoutException -> {
                    ex = AppException(ERROR.TIMEOUT_ERROR)
                    return ex
                }
                is java.net.UnknownHostException -> {
                    ex = AppException(ERROR.TIMEOUT_ERROR)
                    return ex
                }
                is AppException -> return it

                else -> {
                    ex = AppException(ERROR.UNKNOWN)
                    return ex
                }
            }
        }
        ex = AppException(ERROR.UNKNOWN)
        return ex
    }
}