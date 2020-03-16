package me.hgj.jetpackmvvm.demo.ui.welcome

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import cat.ereza.customactivityoncrash.CustomActivityOnCrash
import kotlinx.android.synthetic.main.activity_error.*
import kotlinx.android.synthetic.main.include_toolbar.*
import me.hgj.jetpackmvvm.BaseViewModel
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.base.BaseActivity
import me.hgj.jetpackmvvm.demo.app.ext.init
import me.hgj.jetpackmvvm.demo.databinding.ActivityErrorBinding


/**
 * 作者　: hegaojian
 * 时间　: 2020/3/12
 * 描述　:
 */
class ErrorActivity :BaseActivity<BaseViewModel,ActivityErrorBinding>() {

    override fun layoutId() = R.layout.activity_error

    override fun initView() {
        toolbar.init("发生错误")
        val config = CustomActivityOnCrash.getConfigFromIntent(intent)
        error_restart.setOnClickListener {
            config?.run {
                CustomActivityOnCrash.restartApplication(this@ErrorActivity, this)
            }
        }
        error_sendError.setOnClickListener {
            //获取剪贴板管理器：
            val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            // 创建普通字符型ClipData0
            val mClipData = ClipData.newPlainText("errorLog", CustomActivityOnCrash.getStackTraceFromIntent(intent))
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData)
            showToast("已复制错误日志")
            try {
                val url = "mqqwpa://im/chat?chat_type=wpa&uin=824868922"
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            } catch (e: Exception) {
                showToast("请先安装QQ")
            }
        }
    }

    override fun createObserver() {

    }
}