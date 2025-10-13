package me.hgj.jetpackmvvm.demo.app.core.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.drake.brv.utils.bindingAdapter
import me.hgj.jetpackmvvm.base.vm.BaseViewModel
import me.hgj.jetpackmvvm.core.data.ApiResult
import me.hgj.jetpackmvvm.core.data.obs
import me.hgj.jetpackmvvm.demo.databinding.IncludeRecyclerviewBinding
import me.hgj.jetpackmvvm.ext.util.refresh
import me.hgj.jetpackmvvm.ext.util.loadListError
import me.hgj.jetpackmvvm.ext.util.loadListSuccess

/**
 * 作者　：hegaojian
 * 时间　：2025/9/30
 * 说明　：不带分页的列表，写各种列表的时候发现全特么 是重复代码，所以这里直接封装了一个统一不分页列表，子类只要关心请求数据，和绑定对应的adapter就行了
 */
abstract class BaseListFragment<VM : BaseViewModel, T> : BaseFragment<VM, IncludeRecyclerviewBinding>() {

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): IncludeRecyclerviewBinding {
        return IncludeRecyclerviewBinding.inflate(inflater, container,false)
    }

    override fun initView(savedInstanceState: Bundle?) {
        mBind.refresh.refresh {
            getList(true)
        }.setEnableLoadMore(false)
        setupAdapter(mBind.rv)
    }

    override fun lazyLoadData() {
        onLoadRetry()
    }

    override fun onLoadRetry() {
        super.onLoadRetry()
        getList(isRefresh = true,loadingXml = true)
    }

    private fun getList(isRefresh: Boolean = true, loadingXml: Boolean = false) {
        provideRequest(isRefresh, loadingXml).obs(viewLifecycleOwner)  {
            onSuccess {
                loadListSuccess(it,mBind.rv.bindingAdapter,mBind.refresh,this@BaseListFragment)
            }
            onError {
                loadListError(it,mBind.refresh)
            }
        }
    }

    /**
     * 子类需要提供的请求逻辑
     */
    abstract fun provideRequest(isRefresh: Boolean, loadingXml: Boolean): LiveData<ApiResult<ArrayList<T>>>

    /**
     * 子类配置 Adapter
     */
    abstract fun setupAdapter(rv: RecyclerView)

}