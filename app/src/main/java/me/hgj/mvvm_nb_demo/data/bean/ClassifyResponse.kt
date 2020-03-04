package me.hgj.mvvm_nb_demo.data.bean

import java.io.Serializable

/**
 * 项目分类
 */
data class ClassifyResponse(var children: List<Any> = listOf(),
                            var courseId: Int = 0,
                            var id: Int = 0,
                            var name: String = "",
                            var order: Int = 0,
                            var parentChapterId: Int = 0,
                            var userControlSetTop: Boolean = false,
                            var visible: Int = 0) : Serializable
