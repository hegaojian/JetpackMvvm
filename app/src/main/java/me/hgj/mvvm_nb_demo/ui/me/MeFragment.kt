package me.hgj.mvvm_nb_demo.ui.me

import me.hgj.mvvm_nb_demo.R
import me.hgj.mvvm_nb_demo.app.base.BaseFragment
import me.hgj.mvvm_nb_demo.databinding.FragmentMeBinding

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　: 用法跟Activity一样
 */
class MeFragment : BaseFragment<MeViewModel, FragmentMeBinding>() {


    override fun layoutId() = R.layout.fragment_me

    override fun initView() {

    }

    override fun lazyLoadData() {


    }

    override fun createObserver() {

    }
}