package me.hgj.jetpackmvvm.network.interceptor

import com.orhanobut.logger.Logger
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset
import java.nio.charset.UnsupportedCharsetException

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/12
 * 描述　: 打印Log的拦截器
 */

class LogInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val utf8 = Charset.forName("UTF-8")
        // 打印请求报文
        val request = chain.request()
        val requestBody = request.body()
        var reqBody: String? = null
        if (requestBody != null) {
            val buffer = Buffer()
            requestBody.writeTo(buffer)
            var charset: Charset? = utf8
            val contentType = requestBody.contentType()
            if (contentType != null) {
                charset = contentType.charset(utf8)
            }
            charset?.let {
                reqBody = buffer.readString(it)
            }
        }
        Logger.i(String.format(
            "发起请求\nmethod：%s\nurl：%s\nheaders: %s\nbody：%s",
            request.method(), request.url(), request.headers(), reqBody
        ))
        // 打印返回报文
        // 先执行请求，才能够获取报文
        val response = chain.proceed(request)
        val responseBody = response.body()
        var respBody: String? = null
        if (responseBody != null) {
            val source = responseBody.source()
            source.request(java.lang.Long.MAX_VALUE)
            val buffer = source.buffer()

            var charset: Charset? = utf8
            val contentType = responseBody.contentType()
            if (contentType != null) {
                try {
                    charset = contentType.charset(utf8)
                } catch (e: UnsupportedCharsetException) {
                    e.printStackTrace()
                }
            }
            respBody = buffer.clone().readString(charset!!)
        }

        Logger.i(String.format(
            "请求响应\n%s %s\n请求url：%s\n请求body：%s\n响应body：%s",
            response.code(), response.message(), response.request().url(), reqBody, respBody
        ))

        return response
    }

}
