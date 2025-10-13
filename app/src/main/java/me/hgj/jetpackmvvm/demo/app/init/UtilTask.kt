package me.hgj.jetpackmvvm.demo.app.init

import android.app.Application
import cat.ereza.customactivityoncrash.config.CaocConfig
import me.hgj.jetpackmvvm.demo.ui.activity.ErrorActivity
import me.hgj.jetpackmvvm.demo.ui.activity.SplashActivity
import me.hgj.jetpackmvvm.core.init.InitTask

/**
 * 作者　：hegaojian
 * 时间　：2025/9/29
 * 说明　：一些工具库的初始化，按照功能可以放在异步里面初始
 */
class UtilTask(
    override val name: String = "UtilTask",
    override val runOnMainThread: Boolean = false,
    override val isBlocking: Boolean = false
) : InitTask {
    override suspend fun init(app: Application) {
        //防止项目崩溃，崩溃后打开错误界面
        CaocConfig.Builder.create()
            .backgroundMode(CaocConfig.BACKGROUND_MODE_SILENT) //default: CaocConfig.BACKGROUND_MODE_SHOW_CUSTOM
            .enabled(true)//是否启用CustomActivityOnCrash崩溃拦截机制 必须启用！不然集成这个库干啥？？？
            .showErrorDetails(false) //是否必须显示包含错误详细信息的按钮 default: true
            .showRestartButton(false) //是否必须显示“重新启动应用程序”按钮或“关闭应用程序”按钮default: true
            .logErrorOnRestart(false) //是否必须重新堆栈堆栈跟踪 default: true
            .trackActivities(true) //是否必须跟踪用户访问的活动及其生命周期调用 default: false
            .minTimeBetweenCrashesMs(2000) //应用程序崩溃之间必须经过的时间 default: 3000
            .restartActivity(SplashActivity::class.java) // 重启的activity
            .errorActivity(ErrorActivity::class.java) //发生错误跳转的activity
            .apply()
    }
}