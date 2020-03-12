package me.hgj.jetpackmvvm.demo.data.bean

import me.hgj.jetpackmvvm.demo.data.bean.ClassifyResponse
import java.io.Serializable

/**
 *  体系数据
  * @Author:         hegaojian
  * @CreateDate:     2019/8/21 15:46
 */
data class SystemResponse(var children: ArrayList<ClassifyResponse>,
                          var courseId: Int,
                          var id: Int,
                          var name: String,
                          var order: Int,
                          var parentChapterId: Int,
                          var userControlSetTop: Boolean,
                          var visible: Int) : Serializable
