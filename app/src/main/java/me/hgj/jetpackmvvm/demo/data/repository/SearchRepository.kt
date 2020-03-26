package me.hgj.jetpackmvvm.demo.data.repository

import me.hgj.jetpackmvvm.demo.app.network.NetworkApi
import me.hgj.jetpackmvvm.demo.app.util.CacheUtil
import me.hgj.jetpackmvvm.demo.data.ApiPagerResponse
import me.hgj.jetpackmvvm.demo.data.ApiResponse
import me.hgj.jetpackmvvm.demo.data.bean.AriticleResponse
import me.hgj.jetpackmvvm.demo.data.bean.SearchResponse

/**
 * 作者　: hegaojian
 * 时间　: 2020/2/29
 * 描述　:
 */
class SearchRepository {

    suspend fun getHotData(): ApiResponse<ArrayList<SearchResponse>> {
        return NetworkApi().service.getSearchData()
    }

    suspend fun getSearchResultData(pageNo:Int,searchKey:String): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>> {
        return NetworkApi().service.getSearchDataByKey(pageNo,searchKey)
    }

    fun getHistoryData(): ArrayList<String> {
        return CacheUtil.getSearchHistoryData()
    }
}