package me.hgj.jetpackmvvm.demo.ui.fragment

import android.os.Bundle
import androidx.lifecycle.Observer
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.appViewModel
import me.hgj.jetpackmvvm.demo.app.base.BaseFragment
import me.hgj.jetpackmvvm.demo.app.ext.init
import me.hgj.jetpackmvvm.demo.app.ext.initMain
import me.hgj.jetpackmvvm.demo.app.ext.interceptLongClick
import me.hgj.jetpackmvvm.demo.app.ext.setUiTheme
import me.hgj.jetpackmvvm.demo.databinding.FragmentMainBinding
import me.hgj.jetpackmvvm.demo.viewmodel.state.MainViewModel


/**
 * 时间　: 2019/12/27
 * 作者　: hegaojian
 * 描述　:项目主页Fragment
 */
class MainFragment : BaseFragment<MainViewModel, FragmentMainBinding>() {

    override fun initView(savedInstanceState: Bundle?) {
        //初始化viewpager2
       mDatabind. mainViewpager.initMain(this)
        //初始化 bottomBar
        mDatabind.  mainBottom.init{
            when (it) {
                R.id.menu_main -> mDatabind. mainViewpager.setCurrentItem(0, false)
                R.id.menu_project -> mDatabind. mainViewpager.setCurrentItem(1, false)
                R.id.menu_system -> mDatabind. mainViewpager.setCurrentItem(2, false)
                R.id.menu_public ->mDatabind.  mainViewpager.setCurrentItem(3, false)
                R.id.menu_me -> mDatabind. mainViewpager.setCurrentItem(4, false)
            }
        }
        mDatabind. mainBottom.interceptLongClick(R.id.menu_main,R.id.menu_project,R.id.menu_system,R.id.menu_public,R.id.menu_me)
    }

    override fun createObserver() {
        appViewModel.appColor.observeInFragment(this, Observer {
            setUiTheme(it, mDatabind. mainBottom)
        })
    }
}