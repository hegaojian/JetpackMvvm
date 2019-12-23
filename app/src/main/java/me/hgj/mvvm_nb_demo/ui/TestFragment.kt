package me.hgj.mvvm_nb_demo.ui

import me.hgj.mvvm_nb_demo.R
import me.hgj.mvvm_nb_demo.app.base.BaseFragment
import me.hgj.mvvm_nb_demo.databinding.ActivityLoginBinding
import me.hgj.mvvm_nb_demo.viewmodel.LoginViewModel

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　: 用法跟Activity一样
 */
class TestFragment: BaseFragment<LoginViewModel,ActivityLoginBinding>() {

    override fun layoutId() = R.layout.activity_login



    override fun lazyLoadData() {

    }

    override fun createObserver() {

    }
}