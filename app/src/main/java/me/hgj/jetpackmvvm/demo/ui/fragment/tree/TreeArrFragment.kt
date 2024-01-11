package me.hgj.jetpackmvvm.demo.ui.fragment.tree

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.appViewModel
import me.hgj.jetpackmvvm.demo.app.base.BaseFragment
import me.hgj.jetpackmvvm.demo.app.ext.bindViewPager2
import me.hgj.jetpackmvvm.demo.app.ext.init
import me.hgj.jetpackmvvm.demo.app.ext.setUiTheme
import me.hgj.jetpackmvvm.demo.app.util.CacheUtil
import me.hgj.jetpackmvvm.demo.databinding.FragmentViewpagerBinding
import me.hgj.jetpackmvvm.demo.viewmodel.state.TreeViewModel
import me.hgj.jetpackmvvm.ext.nav
import me.hgj.jetpackmvvm.ext.navigateAction

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/2
 * 描述　: 广场模块父Fragment管理四个子fragment
 */
class TreeArrFragment : BaseFragment<TreeViewModel, FragmentViewpagerBinding>() {

    var titleData = arrayListOf("广场", "每日一问", "体系", "导航")

    private var fragments: ArrayList<Fragment> = arrayListOf()

    init {
        fragments.add(PlazaFragment())
        fragments.add(AskFragment())
        fragments.add(SystemFragment())
        fragments.add(NavigationFragment())
    }

    override fun initView(savedInstanceState: Bundle?)  {
        //初始化时设置顶部主题颜色
        appViewModel.appColor.value?.let { setUiTheme(it, mDatabind.includeViewpager.viewpagerLinear) }
        mDatabind.includeViewpager.includeViewpagerToolbar.run {
            inflateMenu(R.menu.todo_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.todo_add -> {
                        if(CacheUtil.isLogin()){
                           nav().navigateAction(R.id.action_mainfragment_to_addAriticleFragment)
                        }else{
                            nav().navigateAction(R.id.action_to_loginFragment)
                        }
                    }
                }
                true
            }
        }
    }

    override fun lazyLoadData() {
        //初始化viewpager2
        mDatabind.includeViewpager.viewPager.init(this, fragments).offscreenPageLimit = fragments.size
        //初始化 magic_indicator
        mDatabind.includeViewpager.magicIndicator.bindViewPager2(mDatabind.includeViewpager.viewPager, mStringList = titleData) {
            if (it != 0) {
                mDatabind.includeViewpager.includeViewpagerToolbar.menu.clear()
            } else {
                mDatabind.includeViewpager. includeViewpagerToolbar.menu.hasVisibleItems().let { flag ->
                    if (!flag) mDatabind.includeViewpager.includeViewpagerToolbar.inflateMenu(R.menu.todo_menu)
                }
            }
        }
    }

    override fun createObserver() {
        appViewModel.appColor.observeInFragment(this, Observer {
            setUiTheme(it, mDatabind.includeViewpager.viewpagerLinear)
        })
    }

}