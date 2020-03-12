package me.hgj.jetpackmvvm.demo.app.weight.webview

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.util.AttributeSet
import android.webkit.WebView

/**
 * 自定义webview 解决5.1手机打开网页闪退的bug
 * @Author:         hegaojian
 * @CreateDate:     2019/10/11 14:23
 */
class LollipopFixedWebView : WebView {
    constructor(context: Context) : super(getFixedContext(context))
    constructor(context: Context, attrs: AttributeSet) : super(getFixedContext(context), attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(getFixedContext(context), attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(getFixedContext(context), attrs, defStyleAttr, defStyleRes)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, privateBrowsing: Boolean) : super(getFixedContext(context), attrs, defStyleAttr, privateBrowsing)

    companion object {
        fun getFixedContext(context: Context): Context {
            return context.createConfigurationContext(Configuration())
        }
    }

}
