package me.hgj.jetpackmvvm.ext.util

import com.drake.brv.BindingAdapter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import me.hgj.jetpackmvvm.base.ui.BaseVmActivity
import me.hgj.jetpackmvvm.base.ui.BaseVmFragment
import me.hgj.jetpackmvvm.core.net.LoadStatusEntity
import me.hgj.jetpackmvvm.util.BasePage

/**
 * 作者　: hegaojian
 * 时间　: 2020/11/5
 * 描述　:
 */

/**
 * 下拉刷新
 * @receiver SmartRefreshLayout
 * @param refreshAction Function0<Unit>
 * @return SmartRefreshLayout
 */
fun SmartRefreshLayout.refresh(refreshAction: () -> Unit = {}): SmartRefreshLayout {
    this.setOnRefreshListener {
        refreshAction.invoke()
    }
    return this
}

/**
 * 上拉加载
 * @receiver SmartRefreshLayout
 * @param loadMoreAction Function0<Unit>
 * @return SmartRefreshLayout
 */
fun SmartRefreshLayout.loadMore(loadMoreAction: () -> Unit = {}): SmartRefreshLayout {
    this.setOnLoadMoreListener {
        loadMoreAction.invoke()
    }
    return this
}

/**
 * 加载列表成功
 * @param baseListNetEntity 列表数据
 * @param smartRefreshLayout 智能刷新布局
 * @param bindingAdapter BindingAdapter
 * @param uiHost ui宿主，可不传，类型传 BaseVmActivity 或者 BaseVmFragment ，
 */
fun <T> loadListSuccess(
    baseListNetEntity: BasePage<T>,
    bindingAdapter: BindingAdapter,
    smartRefreshLayout: SmartRefreshLayout,
    uiHost: Any? = null
) {
    //关闭头部刷新
    if (baseListNetEntity.isRefresh()) {
        if (baseListNetEntity.getPageData().isEmpty()) {
            // 是第一页并且第一页没有数据，那么可以给页面设置为空布局
            (uiHost as? BaseVmActivity<*>)?.showEmptyUi()
            (uiHost as? BaseVmFragment<*>)?.showEmptyUi()
        }
        //如果是第一页 那么设置最新数据替换
        bindingAdapter.models = baseListNetEntity.getPageData()
        smartRefreshLayout.finishRefresh()
    } else {
        //不是第一页，累加数据
        bindingAdapter.addModels(baseListNetEntity.getPageData())
    }
    //如果还有下一页数据 那么设置 smartRefreshLayout 还可以加载更多数据
    if (baseListNetEntity.hasMore()) {
        smartRefreshLayout.finishLoadMore()
        smartRefreshLayout.setNoMoreData(false)
    } else {
        //如果没有更多数据了，设置 smartRefreshLayout 加载完毕 没有更多数据
        smartRefreshLayout.finishLoadMoreWithNoMoreData()
    }
}

/**
 * 加载列表成功 这个是不带分页的，针对一次性加载完全部的列表数据的业务
 * @param listData 列表数据
 * @param smartRefreshLayout 智能刷新布局
 * @param uiHost ui宿主，可为空，类型传 BaseVmActivity 或者 BaseVmFragment
 */
fun <T> loadListSuccess(
    listData: ArrayList<T>,
    bindingAdapter: BindingAdapter,
    smartRefreshLayout: SmartRefreshLayout,
    uiHost: Any? = null
) {
    if (listData.isEmpty()) {
        // 是第一页并且第一页没有数据，那么可以给页面设置为空布局
        (uiHost as? BaseVmActivity<*>)?.showEmptyUi()
        (uiHost as? BaseVmFragment<*>)?.showEmptyUi()
    }
    //关闭头部刷新
    smartRefreshLayout.finishRefresh()
    bindingAdapter.models = listData
}


/**
 * 列表数据请求失败
 * @param loadStatus LoadStatusEntity
 * @param smartRefreshLayout SmartRefreshLayout
 */
fun loadListError(
    loadStatus: LoadStatusEntity,
    smartRefreshLayout: SmartRefreshLayout
) {
    when {
        smartRefreshLayout.isRefreshing -> {
            //普通下拉刷新，请求失败 就吐司错误信息，并关闭刷新
            smartRefreshLayout.finishRefresh()
        }
        else -> {
            // 加载更多，让 smartRefreshLayout 设置加载失败 并吐司错误信息
            smartRefreshLayout.finishLoadMore(false)
        }
    }
}