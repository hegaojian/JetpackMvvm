package me.hgj.jetpackmvvm.demo.ui.fragment.demo

import android.os.Bundle
import androidx.fragment.app.viewModels
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.base.BaseFragment
import me.hgj.jetpackmvvm.demo.databinding.FragmentDemoBinding
import me.hgj.jetpackmvvm.demo.viewmodel.request.RequestDemoViewModel

class DemoFragment : BaseFragment<BaseViewModel, FragmentDemoBinding>() {

    val requestViewModel: RequestDemoViewModel by viewModels()

    override fun layoutId() = R.layout.fragment_demo

    override fun initView(savedInstanceState: Bundle?) {

    }

    inner class ProxClick {
        fun download() {
            requestViewModel.dowload()
        }
    }
}