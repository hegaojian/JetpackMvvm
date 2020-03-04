package me.hgj.mvvm_nb_demo

import android.content.Context
import com.kingja.loadsir.callback.SuccessCallback
import com.kingja.loadsir.core.LoadSir
import com.tencent.mmkv.MMKV
import me.hgj.mvvm_nb.BaseApp
import me.hgj.mvvm_nb_demo.app.weight.loadCallBack.EmptyCallback
import me.hgj.mvvm_nb_demo.app.weight.loadCallBack.ErrorCallback
import me.hgj.mvvm_nb_demo.app.weight.loadCallBack.LoadingCallback
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
        MMKV.initialize(this.filesDir.absolutePath + "/mmkv")

        //界面加载管理 初始化
        LoadSir.beginBuilder()
            .addCallback(LoadingCallback())//加载
            .addCallback(ErrorCallback())//错误
            .addCallback(EmptyCallback())//空
            .setDefaultCallback(SuccessCallback::class.java)//设置默认加载状态页
            .commit()
    }
}