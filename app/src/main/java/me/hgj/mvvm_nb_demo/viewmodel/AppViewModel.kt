package me.hgj.mvvm_nb_demo.viewmodel

import me.hgj.mvvm_nb.BaseViewModel
import me.hgj.mvvm_nb.callback.UnPeekLiveData
import me.hgj.mvvm_nb_demo.data.UserInfo

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　:APP全局的Viewmodel，可以存放公共数据，当他数据改变时，所有监听他的地方都会收到回调
 */
class AppViewModel:BaseViewModel() {
    //用户信息
    var userinfo = UnPeekLiveData<UserInfo>()

}