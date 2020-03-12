package me.hgj.jetpackmvvm.demo.data.repository

import me.hgj.jetpackmvvm.demo.app.network.NetworkApi
import me.hgj.jetpackmvvm.demo.data.ApiResponse
import me.hgj.jetpackmvvm.demo.data.bean.ShareResponse

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/11
 * 描述　:
 */
class AriticleRepository {
    suspend fun addAriticle(title:String,content:String): ApiResponse<Any?> {
        return NetworkApi.service.addAriticle(title,content)
    }
    suspend fun getShareAriticle(pageNo:Int): ApiResponse<ShareResponse> {
        return NetworkApi.service.getShareData(pageNo)
    }
    suspend fun delShareAriticle(id:Int): ApiResponse<Any?> {
        return NetworkApi.service.deleteShareData(id)
    }
}