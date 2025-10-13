package me.hgj.jetpackmvvm.demo.data.repository.request

import me.hgj.jetpackmvvm.demo.app.core.net.NetUrl
import me.hgj.jetpackmvvm.demo.data.model.entity.ApiPagerResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.ArticleResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.BannerResponse
import rxhttp.wrapper.coroutines.Await
import rxhttp.wrapper.param.RxHttp
import rxhttp.wrapper.param.toAwaitResponse

/**
 * 作者　：hegaojian
 * 时间　：2025/9/29
 * 说明　：
 */
object HomeRepository {

    /**
     * 获取首页文章
     */
    fun getList(pageIndex: Int): Await<ApiPagerResponse<ArticleResponse>> {
        return RxHttp.get(NetUrl.Article.HOME_LIST, pageIndex)
            .toAwaitResponse()
    }

    /**
     * 获取首页banner
     */
    fun getBanner(): Await<ArrayList<BannerResponse>> {
        return RxHttp.get(NetUrl.Article.BANNER)
            .toAwaitResponse()
    }

    /**
     * 获取首页banner
     */
    fun getTopArticle(): Await<ArrayList<ArticleResponse>> {
        return RxHttp.get(NetUrl.Article.TOP)
            .toAwaitResponse()
    }
}