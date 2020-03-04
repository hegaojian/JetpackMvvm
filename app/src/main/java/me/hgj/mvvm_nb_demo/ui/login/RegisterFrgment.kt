package me.hgj.mvvm_nb_demo.ui.login

import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.include_toolbar.*
import me.hgj.mvvm_nb.ext.view.clickNoRepeat
import me.hgj.mvvm_nb.ext.parseState
import me.hgj.mvvm_nb_demo.R
import me.hgj.mvvm_nb_demo.app.base.BaseFragment
import me.hgj.mvvm_nb_demo.app.ext.init
import me.hgj.mvvm_nb_demo.app.ext.initClose
import me.hgj.mvvm_nb_demo.app.ext.showMessage
import me.hgj.mvvm_nb_demo.app.util.CacheUtil
import me.hgj.mvvm_nb_demo.app.util.SettingUtil
import me.hgj.mvvm_nb_demo.databinding.FragmentRegisterBinding

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/24
 * 描述　:
 */
class RegisterFrgment : BaseFragment<LoginRegisterViewModel, FragmentRegisterBinding>() {

    override fun layoutId() = R.layout.fragment_register

    override fun initView() {

        mDatabind.viewmodel = mViewModel

        toolbar.initClose("注册") { Navigation.findNavController(it).navigateUp()}

        //设置颜色跟主题颜色一致
        appViewModel.appColor.value?.let {
            SettingUtil.setShapColor(register_sub, it)
            toolbar. setBackgroundColor(it)
        }

        register_clear.setOnClickListener { mViewModel.username.set("") }

        register_key.setOnCheckedChangeListener { buttonView, isChecked ->
            mViewModel.isShowPwd.set(isChecked)
        }

        register_key1.setOnCheckedChangeListener { buttonView, isChecked ->
            mViewModel.isShowPwd2.set(isChecked)
        }

        register_sub.clickNoRepeat {
            when {
                mViewModel.username.get().isEmpty() -> showMessage("请填写账号")
                mViewModel.password.get().isEmpty() -> showMessage("请填写密码")
                mViewModel.password2.get().isEmpty() -> showMessage("请填写确认密码")
                mViewModel.password.get().length < 6 -> showMessage("密码最少6位")
                mViewModel.password.get() != mViewModel.password2.get() -> showMessage("密码不一致")
                else -> mViewModel.registerAndlogin()
            }
        }
    }

    override fun createObserver() {
        mViewModel.loginResult.observe(this, Observer { viewState ->
            parseState(viewState, {
                CacheUtil.setUser(it)
                appViewModel.userinfo.postValue(it)
                Navigation.findNavController(toolbar)
                    .navigate(R.id.action_registerFrgment_to_mainFragment)
            }, {
                showMessage(it.errorMsg)
            })
        })
    }

    override fun lazyLoadData() {

    }
}