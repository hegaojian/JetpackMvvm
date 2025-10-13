package me.hgj.jetpackmvvm.demo.app.core.net.parses

import me.hgj.jetpackmvvm.core.net.AppException
import me.hgj.jetpackmvvm.demo.app.core.net.NetUrl
import me.hgj.jetpackmvvm.demo.app.core.util.UserManager
import me.hgj.jetpackmvvm.demo.data.model.entity.ApiResponse
import me.hgj.jetpackmvvm.ext.util.toast
import okhttp3.Response
import rxhttp.RxHttpPlugins
import rxhttp.wrapper.annotation.Parser
import rxhttp.wrapper.cookie.ICookieJar
import rxhttp.wrapper.parse.TypeParser
import rxhttp.wrapper.utils.convertTo
import java.io.IOException
import java.lang.reflect.Type

/**
 *
 * 作者　: hegaojian
 * 时间　: 2020/11/2
 * 描述　: 这里是根据RxHttp实现的自定义解析器，对请求到的数据做了脱壳处理，该类项目build后RxHttp会自动生成对应的方法，比如
 * toAwaitResponse,toFlowResponse。 其中 @Parser(name = "Response")中的Response 等于 toAwaitXXX 和 toFlowResponse 中的XXX
 */

@Parser(name = "Response")
open class ResponseParser<T> : TypeParser<T> {

    protected constructor() : super()

    constructor(type: Type) : super(type)

    @Throws(IOException::class)
    override fun onParse(response: Response): T {
        val data: ApiResponse<T> = response.convertTo(ApiResponse::class, *types)
        var t = data.data //获取data字段

        if (t == null && types[0] == String::class.java || t == null && types[0] == Any::class.java) {
            t = "" as T
        }
        if (data.errorCode == NetUrl.EXPIRED_CODE) {
            // 登录过期，清除cookie 和本地 用户信息缓存
            UserManager.clearUser()
            val iCookieJar = RxHttpPlugins.getOkHttpClient().cookieJar as ICookieJar
            iCookieJar.removeAllCookie()
            "登录信息已经过期，请重新登录".toast()
            throw AppException(data.errorCode.toString(), "登录信息已经过期，请重新登录")
        }
        // errCode 不等于 SUCCESS_CODE，抛出异常
        if (data.errorCode != NetUrl.SUCCESS_CODE) {
            throw AppException(data.errorCode.toString(), data.errorMsg)
        }
        return t
    }
}