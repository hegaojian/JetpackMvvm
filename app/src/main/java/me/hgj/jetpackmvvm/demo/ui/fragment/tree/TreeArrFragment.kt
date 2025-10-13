package me.hgj.jetpackmvvm.demo.ui.fragment.tree

import android.os.Bundle
import androidx.fragment.app.Fragment
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.core.base.BaseFragment
import me.hgj.jetpackmvvm.demo.app.core.ext.bindViewPager2
import me.hgj.jetpackmvvm.demo.app.core.ext.init
import me.hgj.jetpackmvvm.demo.app.core.ext.nav
import me.hgj.jetpackmvvm.demo.app.core.util.UserManager
import me.hgj.jetpackmvvm.demo.databinding.FragmentViewpagerBinding
import me.hgj.jetpackmvvm.demo.ui.fragment.MainFragmentDirections
import me.hgj.jetpackmvvm.demo.data.vm.TreeViewModel
import me.hgj.jetpackmvvm.demo.ui.activity.LoginActivity
import me.hgj.jetpackmvvm.ext.util.intent.openActivity
import me.hgj.jetpackmvvm.ext.util.statusPadding

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/2
 * 描述　: 广场模块父Fragment管理四个子fragment
 */
class TreeArrFragment : BaseFragment<TreeViewModel, FragmentViewpagerBinding>() {

    companion object{
        fun newInstance(): TreeArrFragment{
            val args = Bundle()
            val fragment = TreeArrFragment()
            fragment.arguments = args
            return fragment
        }
    }

    var titleData = arrayListOf("广场", "每日一问", "体系", "导航")

    private var fragments: ArrayList<Fragment> = arrayListOf()

    init {
        fragments.add(PlazaFragment())
        fragments.add(AskFragment())
        fragments.add(SystemFragment())
        fragments.add(NavigationFragment())
    }

    override fun initView(savedInstanceState: Bundle?)  {
        mBind.includeLayout.viewpagerLinear.statusPadding()
        mBind.includeLayout.includeViewpagerToolbar.run {
            inflateMenu(R.menu.todo_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.todo_add -> {
                        if(UserManager.isLoggedIn){
                           nav().navigate(MainFragmentDirections.toAddArticleFragment())
                        }else{
                            openActivity<LoginActivity>()
                        }
                    }
                }
                true
            }
        }
        //初始化viewpager
        mBind.includeLayout.viewPager.init(this, fragments).offscreenPageLimit = fragments.size
        //初始化 magic_indicator
        mBind.includeLayout.magicIndicator.bindViewPager2(mBind.includeLayout.viewPager, mStringList = titleData) {
            if (it != 0) {
                mBind.includeLayout.includeViewpagerToolbar.menu.clear()
            } else {
                mBind.includeLayout.includeViewpagerToolbar.menu.hasVisibleItems().let { flag ->
                    if (!flag) mBind.includeLayout.includeViewpagerToolbar.inflateMenu(R.menu.todo_menu)
                }
            }
        }
    }

}