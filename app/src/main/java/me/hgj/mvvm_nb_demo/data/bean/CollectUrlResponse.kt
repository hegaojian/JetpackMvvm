package me.hgj.mvvm_nb_demo.data.bean

import java.io.Serializable

/**
 * 收藏的网址类
 * @Author:         hegaojian
 * @CreateDate:     2019/8/31 10:36
 */
data class CollectUrlResponse(var icon: String,
                              var id: Int,
                              var link: String,
                              var name: String,
                              var order: Int,
                              var userId: Int,
                              var visible: Int) : Serializable

