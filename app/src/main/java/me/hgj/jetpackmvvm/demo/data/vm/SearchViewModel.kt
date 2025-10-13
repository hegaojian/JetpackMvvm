package me.hgj.jetpackmvvm.demo.data.vm

import me.hgj.jetpackmvvm.base.vm.BaseViewModel
import me.hgj.jetpackmvvm.core.data.request
import me.hgj.jetpackmvvm.demo.data.model.CacheConfig
import me.hgj.jetpackmvvm.demo.data.repository.request.SearchRepository
import me.hgj.jetpackmvvm.core.net.LoadingType

/**
 * 作者　：hegaojian
 * 时间　：2025/10/1
 * 描述　：搜索模块下的 ViewModel
 */
class SearchViewModel : BaseViewModel() {

    private var pageNo = 1

    val historyCacheData by lazy {  CacheConfig.historyCacheData.toMutableList() }

    fun updateHistoryCache(){
        CacheConfig.historyCacheData = historyCacheData.toSet()
    }

    /**
     * 获取热门搜索数据
     */
    fun getHotSearchData() = request {
        onRequest {
            SearchRepository.getHotSearchData().await()
        }
        loadingType = LoadingType.LOADING_XML
    }

    /**
     * 根据关键词搜索数据
     */
    fun getSearchDataByKey(
        searchKey: String,
        refresh: Boolean = true,
        loadingXml: Boolean = false
    ) = request {
        if (refresh) {
            pageNo = 0
        }
        onRequest {
            val data = SearchRepository.getSearchDataByKey(pageNo, searchKey).await()
            pageNo++
            data
        }
        loadingType = if (loadingXml) LoadingType.LOADING_XML else LoadingType.LOADING_NULL
    }

    /**
     * 查看他人的信息
     */
    fun getShareUserData(
        id: String,
        refresh: Boolean = true,
        loadingXml: Boolean = false
    ) = request {
        if (refresh) {
            pageNo = 1
        }
        onRequest {
            SearchRepository.getShareUserData(id, pageNo).await().also { pageNo++ }
        }
        loadingType = if (loadingXml) LoadingType.LOADING_XML else LoadingType.LOADING_NULL
    }

}