package me.hgj.jetpackmvvm.demo.data.repository

import me.hgj.jetpackmvvm.demo.app.network.NetworkApi
import me.hgj.jetpackmvvm.demo.data.ApiPagerResponse
import me.hgj.jetpackmvvm.demo.data.ApiResponse
import me.hgj.jetpackmvvm.demo.data.bean.IntegralHistoryResponse
import me.hgj.jetpackmvvm.demo.data.bean.IntegralResponse

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/10
 * 描述　:积分数据仓库
 */
class IntegralRepository {

    suspend  fun getIntegralData(pageNo:Int):ApiResponse<ApiPagerResponse<ArrayList<IntegralResponse>>>{
       return   NetworkApi().service.getIntegralRank(pageNo)
    }

    suspend  fun getIntegralHistoryData(pageNo:Int):ApiResponse<ApiPagerResponse<ArrayList<IntegralHistoryResponse>>>{
        return   NetworkApi().service.getIntegralHistory(pageNo)
    }
}