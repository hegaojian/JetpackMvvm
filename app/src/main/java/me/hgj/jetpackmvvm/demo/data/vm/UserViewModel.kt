package me.hgj.jetpackmvvm.demo.data.vm

import me.hgj.jetpackmvvm.base.vm.BaseViewModel
import me.hgj.jetpackmvvm.core.data.request
import me.hgj.jetpackmvvm.core.net.LoadingType
import me.hgj.jetpackmvvm.demo.data.repository.request.UserRepository

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　:
 */
class UserViewModel : BaseViewModel() {

    fun login(username: String, password: String)  = request {
        onRequest{
            UserRepository.login(username, password).await()
        }
        loadingType = LoadingType.LOADING_DIALOG
        loadingMessage = "正在登录中..."
    }

    fun register(username: String, password: String)  = request {
        onRequest{
            //先注册，注册成功后调用登录，登录成功后返回用户信息 。 协程写起来真的太舒服
            UserRepository.register(username, password).await()
            UserRepository.login(username, password).await()
        }
        loadingType = LoadingType.LOADING_DIALOG
        loadingMessage = "正在注册中..."
    }

}