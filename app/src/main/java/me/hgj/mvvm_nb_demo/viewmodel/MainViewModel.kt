package me.hgj.mvvm_nb_demo.viewmodel

import me.hgj.mvvm_nb.BaseViewModel
import me.hgj.mvvm_nb.databind.StringObservableField

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/21
 * 描述　:
 */
class MainViewModel:BaseViewModel() {
    var username = StringObservableField("点击我去登录")
}