package me.hgj.mvvm_nb_demo.data
import java.io.Serializable

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　: 账户信息
 */
data class UserInfo(var admin: Boolean,
                    var chapterTops: List<String>,
                    var collectIds: MutableList<String>,
                    var email: String,
                    var icon: String,
                    var id: String,
                    var nickname: String,
                    var password: String,
                    var token: String,
                    var type: Int,
                    var username: String) : Serializable
