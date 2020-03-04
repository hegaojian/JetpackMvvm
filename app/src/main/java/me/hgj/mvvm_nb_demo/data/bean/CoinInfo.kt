package me.hgj.mvvm_nb_demo.data.bean

import java.io.Serializable

/**
 * 分享人信息
 */
data class CoinInfo(var coinCount: Int, var rank:Int, var userId: Int,var username:String) : Serializable
