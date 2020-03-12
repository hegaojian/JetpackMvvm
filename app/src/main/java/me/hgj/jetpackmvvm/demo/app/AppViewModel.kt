package me.hgj.jetpackmvvm.demo.app

import me.hgj.jetpackmvvm.BaseViewModel
import me.hgj.jetpackmvvm.callback.UnPeekLiveData
import me.hgj.jetpackmvvm.demo.App
import me.hgj.jetpackmvvm.demo.app.util.CacheUtil
import me.hgj.jetpackmvvm.demo.app.util.SettingUtil
import me.hgj.jetpackmvvm.demo.data.bean.CollectBus
import me.hgj.jetpackmvvm.demo.data.bean.UserInfo

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　:APP全局的Viewmodel，可以存放公共数据，当他数据改变时，所有监听他的地方都会收到回调,也可以做发送消息
 * 比如 全局可使用的 地理位置信息，账户信息，事件通知等，
 */
class AppViewModel : BaseViewModel() {

    //App的账户信息
    var userinfo = UnPeekLiveData<UserInfo>()

    //App主题颜色 大型项目不推荐以这种方式改变主题颜色，比较繁琐，且容易有遗漏某些地方没有设置主题
    var appColor = UnPeekLiveData<Int>()

    //App 列表动画
    var appAnimation = UnPeekLiveData<Int>()

    //全局收藏，在任意一个地方收藏或取消收藏，监听该值的界面都会收到消息
    var collect = UnPeekLiveData<CollectBus>()

    var refreshUi = UnPeekLiveData<String>()

    init {
        //默认值保存的账户信息，没有登陆过则为null
        userinfo.value = CacheUtil.getUser()
        //不建议Viewmodel使用Context，但是这是个App级别的Viewmodel，所以使用applacation上下文没事
        appColor.value = SettingUtil.getColor(App.CONTEXT)
        //初始化列表动画
        appAnimation.value = SettingUtil.getListMode()
    }
}