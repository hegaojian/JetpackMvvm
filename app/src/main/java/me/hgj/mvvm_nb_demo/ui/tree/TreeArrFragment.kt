package me.hgj.mvvm_nb_demo.ui.tree

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.include_viewpager.*
import me.hgj.mvvm_nb_demo.R
import me.hgj.mvvm_nb_demo.app.base.BaseFragment
import me.hgj.mvvm_nb_demo.app.ext.bindViewPager2
import me.hgj.mvvm_nb_demo.app.ext.init
import me.hgj.mvvm_nb_demo.app.ext.setUiTheme
import me.hgj.mvvm_nb_demo.databinding.FragmentViewpagerBinding
import me.hgj.mvvm_nb_demo.ui.TestFragment

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/2
 * 描述　: 广场模块父Fragment管理三个子fragment
 */
class TreeArrFragment :BaseFragment<TreeViewModel, FragmentViewpagerBinding>() {

    var titleData = arrayListOf("广场","体系","导航")

    private var fragments : ArrayList<Fragment> = arrayListOf()

    init {
        fragments.add(PlazaFragment())
        fragments.add(SystemFragment())
        fragments.add(NavigationFragment())
    }

    override fun layoutId() = R.layout.fragment_viewpager

    override fun initView() {
        //初始化时设置顶部主题颜色
        appViewModel.appColor.value?.let { viewpager_linear.setBackgroundColor(it) }
    }

    override fun lazyLoadData() {
        //初始化viewpager2
        view_pager.init(this,fragments).offscreenPageLimit = fragments.size
        //初始化 magic_indicator
        magic_indicator.bindViewPager2(view_pager,mStringList = titleData)
    }

    override fun createObserver() {
        appViewModel.appColor.observe(this, Observer {
            setUiTheme(it, listOf(viewpager_linear))
        })
    }

}