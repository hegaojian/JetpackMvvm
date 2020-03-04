package me.hgj.mvvm_nb_demo.ui.lookinfo

import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_lookinfo.*
import kotlinx.android.synthetic.main.include_toolbar.*
import me.hgj.mvvm_nb_demo.R
import me.hgj.mvvm_nb_demo.app.base.BaseFragment
import me.hgj.mvvm_nb_demo.app.ext.initClose
import me.hgj.mvvm_nb_demo.databinding.FragmentLookinfoBinding

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/4
 * 描述　:
 */
class LookInfoFragment : BaseFragment<LookInfoViewModel, FragmentLookinfoBinding>() {

    override fun layoutId() = R.layout.fragment_lookinfo

    override fun initView() {

        mDatabind.vm = mViewModel

        toolbar.initClose("他的信息") {
            Navigation.findNavController(it).navigateUp()
        }
        appViewModel.appColor.value?.let { share_layout.setBackgroundColor(it) }
    }

    override fun lazyLoadData() {

    }

    override fun createObserver() {

    }
}