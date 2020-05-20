package me.hgj.jetpackmvvm.demo.ui.fragment.demo

import android.os.Bundle
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.base.BaseFragment
import me.hgj.jetpackmvvm.demo.databinding.FragmentDemoBinding
import me.hgj.jetpackmvvm.demo.viewmodel.request.RequestDemoViewModel
import me.hgj.jetpackmvvm.ext.getViewModel

class DemoFragment : BaseFragment<BaseViewModel, FragmentDemoBinding>() {

    val requestViewModel: RequestDemoViewModel by lazy { getViewModel<RequestDemoViewModel>() }

    override fun layoutId() = R.layout.fragment_demo

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun lazyLoadData() {

    }

    override fun createObserver() {
        super.createObserver()
    }
    inner class ProxClick {
        fun download() {
            requestViewModel.dowload()
        }
    }
}