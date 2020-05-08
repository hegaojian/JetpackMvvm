package me.hgj.jetpackmvvm.demo.app.event

import android.app.Application
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.UnPeekLiveData
import me.hgj.jetpackmvvm.demo.data.model.bean.CollectBus

/**
 * 作者　: hegaojian
 * 时间　: 2019/5/2
 * 描述　:APP全局的Viewmodel，可以在这里发送全局通知替代Eventbus，LiveDataBus等
 */
class EventViewModel(app: Application) : BaseViewModel(app) {

    //全局收藏，在任意一个地方收藏或取消收藏，监听该值的界面都会收到消息
    var collect =
        UnPeekLiveData<CollectBus>()

    //分享文章通知
    var shareArticle =
        UnPeekLiveData<Boolean>()

    //添加toto通知
    var addTodo = UnPeekLiveData<Boolean>()

}