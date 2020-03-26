package me.hgj.jetpackmvvm.demo.data.repository

import me.hgj.jetpackmvvm.demo.app.network.NetworkApi
import me.hgj.jetpackmvvm.demo.data.ApiResponse
import me.hgj.jetpackmvvm.demo.data.bean.IntegralResponse

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 */
class MeRepository {
    suspend fun getIntegral(): ApiResponse<IntegralResponse> {
        return NetworkApi().service.getIntegral()
    }
}