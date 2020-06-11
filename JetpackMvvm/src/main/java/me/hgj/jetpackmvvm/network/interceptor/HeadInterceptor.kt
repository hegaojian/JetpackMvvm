package me.hgj.jetpackmvvm.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * 头部参数拦截器，传入heads
 */
@Deprecated("已废弃的类，没啥子卵用啊，没必要把参数传进来，多此一举，完全可以直接写在里面，可以参考demo中的MyHeadInterceptor")
class HeadInterceptor(private val headers: Map<String, String>? = null) :Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        if (headers != null && headers.isNotEmpty()) {
            val keys = headers.keys
            for (headerKey in keys) {
                headers[headerKey]?.let {
                    builder.addHeader(headerKey, it).build()
                }
            }
        }
        //请求信息
        return chain.proceed(builder.build())
    }

}