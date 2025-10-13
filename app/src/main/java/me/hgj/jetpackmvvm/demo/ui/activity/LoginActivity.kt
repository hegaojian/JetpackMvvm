package me.hgj.jetpackmvvm.demo.ui.activity

import android.os.Bundle
import me.hgj.jetpackmvvm.core.data.obs
import me.hgj.jetpackmvvm.demo.app.core.base.BaseActivity
import me.hgj.jetpackmvvm.demo.app.core.util.UserManager
import me.hgj.jetpackmvvm.demo.data.vm.UserViewModel
import me.hgj.jetpackmvvm.demo.databinding.ActivityLoginBinding
import me.hgj.jetpackmvvm.ext.util.clickNoRepeat
import me.hgj.jetpackmvvm.ext.util.intent.openActivity
import me.hgj.jetpackmvvm.ext.view.afterTextChange
import me.hgj.jetpackmvvm.ext.view.showDialogMessage
import me.hgj.jetpackmvvm.ext.view.showPwd
import me.hgj.jetpackmvvm.ext.view.textString
import me.hgj.jetpackmvvm.ext.view.visibleOrInvisible

/**
 * 作者　：hegaojian
 * 时间　：2025/10/10
 * 说明　：
 */
class LoginActivity : BaseActivity<UserViewModel, ActivityLoginBinding>() {

    override val showTitle = true

    override val title = "登录"

    override fun initView(savedInstanceState: Bundle?) {
        mBind.userName.afterTextChange {
            mBind.clearImg.visibleOrInvisible(it.isNotEmpty())
        }
        mBind.password.afterTextChange {
            mBind.pwdVisible.visibleOrInvisible(it.isNotEmpty())
        }
        mBind.pwdVisible.setOnCheckedChangeListener { checkbox, checked ->
            mBind.password.showPwd(checked)
        }
    }

    override fun onBindViewClick() {
        mBind.clearImg.clickNoRepeat {
            mBind.userName.setText("")
        }
        mBind.loginSub.clickNoRepeat {
            //登录
            val userName = mBind.userName.textString()
            val pwd = mBind.password.textString()
            when {
                userName.isEmpty() -> showDialogMessage("请填写账号")
                pwd.isEmpty() -> showDialogMessage("请填写密码")
                else -> {
                    mViewModel.login(userName, pwd).obs(this)  {
                        onSuccess {
                            //请求成功
                            UserManager.saveUser(it)
                            finish()
                        }
                        onError {
                            //请求失败 onError 可以不写的，不写就默认走 ac/fr 的 onRequestError 方法，里面默认是吐司错误消息，RegisterFragment我就是没写的
                            showDialogMessage(it.msg)
                        }
                    }
                }
            }
        }
        mBind.toRegister.clickNoRepeat {
            //去注册
            openActivity<RegisterActivity>()
        }
    }
}