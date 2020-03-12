package me.hgj.jetpackmvvm.demo.data.bean

import java.io.Serializable

/**
 * 搜索热词
 */
data class SearchResponse(var id: Int,
                          var link: String,
                          var name: String,
                          var order: Int,
                          var visible: Int) : Serializable
