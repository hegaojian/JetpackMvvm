package me.hgj.jetpackmvvm.demo.ui.fragment.collect

import android.os.Bundle
import androidx.fragment.app.Fragment
import me.hgj.jetpackmvvm.demo.app.appViewModel
import me.hgj.jetpackmvvm.demo.viewmodel.request.RequestCollectViewModel
import me.hgj.jetpackmvvm.demo.app.base.BaseFragment
import me.hgj.jetpackmvvm.demo.app.ext.bindViewPager2
import me.hgj.jetpackmvvm.demo.app.ext.init
import me.hgj.jetpackmvvm.demo.app.ext.initClose
import me.hgj.jetpackmvvm.demo.databinding.FragmentCollectBinding
import me.hgj.jetpackmvvm.ext.nav

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/10
 * 描述　: 收藏
 */
class CollectFragment:BaseFragment<RequestCollectViewModel,FragmentCollectBinding>() {

    var titleData = arrayListOf("文章","网址")

    private var fragments : ArrayList<Fragment> = arrayListOf()

    init {
        fragments.add(CollectAriticleFragment())
        fragments.add(CollectUrlFragment())
    }
    override fun initView(savedInstanceState: Bundle?)  {
        //初始化时设置顶部主题颜色
        appViewModel.appColor.value?.let {mDatabind. collectViewpagerLinear.setBackgroundColor(it) }
        //初始化viewpager2
        mDatabind. collectViewPager.init(this,fragments)
        //初始化 magic_indicator
        mDatabind.collectMagicIndicator.bindViewPager2(mDatabind.collectViewPager,mStringList = titleData)
        mDatabind.includeToolbar. toolbar.initClose(){
            nav().navigateUp()
        }

    }
}