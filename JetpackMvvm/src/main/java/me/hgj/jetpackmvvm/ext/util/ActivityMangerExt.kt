package me.hgj.jetpackmvvm.ext.util

import android.app.Activity
import java.util.LinkedList

/**
 * 作者　：hegaojian
 * 时间　：2025/9/29
 * 说明　：
 */

private val activityList = LinkedList<Activity>()

/** app当前显示的Activity */
val currentActivity: Activity? get() = if (activityList.isEmpty()) null else activityList.last

/**
 * 添加Activity入栈
 * @param activity Activity
 */
fun addActivity(activity: Activity) {
    activityList.add(activity)
}

/**
 * 关闭Activity出栈
 * @param activity Activity
 */
fun finishActivity(activity: Activity) {
    if (!activity.isFinishing) {
        activity.finish()
    }
    activityList.remove(activity)
}

/**
 * 从栈移除activity 不会finish
 * @param activity Activity
 */
fun removeActivity(activity: Activity) {
    activityList.remove(activity)
}

/**
 * 关闭Activity出栈
 * @param cls Class<*>
 */
fun finishActivityByClass(cls: Class<*>) {
    if (activityList.isNullOrEmpty()) return
    val index = activityList.indexOfFirst { it.javaClass == cls }
    if (index == -1) return
    if (!activityList[index].isFinishing) {
        activityList[index].finish()
    }
    activityList.removeAt(index)
}

/**
 * 关闭所有的Activity 全部出栈
 */
fun finishAllActivity() {
    activityList.forEach {
        if (!it.isFinishing) {
            it.finish()
        }
    }
    activityList.clear()
}