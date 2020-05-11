package me.hgj.jetpackmvvm.demo.ui.fragment.login

import android.os.Bundle
import android.widget.CompoundButton
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.include_toolbar.*
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.base.BaseFragment
import me.hgj.jetpackmvvm.demo.app.ext.hideSoftKeyboard
import me.hgj.jetpackmvvm.demo.app.ext.initClose
import me.hgj.jetpackmvvm.demo.app.ext.showMessage
import me.hgj.jetpackmvvm.demo.app.util.CacheUtil
import me.hgj.jetpackmvvm.demo.app.util.SettingUtil
import me.hgj.jetpackmvvm.demo.databinding.FragmentLoginBinding
import me.hgj.jetpackmvvm.demo.viewmodel.request.RequestLoginRegisterViewModel
import me.hgj.jetpackmvvm.demo.viewmodel.state.LoginRegisterViewModel
import me.hgj.jetpackmvvm.ext.getViewModel
import me.hgj.jetpackmvvm.ext.nav
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.ext.util.logd
import me.hgj.jetpackmvvm.ext.util.toJson

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　: 登录页面
 */
class LoginFragment : BaseFragment<LoginRegisterViewModel, FragmentLoginBinding>() {

    /** 注意，在by lazy中使用getViewModel一定要使用泛型，虽然他提示不报错，但是你不写是不行的 */
    private val requestLoginRegisterViewModel: RequestLoginRegisterViewModel by lazy { getViewModel<RequestLoginRegisterViewModel>() }

    override fun layoutId() = R.layout.fragment_login

    override fun initView(savedInstanceState: Bundle?) {

        mDatabind.viewmodel = mViewModel
        mDatabind.click = ProxyClick()

        toolbar.initClose("登录") {
            nav().navigateUp()
        }
        //设置颜色跟主题颜色一致
        shareViewModel.appColor.value.let {
            SettingUtil.setShapColor(loginSub, it)
            loginGoregister.setTextColor(it)
            toolbar.setBackgroundColor(it)
        }
    }

    override fun createObserver() {
        //监听请求结果
        requestLoginRegisterViewModel.loginResult.observe(
            viewLifecycleOwner,
            Observer { resultState ->
                parseState(resultState, {
                    //登录成功 通知账户数据发生改变鸟
                    CacheUtil.setUser(it)
                    shareViewModel.isLogin.postValue(true)
                    shareViewModel.userinfo.postValue(it)
                    nav().navigateUp()
                }, {
                    //登录失败
                    showMessage(it.errorMsg)
                })
            })
        mViewModel.data1.observe(viewLifecycleOwner, Observer {
            it.toJson().logd("hgj")
        })
    }

    override fun lazyLoadData() {}


    inner class ProxyClick {

        fun clear() {
            mViewModel.data1
            mViewModel.username.postValue("")
        }

        fun login() {
            when {
                mViewModel.username.value.isEmpty() -> showMessage("请填写账号")
                mViewModel.password.value.isEmpty() -> showMessage("请填写密码")
                else -> requestLoginRegisterViewModel.loginReq(
                    mViewModel.username.value,
                    mViewModel.password.value
                )
            }
        }

        fun goRegister() {
            hideSoftKeyboard(activity)
            nav().navigate(R.id.action_loginFragment_to_registerFrgment)
        }

        var onCheckedChangeListener =
            CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
                mViewModel.isShowPwd.postValue(isChecked)
            }

    }
}