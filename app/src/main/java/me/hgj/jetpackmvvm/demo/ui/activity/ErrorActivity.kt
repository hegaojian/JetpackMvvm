package me.hgj.jetpackmvvm.demo.ui.activity

import android.content.ClipData
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import cat.ereza.customactivityoncrash.CustomActivityOnCrash
import me.hgj.jetpackmvvm.base.vm.BaseViewModel
import me.hgj.jetpackmvvm.demo.app.core.base.BaseActivity
import me.hgj.jetpackmvvm.demo.app.core.ext.initClose
import me.hgj.jetpackmvvm.demo.databinding.ActivityErrorBinding
import me.hgj.jetpackmvvm.ext.util.clickNoRepeat
import me.hgj.jetpackmvvm.ext.util.clipboardManager
import me.hgj.jetpackmvvm.ext.util.toast
import me.hgj.jetpackmvvm.ext.view.showDialogMessage


/**
 * 作者　: hegaojian
 * 时间　: 2020/3/12
 * 描述　:
 */
class ErrorActivity : BaseActivity<BaseViewModel, ActivityErrorBinding>() {

    override fun initView(savedInstanceState: Bundle?)  {
        mToolbar.initClose("发送错误"){
            finish()
        }
        val config = CustomActivityOnCrash.getConfigFromIntent(intent)
        mBind.errorRestart.clickNoRepeat{
            config?.run {
                CustomActivityOnCrash.restartApplication(this@ErrorActivity, this)
            }
        }

        mBind.errorSendError.clickNoRepeat {
            CustomActivityOnCrash.getStackTraceFromIntent(intent)?.let {
                showDialogMessage(it,"发现有Bug快去联系作者，他说要V你50","马上去",{
                    val mClipData = ClipData.newPlainText("errorLog",it)
                    // 将ClipData内容放到系统剪贴板里。
                    clipboardManager?.setPrimaryClip(mClipData)
                    "已复制错误日志".toast()
                    try {
                        val url = "mqqwpa://im/chat?chat_type=wpa&uin=824868922"
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
                    } catch (e: Exception) {
                        "请先安装QQ".toast()
                    }
                },"就不")
            }


        }
    }
}