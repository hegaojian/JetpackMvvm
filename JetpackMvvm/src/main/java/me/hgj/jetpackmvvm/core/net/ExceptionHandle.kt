package me.hgj.jetpackmvvm.core.net

import android.net.ParseException
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import kotlinx.coroutines.TimeoutCancellationException
import org.json.JSONException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.net.ssl.SSLException

/**
 * 作者　：hegaojian
 * 时间　：2025/9/24
 * 说明　：
 */
object ExceptionHandle {

    fun handleException(e: Throwable?): AppException {
        val ex: AppException
        e?.let {
            when (it) {
                is JsonParseException, is JSONException, is ParseException, is MalformedJsonException  -> {
                    ex = AppException(Error.PARSE_ERROR,e)
                    return ex
                }
                is ConnectException -> {
                    ex = AppException(Error.NETWORK_ERROR,e)
                    return ex
                }
                is SSLException -> {
                    ex = AppException(Error.SSL_ERROR,e)
                    return ex
                }
                is  SocketTimeoutException,
                is TimeoutException,
                is TimeoutCancellationException -> {
                    ex = AppException(Error.TIMEOUT_ERROR,e)
                    return ex
                }
                is UnknownHostException -> {
                    ex = AppException(Error.TIMEOUT_ERROR,e)
                    return ex
                }
                is AppException -> return it

                else -> {
                    ex = AppException(Error.UNKNOWN,e)
                    return ex
                }
            }
        }
        ex = AppException(Error.UNKNOWN,e)
        return ex
    }
}