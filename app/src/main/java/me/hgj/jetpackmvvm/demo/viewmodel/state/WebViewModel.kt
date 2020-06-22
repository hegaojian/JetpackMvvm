package me.hgj.jetpackmvvm.demo.viewmodel.state

import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/3
 * 描述　:
 */
class WebViewModel : BaseViewModel() {

    //是否收藏
    var collect = false

    //收藏的Id
    var ariticleId = 0

    //标题
    var showTitle: String = ""

    //文章的网络访问路径
    var url: String = ""

    //需要收藏的类型 具体参数说明请看 CollectType 枚举类
    var collectType = 0
}