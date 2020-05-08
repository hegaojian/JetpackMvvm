package me.hgj.jetpackmvvm.demo.data.repository.local

import me.hgj.jetpackmvvm.demo.app.util.CacheUtil

/**
 * 作者　: hegaojian
 * 时间　: 2020/5/4
 * 描述　:从本地获取的数据，可以从数据库，Sp等等中获取
 */
class LocalDataManger {

    companion object {
        val instance: LocalDataManger by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            LocalDataManger()
        }
    }

    /**
     * 获取本地存储的搜索数据
     */
    fun getHistoryData(): ArrayList<String> {
        return CacheUtil.getSearchHistoryData()
    }
}