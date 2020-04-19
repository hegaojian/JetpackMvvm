package me.hgj.jetpackmvvm.demo.ui.login

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.android.synthetic.main.include_toolbar.*
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.base.BaseFragment
import me.hgj.jetpackmvvm.demo.app.ext.hideSoftKeyboard
import me.hgj.jetpackmvvm.demo.app.ext.initClose
import me.hgj.jetpackmvvm.demo.app.ext.showMessage
import me.hgj.jetpackmvvm.demo.app.util.CacheUtil
import me.hgj.jetpackmvvm.demo.app.util.SettingUtil
import me.hgj.jetpackmvvm.demo.databinding.FragmentRegisterBinding
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.ext.util.setOnclickNoRepeat
import me.hgj.jetpackmvvm.ext.view.clickNoRepeat

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/24
 * 描述　:
 */
class RegisterFrgment : BaseFragment<LoginRegisterViewModel, FragmentRegisterBinding>() {

    override fun layoutId() = R.layout.fragment_register

    override fun initView(savedInstanceState: Bundle?)  {
        mDatabind.viewmodel = mViewModel
        toolbar.initClose("注册") {
            hideSoftKeyboard(activity)
            Navigation.findNavController(it).navigateUp()
        }
        //设置颜色跟主题颜色一致
        appViewModel.appColor.value?.let {
            SettingUtil.setShapColor(registerSub, it)
            toolbar.setBackgroundColor(it)
        }
    }

    override fun createObserver() {
        mViewModel.loginResult.observe(viewLifecycleOwner, Observer { resultState ->
            parseState(resultState, {
                CacheUtil.setUser(it)
                appViewModel.userinfo.postValue(it)
                Navigation.findNavController(toolbar)
                    .navigate(R.id.action_registerFrgment_to_mainFragment)
            }, {
                showMessage(it.errorMsg)
            })
        })
    }

    override fun lazyLoadData() { }

    override fun onViewClicked() {
        setOnclickNoRepeat(registerClear,registerSub){
            when(it.id){
                R.id.registerClear ->{
                    mViewModel.username.set("")
                }
                R.id.registerSub ->{
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
        }
        registerKey.setOnCheckedChangeListener { buttonView, isChecked ->
            mViewModel.isShowPwd.set(isChecked)
        }

        registerKey1.setOnCheckedChangeListener { buttonView, isChecked ->
            mViewModel.isShowPwd2.set(isChecked)
        }
    }
}