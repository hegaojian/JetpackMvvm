package me.hgj.jetpackmvvm.demo.data.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.delay
import me.hgj.jetpackmvvm.base.vm.BaseViewModel
import me.hgj.jetpackmvvm.core.data.postValue
import me.hgj.jetpackmvvm.core.data.request
import me.hgj.jetpackmvvm.demo.data.model.CacheConfig
import me.hgj.jetpackmvvm.demo.data.model.entity.BannerResponse
import me.hgj.jetpackmvvm.demo.data.repository.request.HomeRepository
import me.hgj.jetpackmvvm.core.net.LoadingType
import rxhttp.safeAsync

/**
 * 作者　：hegaojian
 * 时间　：2025/9/30
 * 描述　：
 */
class HomeViewModel : BaseViewModel() {

    private var pageIndex = 0

    /** banner 临时数据 */
    var bannerData:ArrayList<BannerResponse> = arrayListOf()

    /**
     * 获取列表数据
     * @param isRefresh Boolean 是否是刷新
     * @param loadingXml Boolean 请求时是否需要展示界面加载中loading
     */
    fun getHomeData(isRefresh: Boolean, loadingXml: Boolean = false) = request {
        if (isRefresh) {
            //是刷新 玩Android的这个接口pageIndex 是0 开始
            pageIndex = 0
        }
        onRequest {
            //下面3个接口并发请求 是同时请求的，等待全部请求完成
            //获取文章数据
            val listDeferred = HomeRepository.getList(pageIndex).safeAsync(this)
            //获取Banner数据 我这里给定的业务是 刷新的时候 bannerData 里面没有值 才去拿。
            val bannerDeferred = if (isRefresh && bannerData.isEmpty()) HomeRepository.getBanner().safeAsync(this) else null
            //获取置顶文章数据
            val topDeferred = if (isRefresh && CacheConfig.showTop) HomeRepository.getTopArticle().safeAsync(this) else null

            // 请求得到结果
            val listData = listDeferred.await()
            val banner = bannerDeferred?.await()
            val top = topDeferred?.await()
            //添加置顶数据
            top?.let {  listData.datas.addAll(0, it ) }
            //给Banner数据做临时缓存
            banner?.let { bannerData = it }
            //请求成功 页码+1
            pageIndex++
            //这里返回 data 出去
            listData
        }
        //loading类型，LOADING_XML 代表 当前所属的页面会跟随请求自动 加载，成功，失败
        loadingType = if (loadingXml) LoadingType.LOADING_XML else LoadingType.LOADING_NULL
    }

}