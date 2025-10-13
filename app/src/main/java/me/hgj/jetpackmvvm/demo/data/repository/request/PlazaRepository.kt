package me.hgj.jetpackmvvm.demo.data.repository.request

import me.hgj.jetpackmvvm.demo.app.core.net.NetUrl
import me.hgj.jetpackmvvm.demo.data.model.entity.ApiPagerResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.ArticleResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.NavigationResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.SystemResponse
import rxhttp.wrapper.coroutines.Await
import rxhttp.wrapper.param.RxHttp
import rxhttp.wrapper.param.toAwaitResponse

/**
 * 作者　：hegaojian
 * 时间　：2025/9/30
 * 说明　：广场模块下的数据仓库
 */
object PlazaRepository {

    /**
     * 广场数据
     */
    fun getPlazaData(pageIndex: Int): Await<ApiPagerResponse<ArticleResponse>> {
        return RxHttp.get(NetUrl.Tree.PLAZA_DATA,pageIndex)
            .toAwaitResponse()
    }


    /**
     * 每日一问列表数据
     */
    fun getAskData(pageIndex: Int): Await<ApiPagerResponse<ArticleResponse>> {
        return RxHttp.get(NetUrl.Tree.ASK_DATA,pageIndex)
            .toAwaitResponse()
    }

    /**
     * 体系分类数据
     */
    fun getTreeTitleData(): Await<ArrayList<SystemResponse>> {
        return RxHttp.get(NetUrl.Tree.TREE_DATA)
            .toAwaitResponse()
    }

    /**
     * 体系分类下的列表数据
     */
    fun getTreeChildData(pageIndex: Int,cid: Int): Await<ApiPagerResponse<ArticleResponse>> {
        return RxHttp.get(NetUrl.Tree.TREE_CHILD_DATA,pageIndex)
            .add("cid",cid)
            .toAwaitResponse()
    }

    /**
     * 导航的列表数据
     */
    fun getNavigationData(): Await<ArrayList<NavigationResponse>> {
        return RxHttp.get(NetUrl.Tree.NAVIGATION)
            .toAwaitResponse()
    }

}