package me.hgj.mvvm_nb.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　: 网络请求构建器基类
 */
abstract class BaseNetworkApi {
    companion object {
        private const val CONNECT_TIME = 5 //连接超时时间 5秒
    }

    fun <T> getApi(serverceClass: Class<T>, baseUrl: String): T {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(okHttpClient)
            .build()
            .create(serverceClass)
    }

    abstract fun setHttpClientBuilder(builder: OkHttpClient.Builder)

    /**
     * 配置http
     */
    private val okHttpClient: OkHttpClient
        get(){
            val builder = OkHttpClient.Builder()
            builder.connectTimeout(CONNECT_TIME.toLong(),TimeUnit.SECONDS)
            setHttpClientBuilder(builder)
            return builder.build()
        }



}



