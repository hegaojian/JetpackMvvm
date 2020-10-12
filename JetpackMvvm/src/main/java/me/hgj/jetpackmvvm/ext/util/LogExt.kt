package me.hgj.jetpackmvvm.ext.util

import android.util.Log

const val TAG = "JetpackMvvm"

/**
 *
 * 是否需要开启打印日志，默认打开，1.1.7-1.1.8版本是默认关闭的(1.0.0-1.1.6没有这个字段，框架在远程依赖下，直接不打印log)，但是默认关闭后很多人反馈都没有日志，好吧，我的我的
 * 根据true|false 控制网络请求日志和该框架产生的打印
 */
var jetpackMvvmLog = true

private enum class LEVEL {
    V, D, I, W, E
}

fun String.logv(tag: String = TAG) =
    log(LEVEL.V, tag, this)
fun String.logd(tag: String = TAG) =
    log(LEVEL.D, tag, this)
fun String.logi(tag: String = TAG) =
    log(LEVEL.I, tag, this)
fun String.logw(tag: String = TAG) =
    log(LEVEL.W, tag, this)
fun String.loge(tag: String = TAG) =
    log(LEVEL.E, tag, this)

private fun log(level: LEVEL, tag: String, message: String) {
    if (!jetpackMvvmLog) return
    when (level) {
        LEVEL.V -> Log.v(tag, message)
        LEVEL.D -> Log.d(tag, message)
        LEVEL.I -> Log.i(tag, message)
        LEVEL.W -> Log.w(tag, message)
        LEVEL.E -> Log.e(tag, message)
    }
}