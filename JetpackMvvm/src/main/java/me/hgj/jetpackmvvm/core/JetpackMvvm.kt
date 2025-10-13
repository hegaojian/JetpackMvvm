package me.hgj.jetpackmvvm.core

import android.app.Application
import android.view.Gravity
import com.hjq.toast.Toaster
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.tencent.mmkv.MMKV
import me.hgj.jetpackmvvm.R
import me.hgj.jetpackmvvm.core.net.interception.logging.util.LogUtils
import me.hgj.jetpackmvvm.ext.util.isApkInDebug
import me.hgj.jetpackmvvm.ext.lifecycle.KtxActivityLifecycleCallbacks
import me.hgj.jetpackmvvm.ext.util.XLog
import me.hgj.jetpackmvvm.ext.util.dp
import me.hgj.jetpackmvvm.ext.util.getColorExt
import me.hgj.jetpackmvvm.widget.loadsir.callback.SuccessCallback
import me.hgj.jetpackmvvm.widget.loadsir.core.LoadSir
import me.hgj.jetpackmvvm.widget.state.BaseEmptyCallback
import me.hgj.jetpackmvvm.widget.state.BaseErrorCallback
import me.hgj.jetpackmvvm.widget.state.BaseLoadingCallback

/**
 * 作者　: hegaojian
 * 时间　: 2022/1/13
 * 描述　:
 */

/**
 * 全局上下文，可直接拿
 */
val appContext: Application by lazy { JetpackMvvm.app }

object JetpackMvvm {

    lateinit var app: Application

    /**
     * 打印日志开关，框架是否打印请求日志、输出Log日志 默认为 true 打印数据
     */
    private var jetpackMvvmLogEnable: Boolean = true
        set(value) {
            field = value
            XLog.init(value)
            LogUtils.setLog(value)
        }
    /**
     * 框架初始化
     * @param application Application 全局上下文
     */
    fun init(application: Application) {
        app = application
        //日志开关
        jetpackMvvmLogEnable = isApkInDebug
        //初始化MMKV
        MMKV.initialize(app)
        //注册全局 activity生命周期监听
        application.registerActivityLifecycleCallbacks(KtxActivityLifecycleCallbacks())
        //注册全局默认的 错误 空 加载中 状态布局
        LoadSir.beginBuilder()
            .setErrorCallBack(BaseErrorCallback())
            .setEmptyCallBack(BaseEmptyCallback())
            .setLoadingCallBack(BaseLoadingCallback())
            .setDefaultCallback(SuccessCallback::class.java)
            .commit()
        Toaster.init(app)
        Toaster.setGravity(Gravity.BOTTOM, 0, 100.dp)

        SmartRefreshLayout.setDefaultRefreshInitializer { context, layout ->
            //设置 SmartRefreshLayout 通用配置
            layout.setEnableScrollContentWhenLoaded(true)//是否在加载完成时滚动列表显示新的内容
            layout.setFooterTriggerRate(0.6f)
        }
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            //设置 Head
            ClassicsHeader(context).apply {
                setAccentColor(getColorExt(R.color.helperColorBlack))
            }
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            //设置 Footer
            ClassicsFooter(context).apply {
                setAccentColor(getColorExt(R.color.helperColorBlack))
            }
        }
    }
}