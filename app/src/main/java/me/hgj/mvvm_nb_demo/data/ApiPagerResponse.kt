package me.hgj.mvvm_nb_demo.data

/**
 *  分页数据的基类
 */
data class ApiPagerResponse<T>(
    var datas: T,
    var curPage: Int,
    var offset: Int,
    var over: Boolean,
    var pageCount: Int,
    var size: Int,
    var total: Int
) {
    /**
     * 数据是否为空
     */
    fun isEmpty(): Boolean {

        return (datas as List<*>).size==0
    }
    /**
     * 是否为第一页
     */
    fun isFirst(): Boolean {
        //wanandroid 第一页该字段都为0
        return offset==0
    }

    /**
     * 是否还有更多数据
     */
    fun hasMore(): Boolean {
        return !over
    }
}