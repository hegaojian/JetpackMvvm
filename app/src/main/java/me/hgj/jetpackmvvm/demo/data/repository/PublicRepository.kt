package me.hgj.jetpackmvvm.demo.data.repository

import me.hgj.jetpackmvvm.demo.app.network.NetworkApi
import me.hgj.jetpackmvvm.demo.data.ApiPagerResponse
import me.hgj.jetpackmvvm.demo.data.ApiResponse
import me.hgj.jetpackmvvm.demo.data.bean.AriticleResponse
import me.hgj.jetpackmvvm.demo.data.bean.ClassifyResponse


/**
 * 作者　: hegaojian
 * 时间　: 2020/2/23
 * 描述　:
 */
class PublicRepository {

    //获取公众号标题数据
    suspend fun getTitleData(): ApiResponse<ArrayList<ClassifyResponse>> {
        return NetworkApi().service.getPublicTitle()
    }

    //根据公众号标题获取数据
    suspend fun getPublicData(
        pageNo: Int,
        cid: Int = 0
    ): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>> {
        return NetworkApi().service.getPublicData(pageNo, cid)
    }


}