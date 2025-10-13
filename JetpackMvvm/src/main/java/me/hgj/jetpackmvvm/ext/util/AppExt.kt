package me.hgj.jetpackmvvm.ext.util

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Process
import me.hgj.jetpackmvvm.core.appContext

/**
 * 作者　: hegaojian
 * 时间　: 2020/11/17
 * 描述　:
 */

/**
 * 获取当前进程的名称，默认进程名称是包名
 */
val currentProcessName: String?
    get() {
        val pid = Process.myPid()
        val mActivityManager = appContext.getSystemService(
            Context.ACTIVITY_SERVICE
        ) as ActivityManager
        for (appProcess in mActivityManager.runningAppProcesses) {
            if (appProcess.pid == pid) {
                return appProcess.processName
            }
        }
        return null
    }

fun Application.isMainProcess(): Boolean {
    val pid = Process.myPid()
    val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    val processName = manager.runningAppProcesses.firstOrNull { it.pid == pid }?.processName
    return processName == packageName
}

/***
 * 判断当前是否在debug模式下
 */
val isApkInDebug: Boolean get() {
    return try {
        val info: ApplicationInfo = appContext.applicationInfo
        info.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
    } catch (e: Exception) {
        false
    }
}


/**
 * 获取versionName
 */
fun Context.getAppVersion(): String {
    try {
        val pi = packageManager.getPackageInfo(packageName, 0)
        return pi.versionName?:""
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }
    return ""
}



