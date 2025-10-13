package me.hgj.jetpackmvvm.core.net.interception.logging.util

import android.text.TextUtils
import android.util.Log
import me.hgj.jetpackmvvm.ext.util.XLog

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/26
 * 描述　:
 */
class LogUtils private constructor() {
    companion object {
        private const val DEFAULT_TAG = "MvvmHelper"
        private var isLog = true
        fun isLog(): Boolean {
            return isLog
        }
        fun setLog(isLog: Boolean) {
            Companion.isLog = isLog
            XLog.init(isLog)
        }

        fun debugInfo(tag: String?, msg: String?) {
            if (!isLog || TextUtils.isEmpty(msg)) {
                return
            }
            Log.d(tag, msg?:"")
        }

        fun debugInfo(msg: String?) {
            debugInfo(DEFAULT_TAG, msg)
        }

        fun warnInfo(tag: String?, msg: String?) {
            if (!isLog || TextUtils.isEmpty(msg)) {
                return
            }
            Log.w(tag, msg?:"")
        }

        fun warnInfo(msg: String?) {
            warnInfo(DEFAULT_TAG, msg)
        }

        /**
         * 这里使用自己分节的方式来输出足够长度的 message
         *
         * @param tag 标签
         * @param msg 日志内容
         */
        fun debugLongInfo(tag: String?, msg: String) {
            var msg = msg
            if (!isLog || TextUtils.isEmpty(msg)) {
                return
            }
            msg = msg.trim { it <= ' ' }
            var index = 0
            val maxLength = 3500
            var sub: String
            while (index < msg.length) {
                sub = if (msg.length <= index + maxLength) {
                    msg.substring(index)
                } else {
                    msg.substring(index, index + maxLength)
                }
                index += maxLength
                Log.d(tag, sub.trim { it <= ' ' })
            }
        }

        fun debugLongInfo(msg: String) {
            debugLongInfo(DEFAULT_TAG, msg)
        }
    }

    init {
        throw IllegalStateException("you can't instantiate me!")
    }
}