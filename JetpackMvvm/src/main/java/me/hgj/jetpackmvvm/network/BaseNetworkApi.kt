package me.hgj.jetpackmvvm.network

import com.google.gson.GsonBuilder
import me.hgj.jetpackmvvm.BuildConfig
import me.hgj.jetpackmvvm.network.interceptor.LogInterceptor
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
        private const val CONNECT_TIME = 10 //连接超时时间 10秒
    }

    fun <T> getApi(serverceClass: Class<T>, baseUrl: String): T {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()
            .create(serverceClass)
    }

    abstract fun setHttpClientBuilder(builder: OkHttpClient.Builder):OkHttpClient.Builder

    /**
     * 配置http
     */
    private val okHttpClient: OkHttpClient
        get(){
            var builder = OkHttpClient.Builder()
            builder = setHttpClientBuilder(builder)
            builder.connectTimeout(CONNECT_TIME.toLong(), TimeUnit.SECONDS)
            //如果是debug模式，添加日志拦截器，打印网络请求日志
            if (BuildConfig.DEBUG) builder.addInterceptor(LogInterceptor())
            return builder.build()
        }



}



