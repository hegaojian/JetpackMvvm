package me.hgj.jetpackmvvm.demo.app.event

import android.app.Application
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.UnPeekLiveData
import me.hgj.jetpackmvvm.callback.livedata.UnPeekNotNullLiveData
import me.hgj.jetpackmvvm.demo.app.util.CacheUtil
import me.hgj.jetpackmvvm.demo.app.util.SettingUtil
import me.hgj.jetpackmvvm.demo.data.model.bean.UserInfo

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　:APP全局的Viewmodel，可以存放公共数据，当他数据改变时，所有监听他的地方都会收到回调,也可以做发送消息
 * 比如 全局可使用的 地理位置信息，账户信息,App的基本配置等等，
 */
class AppViewModel(app: Application) : BaseViewModel(app) {

    //App的账户信息 使用UnPeekLiveData 因为账户信息有可能为空
    var userinfo = UnPeekLiveData<UserInfo>()

    //App主题颜色 中大型项目不推荐以这种方式改变主题颜色，比较繁琐耦合，且容易有遗漏某些控件没有设置主题色
    var appColor = UnPeekNotNullLiveData<Int>()

    //App 列表动画
    var appAnimation = UnPeekNotNullLiveData<Int>()

    init {
        //默认值保存的账户信息，没有登陆过则为null
        userinfo.value = CacheUtil.getUser()
        //默认值颜色
        appColor.value = SettingUtil.getColor(getApplication())
        //初始化列表动画
        appAnimation.value = SettingUtil.getListMode()
    }
}