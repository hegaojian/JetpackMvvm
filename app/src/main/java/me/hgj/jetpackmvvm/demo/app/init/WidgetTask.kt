package me.hgj.jetpackmvvm.demo.app.init

import android.app.Application
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.core.widget.loadCallBack.LoadingCallback
import me.hgj.jetpackmvvm.ext.util.getColorExt
import me.hgj.jetpackmvvm.widget.loadsir.callback.SuccessCallback
import me.hgj.jetpackmvvm.widget.loadsir.core.LoadSir
import me.hgj.jetpackmvvm.core.init.InitTask
import me.hgj.jetpackmvvm.widget.state.BaseEmptyCallback
import me.hgj.jetpackmvvm.widget.state.BaseErrorCallback

/**
 * 作者　：hegaojian
 * 时间　：2025/9/29
 * 说明　：常用控件初始化
 */
class WidgetTask(
    override val name: String = "WidgetTask",
    override val runOnMainThread: Boolean = false,
    override val isBlocking: Boolean = false
) : InitTask {
    override suspend fun init(app: Application) {
        SmartRefreshLayout.setDefaultRefreshInitializer { context, layout ->
            //设置 SmartRefreshLayout 通用配置
            layout.setEnableScrollContentWhenLoaded(true)//是否在加载完成时滚动列表显示新的内容
            layout.setFooterTriggerRate(0.6f)
        }
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            //设置 Head
            ClassicsHeader(context).apply {
                setAccentColor(getColorExt(R.color.black))
            }
        }
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            //设置 Footer
            ClassicsFooter(context).apply {
                setAccentColor(getColorExt(R.color.black))
            }
        }

        //这里写的原因是 框架的 状态布局页面可能不适用于你的项目，你可以添加自己的 错误 空 加载中全局配置
        LoadSir.beginBuilder()
            .setErrorCallBack(BaseErrorCallback())
            .setEmptyCallBack(BaseEmptyCallback())
            .setLoadingCallBack(LoadingCallback()) //比如我替换了全局loading加载
//            .setLoadingCallBack(BaseLoadingCallback())
            .setDefaultCallback(SuccessCallback::class.java)
            .commit()
    }

}