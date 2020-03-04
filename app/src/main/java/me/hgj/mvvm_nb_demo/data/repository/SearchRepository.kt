package me.hgj.mvvm_nb_demo.data.repository

import me.hgj.mvvm_nb_demo.app.network.NetworkApi
import me.hgj.mvvm_nb_demo.app.util.CacheUtil
import me.hgj.mvvm_nb_demo.data.ApiPagerResponse
import me.hgj.mvvm_nb_demo.data.ApiResponse
import me.hgj.mvvm_nb_demo.data.bean.AriticleResponse
import me.hgj.mvvm_nb_demo.data.bean.SearchResponse

/**
 * 作者　: hegaojian
 * 时间　: 2020/2/29
 * 描述　:
 */
class SearchRepository {

    suspend fun getHotData(): ApiResponse<ArrayList<SearchResponse>> {
        return NetworkApi.service.getSearchData()
    }

    suspend fun getSearchResultData(pageNo:Int,searchKey:String): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>> {
        return NetworkApi.service.getSearchDataByKey(pageNo,searchKey)
    }

    fun getHistoryData(): ArrayList<String> {
        return CacheUtil.getSearchHistoryData()
    }
}