package me.hgj.jetpackmvvm.demo.data.repository

import me.hgj.jetpackmvvm.network.AppException
import me.hgj.jetpackmvvm.demo.app.network.NetworkApi
import me.hgj.jetpackmvvm.demo.data.ApiResponse
import me.hgj.jetpackmvvm.demo.data.bean.UserInfo

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　: 登录注册的数据仓库
 */
class LoginRepository {

    //登录
    suspend fun login(username: String, password: String): ApiResponse<UserInfo> {
        return NetworkApi.service.login(username, password)
    }

    //注册
    suspend fun register(username: String, password: String): ApiResponse<UserInfo> {
        //因为是在协程里面 所以可以同步的方式写异步
        val registerData =  NetworkApi.service.register(username, password,password)
        //判断注册结果 注册成功，调用登录接口
        if(registerData.isSucces()){
            return login(username,password)
        }else{
            throw AppException(registerData.errorCode,registerData.errorMsg)
        }
    }
}