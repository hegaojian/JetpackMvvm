package me.hgj.jetpackmvvm.demo.ui.fragment.publicNumber

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import me.hgj.jetpackmvvm.core.data.obs
import me.hgj.jetpackmvvm.demo.app.core.base.BaseFragment
import me.hgj.jetpackmvvm.demo.app.core.ext.bindViewPager2
import me.hgj.jetpackmvvm.demo.app.core.ext.init
import me.hgj.jetpackmvvm.demo.databinding.FragmentViewpagerBinding
import me.hgj.jetpackmvvm.demo.data.vm.PublicNumberViewModel
import me.hgj.jetpackmvvm.ext.util.statusPadding

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/28
 * 描述　: 聚合公众号
 */
class PublicNumberFragment : BaseFragment<PublicNumberViewModel, FragmentViewpagerBinding>() {

    companion object{
        fun newInstance(): PublicNumberFragment{
            val args = Bundle()
            val fragment = PublicNumberFragment()
            fragment.arguments = args
            return fragment
        }
    }

    //fragment集合
    private var fragments: ArrayList<Fragment> = arrayListOf()

    //标题集合
    private var mDataList: ArrayList<String> = arrayListOf()

    override fun initView(savedInstanceState: Bundle?)  {
        mBind.includeLayout.viewpagerLinear.statusPadding()
        //初始化 viewpager2
        mBind.includeLayout.viewPager.init(this,fragments)
        //初始化 magic_indicator
        mBind.includeLayout.magicIndicator.bindViewPager2(mBind.includeLayout.viewPager,mDataList)
    }

    override fun lazyLoadData() {
        onLoadRetry()
    }

    override fun getLoadingView() = mBind.includeLayout.viewPager

    override fun onLoadRetry() {
        super.onLoadRetry()
        mViewModel.getPublicNumberTitle().obs(viewLifecycleOwner) {
            onSuccess {
                mDataList.addAll(it.map { it.name })
                it.forEach { classify ->
                    fragments.add(PublicChildFragment.newInstance(classify.id))
                }
                mBind.includeLayout.magicIndicator.navigator.notifyDataSetChanged()
                mBind.includeLayout.viewPager.adapter?.notifyDataSetChanged()
                mBind.includeLayout.viewPager.offscreenPageLimit = fragments.size
            }
        }
    }

}