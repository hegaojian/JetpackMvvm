package me.hgj.jetpackmvvm.demo.viewmodel.state

import android.app.Application
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.IntLiveData
import me.hgj.jetpackmvvm.callback.livedata.StringLiveData
import me.hgj.jetpackmvvm.demo.app.util.ColorUtil

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/27
 * 描述　: 专门存放 MeFragment 界面数据的ViewModel
 */
class MeViewModel(application: Application) : BaseViewModel(application) {

    var name = StringLiveData("请先登录~")

    var integral = IntLiveData(0)

    var info = StringLiveData("id：--　排名：-")

    var imageUrl = StringLiveData(ColorUtil.randomImage())

}