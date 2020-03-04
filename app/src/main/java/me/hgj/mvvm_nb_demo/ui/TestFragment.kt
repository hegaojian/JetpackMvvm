package me.hgj.mvvm_nb_demo.ui

import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_test.*
import me.hgj.mvvm_nb.BaseViewModel
import me.hgj.mvvm_nb_demo.R
import me.hgj.mvvm_nb_demo.app.base.BaseFragment
import me.hgj.mvvm_nb_demo.app.network.NetworkApi
import me.hgj.mvvm_nb_demo.app.util.CacheUtil
import me.hgj.mvvm_nb_demo.databinding.FragmentTestBinding

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　: 用法跟Activity一样
 */
class TestFragment() : BaseFragment<BaseViewModel, FragmentTestBinding>() {

    override fun layoutId() = R.layout.fragment_test

    override fun initView() {
        //设置用户名
        if (appViewModel.userinfo.value != null) {
            test_text.text = appViewModel.userinfo.value?.username
        } else {
            test_text.text = "没登录"
        }

        test_text.setOnClickListener {
            if (appViewModel.userinfo.value != null) {
                //清空cookie
                NetworkApi.cookieJar.clear()
                appViewModel.userinfo.postValue(null)
                CacheUtil.setUser(null)
            } else {
                Navigation.findNavController(it).navigate(R.id.action_mainFragment_to_loginFragment)
            }
        }
    }

    override fun lazyLoadData() {

    }

    override fun createObserver() {

    }

}