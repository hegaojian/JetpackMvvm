package me.hgj.mvvm_nb_demo.data.repository

import me.hgj.mvvm_nb_demo.app.network.NetworkApi
import me.hgj.mvvm_nb_demo.data.ApiResponse

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/3
 * 描述　:
 */
class CollectRepository {

    suspend fun collect(id: Int): ApiResponse<Any?> {
        return NetworkApi.service.collect(id)
    }

    suspend fun uncollect(id: Int): ApiResponse<Any?> {
        return NetworkApi.service.uncollect(id)
    }
}