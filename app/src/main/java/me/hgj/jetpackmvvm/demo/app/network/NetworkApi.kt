package me.hgj.jetpackmvvm.demo.app.network

import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import me.hgj.jetpackmvvm.demo.App
import me.hgj.jetpackmvvm.demo.app.NetApiService
import me.hgj.jetpackmvvm.network.BaseNetworkApi
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　: 自己项目中的网络请求构建器，继承BasenetworkApi 并实现setHttpClientBuilder方法，
 * 在这里可以添加拦截器，可以对Builder做任意操作
 */
object NetworkApi:BaseNetworkApi(){

    //封装NetApiService变量 方便直接快速调用
    val service:NetApiService by lazy {
        getApi(NetApiService::class.java,NetApiService.SERVER_URL)
    }

    //Cookies自动持久化 调用 clear 可清空Cookies
     val cookieJar: PersistentCookieJar by lazy {
        PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(App.CONTEXT))
    }

    //缓存信息配置
    private val cache: Cache by lazy {
        //设置缓存路径
        val httpCacheDirectory = File(App.CONTEXT.cacheDir, "http_response")
        Cache(httpCacheDirectory, 10 * 1024 * 1024)
    }

    /**
     * 实现重写父类的setHttpClientBuilder方法，
     * 在这里可以添加拦截器，可以对Builder做任意操作
     */
    override fun setHttpClientBuilder(builder: OkHttpClient.Builder):OkHttpClient.Builder{
        builder.apply {
            //设置缓存配置
            cache(cache)
            //添加Cookies自动持久化
            cookieJar(cookieJar)
            //添加缓存拦截器
            addInterceptor(CacheInterceptor())
        }
        return builder
    }

}



