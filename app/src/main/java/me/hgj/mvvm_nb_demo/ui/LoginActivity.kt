package me.hgj.mvvm_nb_demo.ui

import android.text.InputType
import android.widget.CompoundButton
import androidx.lifecycle.Observer
import me.hgj.mvvm_nb.ext.parseState
import me.hgj.mvvm_nb_demo.R
import me.hgj.mvvm_nb_demo.app.base.BaseActivity
import me.hgj.mvvm_nb_demo.app.ext.toTextString
import me.hgj.mvvm_nb_demo.databinding.ActivityLoginBinding
import me.hgj.mvvm_nb_demo.viewmodel.LoginViewModel

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　:
 */
class LoginActivity: BaseActivity<LoginViewModel, ActivityLoginBinding>() {
    override fun layoutId() = R.layout.activity_login

    override fun initView() {
        mDatabind.viewmodel = mViewModel
        mDatabind.clickPox = LoginClick()
    }

    override fun createObserver() {
        //监听请求结果
        mViewModel.loginResult.observe(this, Observer { viewState ->
            parseState(viewState,{
                //登录成功
                //通知账户数据发生改变鸟
                appViewModel.userinfo.postValue(it)
                finish()
            },{
                //登录失败
                showMessage(it.errorMsg)
            })
        })
    }

    inner class LoginClick : CompoundButton.OnCheckedChangeListener {
        //清空
        fun clear() {
            mViewModel.username.set("")
        }
        //登录
        fun login() {
            when {
                mViewModel.username.get().isEmpty() -> showMessage("请填写账户名")
                mViewModel.password.get().isEmpty() -> showMessage("请填写密码")
                else -> mViewModel.login()
            }
        }
        // CheckBox的状态改变监听
        override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
            if (isChecked) {
                //设置明文密码
                mDatabind.loginPwd.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            } else {
                //关闭明文密码
                mDatabind.loginPwd.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            }
            mDatabind.loginPwd.setSelection(mDatabind.loginPwd.toTextString().length)
        }

    }

}