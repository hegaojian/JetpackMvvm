package me.hgj.mvvm_nb_demo

import android.content.Context
import me.hgj.mvvm_nb.BaseApp
import kotlin.properties.Delegates

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　:
 */
class App:BaseApp() {
    companion object {
        var CONTEXT: Context by Delegates.notNull()
    }

    override fun onCreate() {
        super.onCreate()
        CONTEXT = applicationContext
    }
}