package me.hgj.mvvm_nb_demo.repository

import me.hgj.mvvm_nb_demo.app.network.NetworkApi
import me.hgj.mvvm_nb_demo.data.ApiResponse
import me.hgj.mvvm_nb_demo.data.UserInfo

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　: 登录的数据仓库
 */
class LoginRepository {

    suspend fun login(username: String, password: String): ApiResponse<UserInfo> {
        return NetworkApi.getApi().login(username, password)
    }

}