package me.hgj.jetpackmvvm.demo.ui.fragment.collect

import android.os.Bundle
import androidx.fragment.app.Fragment
import me.hgj.jetpackmvvm.demo.app.core.base.BaseFragment
import me.hgj.jetpackmvvm.demo.app.core.ext.bindViewPager2
import me.hgj.jetpackmvvm.demo.app.core.ext.init
import me.hgj.jetpackmvvm.demo.app.core.ext.initClose
import me.hgj.jetpackmvvm.demo.app.core.ext.nav
import me.hgj.jetpackmvvm.demo.databinding.FragmentCollectBinding
import me.hgj.jetpackmvvm.demo.data.vm.CollectViewModel
import me.hgj.jetpackmvvm.ext.util.statusPadding

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/10
 * 描述　: 收藏
 */
class CollectFragment: BaseFragment<CollectViewModel, FragmentCollectBinding>() {

    var titleData = arrayListOf("文章","网址")

    private var fragments : ArrayList<Fragment> = arrayListOf()

    init {
        fragments.add(CollectArticleFragment())
        fragments.add(CollectUrlFragment())
    }
    override fun initView(savedInstanceState: Bundle?)  {
        mBind.includeLayout.toolbar.statusPadding()
        mBind.collectViewpagerLinear.statusPadding()
        //初始化 collectViewPager
        mBind.collectViewPager.init(this,fragments)
        //初始化 collectMagicIndicator
        mBind.collectMagicIndicator.bindViewPager2(mBind.collectViewPager,mStringList = titleData)
        mBind.includeLayout.toolbar.initClose(){
            nav().navigateUp()
        }
    }
}