package me.hgj.jetpackmvvm.demo.data.repository.request

import me.hgj.jetpackmvvm.demo.app.core.net.NetUrl
import me.hgj.jetpackmvvm.demo.data.model.entity.UserInfo
import rxhttp.wrapper.coroutines.Await
import rxhttp.wrapper.param.RxHttp
import rxhttp.wrapper.param.toAwaitResponse

/**
 * 作者　: hegaojian
 * 时间　: 2020/11/2
 * 描述　: 用户数据
 */
object UserRepository {

    /**
     * 登录
     */
    fun login(userName: String, password: String): Await<UserInfo> {
        return RxHttp.postForm(NetUrl.User.LOGIN)
            .add("username", userName)
            .add("password", password)
            .toAwaitResponse()
    }

    /**
     * 登录
     */
    fun register(userName: String, password: String): Await<Any> {
        return RxHttp.postForm(NetUrl.User.REGISTER)
            .add("username", userName)
            .add("password", password)
            .add("repassword", password)
            .toAwaitResponse()
    }

}

