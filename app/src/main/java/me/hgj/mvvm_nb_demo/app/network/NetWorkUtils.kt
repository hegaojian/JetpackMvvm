package me.hgj.mvvm_nb_demo.app.network

import android.content.Context
import android.net.ConnectivityManager

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　:
 */
class NetWorkUtils {
    companion object {
        fun isNetworkAvailable(context: Context): Boolean {
            val manager = context.applicationContext.getSystemService(
                Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = manager.activeNetworkInfo
            return !(null == info || !info.isAvailable)
        }
    }
}