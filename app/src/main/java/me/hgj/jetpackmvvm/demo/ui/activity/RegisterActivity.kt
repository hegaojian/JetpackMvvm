package me.hgj.jetpackmvvm.demo.ui.activity

import android.os.Bundle
import me.hgj.jetpackmvvm.core.data.obs
import me.hgj.jetpackmvvm.demo.app.core.base.BaseActivity
import me.hgj.jetpackmvvm.demo.app.core.util.UserManager
import me.hgj.jetpackmvvm.demo.data.vm.UserViewModel
import me.hgj.jetpackmvvm.demo.databinding.ActivityRegisterBinding
import me.hgj.jetpackmvvm.ext.util.clickNoRepeat
import me.hgj.jetpackmvvm.ext.util.finishActivityByClass
import me.hgj.jetpackmvvm.ext.view.afterTextChange
import me.hgj.jetpackmvvm.ext.view.showDialogMessage
import me.hgj.jetpackmvvm.ext.view.showPwd
import me.hgj.jetpackmvvm.ext.view.textString
import me.hgj.jetpackmvvm.ext.view.visibleOrInvisible

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/24
 * 描述　:
 */
class RegisterActivity : BaseActivity<UserViewModel, ActivityRegisterBinding>() {

    override val showTitle = true

    override val title = "注册"

    override fun initView(savedInstanceState: Bundle?) {
        mBind.userName.afterTextChange {
            mBind.clearImg.visibleOrInvisible(it.isNotEmpty())
        }
        mBind.password.afterTextChange {
            mBind.pwdVisible1.visibleOrInvisible(it.isNotEmpty())
        }
        mBind.pwdVisible1.setOnCheckedChangeListener { checkbox, checked ->
            mBind.password.showPwd(checked)
        }
        mBind.password2.afterTextChange {
            mBind.pwdVisible2.visibleOrInvisible(it.isNotEmpty())
        }
        mBind.pwdVisible2.setOnCheckedChangeListener { checkbox, checked ->
            mBind.password2.showPwd(checked)
        }
    }

    override fun onBindViewClick() {
        mBind.clearImg.clickNoRepeat {
            mBind.userName.setText("")
        }
        mBind.registerSub.clickNoRepeat {
            val userName = mBind.userName.textString()
            val password = mBind.password.textString()
            val password2 = mBind.password2.textString()
            when {
                userName.isEmpty() -> showDialogMessage("请填写账号")
                password.isEmpty() -> showDialogMessage("请填写密码")
                password2.isEmpty() -> showDialogMessage("请填写确认密码")
                password.length < 6 -> showDialogMessage("密码最少6位")
                password != password2 -> showDialogMessage("密码不一致")
                else -> {
                    mViewModel.register(userName, password).obs(this)  {
                        onSuccess {
                            UserManager.saveUser(it)
                            finish()
                            finishActivityByClass(LoginActivity::class.java)
                        }
                    }
                }
            }
        }
    }

}