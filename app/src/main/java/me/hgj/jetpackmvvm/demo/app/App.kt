package me.hgj.jetpackmvvm.demo.app

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import me.hgj.jetpackmvvm.core.JetpackMvvm
import me.hgj.jetpackmvvm.core.init.InitTaskManager
import me.hgj.jetpackmvvm.demo.app.init.NetTask
import me.hgj.jetpackmvvm.demo.app.init.UtilTask
import me.hgj.jetpackmvvm.demo.app.init.WidgetTask
import me.hgj.jetpackmvvm.ext.util.isMainProcess

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　:
 */

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        if (isMainProcess()){
            // 只在主进程初始化 SDK
            JetpackMvvm.init(this)
            //启动初始化任务
            InitTaskManager
                .register(NetTask())
                .register(UtilTask())
                .register(WidgetTask())
                .execute(this)
        }
    }
}
