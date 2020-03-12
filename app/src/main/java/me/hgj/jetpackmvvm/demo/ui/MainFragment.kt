package me.hgj.jetpackmvvm.demo.ui

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_main.*
import me.hgj.jetpackmvvm.BaseViewModel
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.base.BaseFragment
import me.hgj.jetpackmvvm.demo.app.ext.init
import me.hgj.jetpackmvvm.demo.app.ext.setUiTheme
import me.hgj.jetpackmvvm.demo.app.util.SettingUtil
import me.hgj.jetpackmvvm.demo.databinding.FragmentMainBinding
import me.hgj.jetpackmvvm.demo.ui.home.HomeFragment
import me.hgj.jetpackmvvm.demo.ui.me.MeFragment
import me.hgj.jetpackmvvm.demo.ui.project.ProjectFragment
import me.hgj.jetpackmvvm.demo.ui.publicNumber.PublicNumberFragment
import me.hgj.jetpackmvvm.demo.ui.tree.TreeArrFragment

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/27
 * 描述　:项目主页Fragment
 */
class MainFragment : BaseFragment<BaseViewModel, FragmentMainBinding>() {

    var fragments = arrayListOf<Fragment>()
    private val homeFragment: HomeFragment by lazy { HomeFragment() }
    private val projectFragment: ProjectFragment by lazy { ProjectFragment() }
    private val treeArrFragment: TreeArrFragment by lazy { TreeArrFragment() }
    private val publicNumberFragment: PublicNumberFragment by lazy { PublicNumberFragment() }
    private val meFragment: MeFragment by lazy { MeFragment() }

    init {
        fragments.apply {
            add(homeFragment)
            add(projectFragment)
            add(treeArrFragment)
            add(publicNumberFragment)
            add(meFragment)
        }
    }

    override fun layoutId() = R.layout.fragment_main

    override fun initView() {
        //初始化viewpager2
        main_viewpager.init(this,fragments,false).run {
            offscreenPageLimit = fragments.size
        }
        //初始化 bottombar
        main_bottom.run {
            enableAnimation(false)
            enableShiftingMode(false)
            enableItemShiftingMode(false)
            appViewModel.appColor.value?.let {
                itemIconTintList = SettingUtil.getColorStateList(it)
                itemTextColor = SettingUtil.getColorStateList(it)
            }
            setTextSize(12F)
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.menu_main -> main_viewpager.setCurrentItem(0, false)
                    R.id.menu_project -> main_viewpager.setCurrentItem(1, false)
                    R.id.menu_system -> main_viewpager.setCurrentItem(2, false)
                    R.id.menu_public -> main_viewpager.setCurrentItem(3, false)
                    R.id.menu_me -> main_viewpager.setCurrentItem(4, false)
                }
                true
            }
        }
    }

    override fun lazyLoadData() {

    }

    override fun createObserver() {
        appViewModel.appColor.observe(viewLifecycleOwner, Observer {
            setUiTheme(it, listOf(main_bottom))
        })
    }
}