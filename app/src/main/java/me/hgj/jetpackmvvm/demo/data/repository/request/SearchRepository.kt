package me.hgj.jetpackmvvm.demo.data.repository.request

import me.hgj.jetpackmvvm.demo.app.core.net.NetUrl
import me.hgj.jetpackmvvm.demo.data.model.entity.ApiPagerResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.ArticleResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.SearchResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.ShareResponse
import rxhttp.wrapper.coroutines.Await
import rxhttp.wrapper.param.RxHttp
import rxhttp.wrapper.param.toAwaitResponse

/**
 * 作者　：hegaojian
 * 时间　：2025/9/30
 * 说明　：搜索
 */
object SearchRepository {

    /**
     * 获取热门搜索数据
     */
    fun getHotSearchData(): Await<ArrayList<SearchResponse>> {
        return RxHttp.get(NetUrl.Search.HOT_DATA)
            .toAwaitResponse()
    }


    /**
     * 根据关键词搜索数据
     */
    fun getSearchDataByKey(pageIndex: Int,searchKey: String): Await<ApiPagerResponse<ArticleResponse>> {
        return RxHttp.postForm(NetUrl.Search.SEARCH_DATA,pageIndex)
            .add("k",searchKey)
            .toAwaitResponse()
    }

    /**
     * 查看他人的信息
     */
    fun getShareUserData(id: String,pageIndex: Int): Await<ShareResponse> {
        return RxHttp.get(NetUrl.Search.SHARE_USER_DATA,id,pageIndex)
            .toAwaitResponse()
    }
}