package me.hgj.jetpackmvvm.demo.data.repository.request

import me.hgj.jetpackmvvm.demo.app.core.net.NetUrl
import me.hgj.jetpackmvvm.demo.data.model.entity.ApiPagerResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.CollectResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.CollectUrlResponse
import rxhttp.wrapper.coroutines.Await
import rxhttp.wrapper.param.RxHttp
import rxhttp.wrapper.param.toAwaitResponse

/**
 * 作者　：hegaojian
 * 时间　：2025/9/29
 * 描述　：收藏
 */
object CollectRepository {

    /**
     * 收藏文章
     */
    fun collectArticle(id: String): Await<Any> {
        return RxHttp.postForm(NetUrl.Collect.COLLECT_ARTICLE, id)
            .toAwaitResponse()
    }

    /**
     * 取消收藏文章
     */
    fun unCollectArticle(id: String): Await<Any> {
        return RxHttp.postForm(NetUrl.Collect.UN_COLLECT_ARTICLE, id)
            .toAwaitResponse()
    }

    /**
     * 取消收藏文章
     */
    fun unCollectOnCollect(id: String,originId: String): Await<Any> {
        return RxHttp.postForm(NetUrl.Collect.UN_COLLECT_ON_COLLECT,id)
            .add("originId", originId)
            .toAwaitResponse()
    }

    /**
     * 收藏网址
     */
    fun collectUrl(name: String, link: String): Await<CollectUrlResponse> {
        return RxHttp.postForm(NetUrl.Collect.COLLECT_URL)
            .add("name", name)
            .add("link", link)
            .toAwaitResponse()
    }

    /**
     * 取消收藏网址
     */
    fun unCollectUrl(id: String): Await<Any> {
        return RxHttp.postForm(NetUrl.Collect.UN_COLLECT_URL)
            .add("id", id)
            .toAwaitResponse()
    }

    /**
     * 获取收藏文章数据集合
     */
    fun getCollectArticleList(pageIndex: Int): Await<ApiPagerResponse<CollectResponse>> {
        return RxHttp.get(NetUrl.Collect.COLLECT_ARTICLE_LIST, pageIndex)
            .toAwaitResponse()
    }

    /**
     * 获取收藏网址数据集合
     */
    fun getCollectUrlList(pageIndex: Int): Await<ArrayList<CollectUrlResponse>> {
        return RxHttp.get(NetUrl.Collect.COLLECT_URL_LIST, pageIndex)
            .toAwaitResponse()
    }

}

