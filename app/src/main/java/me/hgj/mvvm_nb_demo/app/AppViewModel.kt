package me.hgj.mvvm_nb_demo.app

import me.hgj.mvvm_nb.BaseViewModel
import me.hgj.mvvm_nb.callback.UnPeekLiveData
import me.hgj.mvvm_nb_demo.App
import me.hgj.mvvm_nb_demo.app.util.CacheUtil
import me.hgj.mvvm_nb_demo.app.util.SettingUtil
import me.hgj.mvvm_nb_demo.data.bean.CollectBus
import me.hgj.mvvm_nb_demo.data.bean.UserInfo

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　:APP全局的Viewmodel，可以存放公共数据，当他数据改变时，所有监听他的地方都会收到回调,也可以做发送消息
 * 比如 全局可使用的 地理位置信息，账户信息，事件通知等，
 */
class AppViewModel : BaseViewModel() {

    //App的账户信息
    var userinfo = UnPeekLiveData<UserInfo>()

    //App主题颜色
    var appColor = UnPeekLiveData<Int>()

    //App 列表动画
    var appAnimation = UnPeekLiveData<Int>()

    //全局收藏，在任意一个地方收藏或取消收藏，监听该值的界面都会收到消息
    var collect = UnPeekLiveData<CollectBus>()

    init {
        //默认值保存的账户信息，没有登陆过则为null
        userinfo.value = CacheUtil.getUser()
        //本来不建议Viewmodel使用Context的，但是这是个App级别的Viewmodel，所以使用applacation上下文没问题
        appColor.value = SettingUtil.getColor(App.CONTEXT)
        //0 关闭动画 1.渐显 2.缩放 3.从下到上 4.从左到右 5.从右到左
        appAnimation.value = 2
    }
}