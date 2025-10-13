package me.hgj.jetpackmvvm.demo.app.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.drake.brv.utils.bindingAdapter
import me.hgj.jetpackmvvm.base.vm.BaseViewModel
import me.hgj.jetpackmvvm.core.data.ApiResult
import me.hgj.jetpackmvvm.core.data.obs
import me.hgj.jetpackmvvm.demo.app.event.EventViewModel
import me.hgj.jetpackmvvm.demo.app.core.ext.collectRefresh
import me.hgj.jetpackmvvm.demo.app.core.ext.refreshArticleData
import me.hgj.jetpackmvvm.demo.app.core.ext.toggleCollect
import me.hgj.jetpackmvvm.demo.app.core.util.UserManager
import me.hgj.jetpackmvvm.demo.data.model.entity.ApiPagerResponse
import me.hgj.jetpackmvvm.demo.data.vm.CollectViewModel
import me.hgj.jetpackmvvm.demo.databinding.IncludeRecyclerviewBinding
import me.hgj.jetpackmvvm.demo.ui.adapter.articleAdapter
import me.hgj.jetpackmvvm.ext.lifecycle.getViewModel
import me.hgj.jetpackmvvm.ext.lifecycle.scopeLife
import me.hgj.jetpackmvvm.ext.util.loadListError
import me.hgj.jetpackmvvm.ext.util.loadListSuccess
import me.hgj.jetpackmvvm.ext.util.loadMore
import me.hgj.jetpackmvvm.ext.util.refresh
import me.hgj.jetpackmvvm.ext.view.divider
import me.hgj.jetpackmvvm.ext.view.vertical
import me.hgj.jetpackmvvm.util.decoration.DividerOrientation

/**
 * 作者　：hegaojian
 * 时间　：2025/9/30
 * 说明　：因为玩安卓这个项目其实大部分页面都是带文章的，全是重复的代码，所以这里直接封装了页面+adapter，只需要关心 请求数据就好了
 */
abstract class BaseArticleListFragment<VM : BaseViewModel,T> : BaseFragment<VM, IncludeRecyclerviewBinding>() {

    /** 是否是首页 */
    open val isHome = false

    // 收藏 viewmodel 公共的
    protected val collectVm: CollectViewModel by getViewModel()

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): IncludeRecyclerviewBinding {
        return IncludeRecyclerviewBinding.inflate(inflater, container,false)
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBind.refresh.refresh {
            getList(true)
        }.loadMore {
            getList(false)
        }
        mBind.rv.vertical().divider {
            orientation = DividerOrientation.VERTICAL //分割线方向
            startVisible = !isHome //首页有Banner 所以这里 顶部不加分割线了
            endVisible = true
            setDivider(8, true) //分割线高度
        }.articleAdapter(
            showTag = isHome,
            onCollectClick = { article, isChecked, result ->
                collectVm.toggleCollect(this, article.id, isChecked, result)
            },
            onBannerInit = { it.registerLifecycleObserver(lifecycle) }
        )
    }

    override fun lazyLoadData() {
        onLoadRetry()
    }

    override fun onLoadRetry() {
        super.onLoadRetry()
        getList(isRefresh = true, loadingXml = true)
    }

    private fun getList(isRefresh: Boolean = true, loadingXml: Boolean = false) {
        provideRequest(isRefresh, loadingXml).obs(viewLifecycleOwner)  {
            onSuccess {
                loadListSuccess(it, mBind.rv.bindingAdapter,mBind.refresh,this@BaseArticleListFragment)
                // 适配首页Banner加的方法
                homeBanner()
            }
            onError {
                loadListError(it, mBind.refresh)
            }
        }
    }

    override fun createObserver() {
        // 收藏变化监听
        EventViewModel.collectEvent.observe(viewLifecycleOwner, Observer {
            mBind.rv.collectRefresh(it)
        })
        UserManager.observeUser().observe(viewLifecycleOwner){
            mBind.rv.refreshArticleData(it)
        }
    }

    /**
     * 子类需要提供的请求逻辑
     */
    abstract fun provideRequest(
        isRefresh: Boolean,
        loadingXml: Boolean
    ): LiveData<ApiResult<ApiPagerResponse<T>>>

    open fun homeBanner() {}

}