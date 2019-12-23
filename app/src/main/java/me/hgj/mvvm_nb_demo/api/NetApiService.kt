package me.hgj.mvvm_nb_demo.api

import me.hgj.mvvm_nb_demo.data.UserInfo
import me.hgj.mvvm_nb_demo.data.ApiResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　: 网络API
 */
interface NetApiService {

    companion object{
        const val SERVER_URL = "https://wanandroid.com/"
    }
    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("/user/login")
    suspend fun login(@Field("username") username: String, @Field("password") pwd: String): ApiResponse<UserInfo>

}