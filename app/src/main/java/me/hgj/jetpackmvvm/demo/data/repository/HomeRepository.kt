package me.hgj.jetpackmvvm.demo.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import me.hgj.jetpackmvvm.demo.app.network.NetworkApi
import me.hgj.jetpackmvvm.demo.app.util.CacheUtil
import me.hgj.jetpackmvvm.demo.data.ApiPagerResponse
import me.hgj.jetpackmvvm.demo.data.ApiResponse
import me.hgj.jetpackmvvm.demo.data.bean.AriticleResponse
import me.hgj.jetpackmvvm.demo.data.bean.BannerResponse


/**
 * 作者　: hegaojian
 * 时间　: 2020/2/23
 * 描述　:
 */
class HomeRepository {
    //获取首页文章数据
    suspend fun getHomeData(pageNo: Int): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>> {
        //同时异步请求2个接口，请求完成后合并数据
        return withContext(Dispatchers.IO) {
            val data = async { NetworkApi().service.getAritrilList(pageNo) }
            //如果App配置打开了首页请求置顶文章，且是第一页
            if (CacheUtil.isNeedTop() && pageNo == 0) {
                val topData = async { getTopData() }
                data.await().data.datas.addAll(0, topData.await().data)
                data.await()
            } else {
                data.await()
            }
        }
    }

    //获取置顶文章数据
    private suspend fun getTopData(): ApiResponse<ArrayList<AriticleResponse>> {
        return NetworkApi().service.getTopAritrilList()
    }

    //获取轮播图数据
    suspend fun getBannData(): ApiResponse<ArrayList<BannerResponse>> {
        return NetworkApi().service.getBanner()
    }


}