package me.hgj.jetpackmvvm.demo.data.repository

import me.hgj.jetpackmvvm.demo.app.network.NetworkApi
import me.hgj.jetpackmvvm.demo.data.ApiResponse
import me.hgj.jetpackmvvm.demo.data.bean.ShareResponse

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/5
 * 描述　:
 */
class ShareRepository  {

    suspend fun getLookinfoById(id:Int, pageNo:Int): ApiResponse<ShareResponse> {
        return NetworkApi.service.getShareByidData(pageNo,id)
    }
}