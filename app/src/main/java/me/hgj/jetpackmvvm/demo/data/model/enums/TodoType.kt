package me.hgj.jetpackmvvm.demo.data.model.enums

import android.graphics.Color

enum class TodoType(val type: Int, val color: Int, val content: String) {
    TodoType1(0, Color.parseColor("#fe7b7b"), "重要且紧急"),
    TodoType2(1, Color.parseColor("#dda239"), "重要不紧急"),
    TodoType3(2, Color.parseColor("#69adef"), "紧急但不重要"),
    TodoType4(3, Color.parseColor("#6cc959"), "不重要不紧急");

    companion object {
        /**
         * 根据传入的type得到相关的枚举类型 没有的话默认 重要且紧急
         */
        fun byType(type: Int): TodoType {
            values().forEach {
                if (type == it.type) {
                    return it
                }
            }
            return TodoType1
        }
        /**
         * 根据传入的content得到相关的枚举类型 没有的话默认 重要且紧急
         */
        fun byValue(content: String): TodoType {
            values().forEach {
                if (content == it.content) {
                    return it
                }
            }
            return TodoType1
        }
    }

}