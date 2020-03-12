package me.hgj.jetpackmvvm.demo.ui

import androidx.lifecycle.MutableLiveData
import me.hgj.jetpackmvvm.BaseViewModel

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/11
 * 描述　: 主页的消息通知类
 */
class MessageViewmodel:BaseViewModel(){
    //分享文章通知
    var shareArticle = MutableLiveData<Boolean>(false)
    //添加toto通知
    var addTodo = MutableLiveData<Boolean>(false)
}