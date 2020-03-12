package me.hgj.jetpackmvvm.demo.data.bean

import me.hgj.jetpackmvvm.demo.app.util.DatetimeUtil
import java.io.Serializable

/**
 * 项目分类
 */
data class TodoResponse(var completeDate: Long,
                        var completeDateStr: String,
                        var content: String,
                        var date: Long,
                        var dateStr: String,
                        var id: Int,
                        var priority: Int,
                        var status: Int,
                        var title: String,
                        var type: Int,
                        var userId: Int) : Serializable {
    fun isDone(): Boolean {
        //判断是否已完成或者已过期
        return if (status == 1) {
            true
        } else {
            date < DatetimeUtil.nows.time
        }
    }
}
