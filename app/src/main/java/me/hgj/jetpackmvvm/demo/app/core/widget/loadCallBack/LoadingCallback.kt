package me.hgj.jetpackmvvm.demo.app.core.widget.loadCallBack

import android.content.Context
import android.view.View
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.widget.loadsir.callback.Callback

/**
 * 作者　: hegaojian
 * 时间　: 2023/3/28
 * 描述　:
 */
class LoadingCallback : Callback() {

    override fun onCreateView() = R.layout.layout_loading

    override fun onReloadEvent(context: Context?, view: View?) = true
}