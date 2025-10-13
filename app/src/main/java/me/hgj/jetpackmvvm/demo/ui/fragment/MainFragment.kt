package me.hgj.jetpackmvvm.demo.ui.fragment

import android.os.Bundle
import me.hgj.jetpackmvvm.base.vm.BaseViewModel
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.core.base.BaseFragment
import me.hgj.jetpackmvvm.demo.databinding.FragmentMainBinding
import me.hgj.jetpackmvvm.demo.ui.adapter.MainAdapter

/**
 * 时间　: 2019/12/27
 * 作者　: hegaojian
 * 描述　: 项目主页Fragment
 */
class MainFragment : BaseFragment<BaseViewModel, FragmentMainBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        //设置适配器
        mBind.mainViewPager.adapter = MainAdapter(this)
        //设置缓存页面数量
        mBind.mainViewPager.offscreenPageLimit = mBind.mainViewPager.adapter!!.itemCount
        //禁止滑动
        mBind.mainViewPager.isUserInputEnabled = false
        //设置底部导航栏选择事件
        mBind.mainNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_main -> mBind.mainViewPager.setCurrentItem(0, false)
                R.id.menu_project -> mBind.mainViewPager.setCurrentItem(1, false)
                R.id.menu_system -> mBind.mainViewPager.setCurrentItem(2, false)
                R.id.menu_public -> mBind.mainViewPager.setCurrentItem(3, false)
                R.id.menu_me -> mBind.mainViewPager.setCurrentItem(4, false)
            }
            true
        }
    }
}