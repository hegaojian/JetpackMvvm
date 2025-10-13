package me.hgj.jetpackmvvm.demo.data.repository.request

import me.hgj.jetpackmvvm.demo.app.core.net.NetUrl
import me.hgj.jetpackmvvm.demo.data.model.entity.ApiPagerResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.ArticleResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.ClassifyResponse
import rxhttp.wrapper.coroutines.Await
import rxhttp.wrapper.param.RxHttp
import rxhttp.wrapper.param.toAwaitResponse

/**
 * 作者　: hegaojian
 * 时间　: 2020/11/2
 * 描述　: 公众号
 */
object PublicNumberRepository {

    /**
     * 公众号分类标题
     */
    fun getPublicTitle(): Await<ArrayList<ClassifyResponse>> {
        return RxHttp.get(NetUrl.PublicNumber.PUBLIC_TITLE)
            .toAwaitResponse()
    }

    /**
     * 获取公众号数据
     * @param pageIndex 页码
     * @param id 公众号分类id
     */
    fun getPublicData(pageIndex: Int, id: Int): Await<ApiPagerResponse<ArticleResponse>> {
        return RxHttp.get(NetUrl.PublicNumber.PUBLIC_DATA, id,pageIndex)
            .toAwaitResponse()
    }

}

