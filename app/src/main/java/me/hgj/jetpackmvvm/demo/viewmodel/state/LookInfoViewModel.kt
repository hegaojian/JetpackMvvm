package me.hgj.jetpackmvvm.demo.viewmodel.state

import android.app.Application
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.databind.StringObservableField

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/4
 * 描述　:
 */
class LookInfoViewModel(application: Application) : BaseViewModel(application){

    var pageNo = 1

    var name: StringObservableField = StringObservableField("--")

    var imageUrl: StringObservableField =
        StringObservableField("https://upload.jianshu.io/users/upload_avatars/9305757/93322613-ff1a-445c-80f9-57f088f7c1b1.jpg?imageMogr2/auto-orient/strip|imageView2/1/w/300/h/300/format/webp")

    var info: StringObservableField = StringObservableField()



}