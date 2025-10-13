package me.hgj.jetpackmvvm.demo.ui.fragment.project

import android.os.Bundle
import androidx.fragment.app.Fragment
import me.hgj.jetpackmvvm.core.data.obs
import me.hgj.jetpackmvvm.demo.app.core.base.BaseFragment
import me.hgj.jetpackmvvm.demo.app.core.ext.bindViewPager2
import me.hgj.jetpackmvvm.demo.app.core.ext.init
import me.hgj.jetpackmvvm.demo.data.vm.ProjectViewModel
import me.hgj.jetpackmvvm.demo.databinding.FragmentViewpagerBinding
import me.hgj.jetpackmvvm.ext.util.statusPadding

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/28
 * 描述　:
 */
class ProjectFragment : BaseFragment<ProjectViewModel, FragmentViewpagerBinding>() {

    companion object {
        fun newInstance(): ProjectFragment {
            val args = Bundle()
            val fragment = ProjectFragment()
            fragment.arguments = args
            return fragment
        }
    }

    /** fragment集合 */
    var fragments: ArrayList<Fragment> = arrayListOf()

    /** 标题集合 */
    var mDataList: ArrayList<String> = arrayListOf()

    override fun initView(savedInstanceState: Bundle?) {
        mBind.includeLayout.viewpagerLinear.statusPadding()
        //初始化 viewpager
        mBind.includeLayout.viewPager.init(this, fragments)
        //初始化 指示器
        mBind.includeLayout.magicIndicator.bindViewPager2(mBind.includeLayout.viewPager, mDataList)
    }

    override fun getLoadingView() = mBind.includeLayout.viewPager

    override fun lazyLoadData() {
        onLoadRetry()
    }

    override fun onLoadRetry() {
        super.onLoadRetry()
        mViewModel.getProjectTitle().obs(viewLifecycleOwner) {
            onSuccess {
                mDataList.clear()
                fragments.clear()
                mDataList.add("最新项目")
                mDataList.addAll(it.map { it.name })
                fragments.add(ProjectChildFragment.newInstance(0, true))
                it.forEach { classify ->
                    fragments.add(ProjectChildFragment.newInstance(classify.id, false))
                }
                mBind.includeLayout.magicIndicator.navigator.notifyDataSetChanged()
                mBind.includeLayout.viewPager.adapter?.notifyDataSetChanged()
                mBind.includeLayout.viewPager.offscreenPageLimit = fragments.size
            }
        }
    }

}