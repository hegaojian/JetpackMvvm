package me.hgj.mvvm_nb_demo.viewmodel

import androidx.lifecycle.MutableLiveData
import me.hgj.mvvm_nb.BaseViewModel
import me.hgj.mvvm_nb.databind.StringObservableField
import me.hgj.mvvm_nb.ext.launchRequest
import me.hgj.mvvm_nb.state.ViewState
import me.hgj.mvvm_nb_demo.data.UserInfo
import me.hgj.mvvm_nb_demo.repository.LoginRepository

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　:登录的Viewmodel
 */
class LoginViewModel:BaseViewModel() {

    var username = StringObservableField()

    var password = StringObservableField()

    private val loginRpository:LoginRepository by lazy{ LoginRepository() }

    var loginResult = MutableLiveData<ViewState<UserInfo>>()

    fun login(){
        launchRequest({loginRpository.login(username.get(),password.get())}//请求体
            ,loginResult,//请求的返回结果，请求成功与否都会改变该值，在Activity或fragment中监听回调结果，具体可看loginActivity中的回调
            true,//是否显示等待框，，默认false不显示
            "正在登录中...")//等待框内容，默认可不填，默认：请求网络中...

    }

}