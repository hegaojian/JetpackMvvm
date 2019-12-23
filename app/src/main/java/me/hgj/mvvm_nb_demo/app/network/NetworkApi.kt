package me.hgj.mvvm_nb_demo.app.network

import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.GsonBuilder
import me.hgj.mvvm_nb.network.interceptor.LogInterceptor
import me.hgj.mvvm_nb_demo.App
import me.hgj.mvvm_nb_demo.BuildConfig
import me.hgj.mvvm_nb_demo.api.NetApiService
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　: 网络请求构建器
 */
object NetworkApi {
    fun getApi(baseUrl: String = NetApiService.SERVER_URL): NetApiService {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .client(getOkHttpClient()).build().create(NetApiService::class.java)
    }

    /**
     * 配置http
     */
    private fun getOkHttpClient(): OkHttpClient {
        val cookieJar by lazy {
            PersistentCookieJar(
                SetCookieCache(), SharedPrefsCookiePersistor(
                    App.CONTEXT
                )
            )
        }
        //设置缓存路径
        val httpCacheDirectory = File(App.CONTEXT.cacheDir, "responses")
        val cache = Cache(httpCacheDirectory, 10 * 1024 * 1024)
        return OkHttpClient.Builder().apply {
            cache(cache)
            //添加Cookies自动持久化
            cookieJar(cookieJar)
            //添加缓存拦截器
            addInterceptor(CacheInterceptor())
            //如果是debug模式，添加日志拦截器，打印网络请求日志
            if (BuildConfig.DEBUG) addInterceptor(LogInterceptor())
            //读取超时时间 5秒
            writeTimeout(5, TimeUnit.SECONDS)
            readTimeout(5, TimeUnit.SECONDS)
            //连接超时时间 10秒
            connectTimeout(10, TimeUnit.SECONDS)
        }.build()
    }

}



