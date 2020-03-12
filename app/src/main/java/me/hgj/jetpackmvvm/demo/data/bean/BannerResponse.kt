package me.hgj.jetpackmvvm.demo.data.bean

import java.io.Serializable

/**
 * 轮播图
 */
data class BannerResponse(
    var desc: String = "",
    var id: Int = 0,
    var imagePath: String = "",
    var isVisible: Int = 0,
    var order: Int = 0,
    var title: String = "",
    var type: Int = 0,
    var url: String = ""
) : Serializable


