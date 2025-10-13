package me.hgj.jetpackmvvm.demo.ui.fragment.tree

import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import me.hgj.jetpackmvvm.demo.app.core.base.BaseFragment
import me.hgj.jetpackmvvm.demo.app.core.ext.bindViewPager2
import me.hgj.jetpackmvvm.demo.app.core.ext.init
import me.hgj.jetpackmvvm.demo.app.core.ext.initClose
import me.hgj.jetpackmvvm.demo.app.core.ext.nav
import me.hgj.jetpackmvvm.demo.data.model.entity.SystemResponse
import me.hgj.jetpackmvvm.demo.databinding.FragmentSystemBinding
import me.hgj.jetpackmvvm.demo.data.vm.TreeViewModel
import me.hgj.jetpackmvvm.ext.util.intent.bundle

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/4
 * 描述　:
 */
class SystemArrFragment : BaseFragment<TreeViewModel, FragmentSystemBinding>() {

    override val showTitle = true

    val data: SystemResponse by bundle()

    val index by bundle(0)

    private var fragments: ArrayList<Fragment> = arrayListOf()

    override fun initView(savedInstanceState: Bundle?)  {
        baseBinding.includeToolbar.toolbar.initClose(data.name) {
            nav().navigateUp()
        }
        //设置栏目标题居左显示
        (mBind.includeLayout.viewPager.layoutParams as LinearLayout.LayoutParams).gravity = Gravity.START

    }

    override fun lazyLoadData() {
        data.children.forEach {
            fragments.add(SystemChildFragment.newInstance(it.id))
        }
        //初始化viewpager2
        mBind.includeLayout.viewPager.init(this, fragments)
        //初始化 magic_indicator
        mBind.includeLayout.magicIndicator.bindViewPager2(mBind.includeLayout.viewPager, data.children.map { it.name })
        mBind.includeLayout.viewPager.setCurrentItem(index,false)
    }

}