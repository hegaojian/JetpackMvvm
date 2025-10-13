package me.hgj.jetpackmvvm.demo.app.core.net

import me.hgj.jetpackmvvm.core.appContext
import me.hgj.jetpackmvvm.core.net.interception.LogInterceptor
import okhttp3.OkHttpClient
import rxhttp.RxHttpPlugins
import rxhttp.wrapper.cookie.CookieStore
import rxhttp.wrapper.ssl.HttpsUtils
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * 作者　：hegaojian
 * 时间　：2025/9/29
 * 说明　：rxHttp框架初始化 示例
 */
object RxHttpInit {
    fun init(){
        RxHttpPlugins.init(getDefaultOkHttpClient().build())
    }

    private fun getDefaultOkHttpClient():  OkHttpClient.Builder {
        //在这里面可以写你想要的配置 太多了，我就简单的写了一点，具体可以看rxHttp的文档，有很多
        val sslParams = HttpsUtils.getSslSocketFactory()
        return OkHttpClient.Builder()
            //使用CookieStore对象磁盘缓存,自动管理cookie 玩安卓自动登录验证
            .cookieJar(CookieStore(File(appContext.externalCacheDir, "RxHttpCookie")))
            .connectTimeout(15, TimeUnit.SECONDS)//读取连接超时时间 15秒
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(LogInterceptor())//添加Log拦截器
            .sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager) //添加信任证书
            .hostnameVerifier { hostname, session -> true } //忽略host验证
    }
}