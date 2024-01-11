package me.hgj.jetpackmvvm.demo.ui.fragment.project

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.kingja.loadsir.core.LoadService
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.appViewModel
import me.hgj.jetpackmvvm.demo.app.base.BaseFragment
import me.hgj.jetpackmvvm.demo.app.ext.*
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.ErrorCallback
import me.hgj.jetpackmvvm.demo.databinding.FragmentViewpagerBinding
import me.hgj.jetpackmvvm.demo.viewmodel.request.RequestProjectViewModel
import me.hgj.jetpackmvvm.demo.viewmodel.state.ProjectViewModel
import me.hgj.jetpackmvvm.ext.parseState

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/28
 * 描述　:
 */
class ProjectFragment : BaseFragment<ProjectViewModel, FragmentViewpagerBinding>() {

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    //fragment集合
    var fragments: ArrayList<Fragment> = arrayListOf()

    //标题集合
    var mDataList: ArrayList<String> = arrayListOf()

    /** */
    private val requestProjectViewModel: RequestProjectViewModel by viewModels()

    override fun initView(savedInstanceState: Bundle?) {
        //状态页配置
        loadsir = loadServiceInit(mDatabind.includeViewpager.viewPager) {
            //点击重试时触发的操作
            loadsir.showLoading()
            requestProjectViewModel.getProjectTitleData()
        }
        //初始化viewpager2
        mDatabind.includeViewpager.viewPager.init(this, fragments)
        //初始化 magic_indicator
        mDatabind.includeViewpager.magicIndicator.bindViewPager2(mDatabind.includeViewpager.viewPager, mDataList)
        appViewModel.appColor.value?.let { setUiTheme(it, mDatabind.includeViewpager.viewpagerLinear, loadsir) }
    }

    /**
     * 懒加载
     */
    override fun lazyLoadData() {
        //设置界面 加载中
        loadsir.showLoading()
        //请求标题数据
        requestProjectViewModel.getProjectTitleData()
    }

    override fun createObserver() {
        requestProjectViewModel.titleData.observe(viewLifecycleOwner, Observer { data ->
            parseState(data, {
                mDataList.clear()
                fragments.clear()
                mDataList.add("最新项目")
                mDataList.addAll(it.map { it.name })
                fragments.add(ProjectChildFragment.newInstance(0, true))
                it.forEach { classify ->
                    fragments.add(ProjectChildFragment.newInstance(classify.id, false))
                }
                mDatabind.includeViewpager.magicIndicator.navigator.notifyDataSetChanged()
                mDatabind.includeViewpager.viewPager.adapter?.notifyDataSetChanged()
                mDatabind.includeViewpager.viewPager.offscreenPageLimit = fragments.size
                loadsir.showSuccess()
            }, {
                //请求项目标题失败
                loadsir.showCallback(ErrorCallback::class.java)
                loadsir.setErrorText(it.errorMsg)
            })
        })
        appViewModel.appColor.observeInFragment(this, Observer {
            setUiTheme(it, mDatabind.includeViewpager.viewpagerLinear, loadsir)
        })
    }
}