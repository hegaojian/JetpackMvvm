package me.hgj.jetpackmvvm.demo.ui.me

import androidx.lifecycle.MutableLiveData
import me.hgj.jetpackmvvm.BaseViewModel
import me.hgj.jetpackmvvm.databind.IntObservableField
import me.hgj.jetpackmvvm.databind.StringObservableField
import me.hgj.jetpackmvvm.ext.launchRequest
import me.hgj.jetpackmvvm.state.ViewState
import me.hgj.jetpackmvvm.demo.data.bean.IntegralResponse
import me.hgj.jetpackmvvm.demo.data.repository.MeRepository

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/27
 * 描述　:
 */
class MeViewModel : BaseViewModel() {

    private val repository: MeRepository by lazy { MeRepository() }

    var name = StringObservableField("请先登录~")

    var integral = IntObservableField(0)

    var info = StringObservableField("id：--　排名：-")

    var imageUrl = StringObservableField("https://upload.jianshu.io/users/upload_avatars/9305757/93322613-ff1a-445c-80f9-57f088f7c1b1.jpg?imageMogr2/auto-orient/strip|imageView2/1/w/300/h/300/format/webp")

    var meData = MutableLiveData<ViewState<IntegralResponse>>()

    fun getIntegral() {
        launchRequest({ repository.getIntegral() }, meData)
    }
}