package me.hgj.mvvm_nb_demo.ui.publicNumber

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.kingja.loadsir.core.LoadService
import kotlinx.android.synthetic.main.include_viewpager.*
import me.hgj.mvvm_nb.ext.parseState
import me.hgj.mvvm_nb_demo.R
import me.hgj.mvvm_nb_demo.app.base.BaseFragment
import me.hgj.mvvm_nb_demo.app.ext.*
import me.hgj.mvvm_nb_demo.app.weight.loadCallBack.ErrorCallback
import me.hgj.mvvm_nb_demo.app.weight.loadCallBack.LoadingCallback
import me.hgj.mvvm_nb_demo.data.bean.ClassifyResponse
import me.hgj.mvvm_nb_demo.databinding.FragmentViewpagerBinding

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/28
 * 描述　:聚合公众号
 */
class PublicNumberFragment : BaseFragment<PublicNumberViewModel, FragmentViewpagerBinding>() {

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>
    //fragment集合
    var fragments: ArrayList<Fragment> = arrayListOf()
    //标题集合
    var mDataList: ArrayList<ClassifyResponse> = arrayListOf()

    override fun layoutId() = R.layout.fragment_viewpager

    override fun initView() {
        //初始化时设置顶部主题颜色
        appViewModel.appColor.value?.let { viewpager_linear.setBackgroundColor(it) }
    }

    /**
     * 懒加载
     */
    override fun lazyLoadData() {
        //状态页配置
        loadsir = LoadServiceInit(view_pager) {
            //点击重试时触发的操作
            loadsir.showCallback(LoadingCallback::class.java)
            mViewModel.getPublicTitleData()
        }
        //初始化viewpager2
        view_pager.init(this,fragments)
        //初始化 magic_indicator
        magic_indicator.bindViewPager2(view_pager,mDataList)
        //请求标题数据
        mViewModel.getPublicTitleData()
    }

    override fun createObserver() {
        mViewModel.titleData.observe(this, Observer { data ->
            parseState(data, {
                mDataList.addAll(it)
                it.forEach { classify ->
                    fragments.add(PublicChildFragment.newInstance(classify.id))
                }
                magic_indicator.navigator.notifyDataSetChanged()
                view_pager.adapter?.notifyDataSetChanged()
                view_pager.offscreenPageLimit = fragments.size
                loadsir.showSuccess()
            }, {
                //请求项目标题失败
                loadsir.showCallback(ErrorCallback::class.java)
                loadsir.setErrorText(it.errorMsg)
            })
        })
        appViewModel.appColor.observe(this, Observer {
            setUiTheme(it, listOf(viewpager_linear,loadsir))
        })
    }
}