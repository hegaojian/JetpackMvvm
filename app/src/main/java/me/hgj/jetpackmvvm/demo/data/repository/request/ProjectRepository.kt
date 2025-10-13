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
 * 描述　: 项目
 */
object ProjectRepository {

    /**
     * 项目标题
     */
    fun getProjectTitle(): Await<ArrayList<ClassifyResponse>> {
        return RxHttp.get(NetUrl.Project.PROJECT_TITLE)
            .toAwaitResponse()
    }

    /**
     * 获取项目数据
     * @param pageIndex 页码
     * @param cid 项目分类id
     * @param new 是最新项目
     */
    fun getProjectData(pageIndex: Int,cid:Int,new: Boolean = false): Await<ApiPagerResponse<ArticleResponse>> {
        val url = if(new) NetUrl.Project.NEW_PROJECT_DATA else NetUrl.Project.PROJECT_DATA
        return RxHttp.get(url,pageIndex)
            .add("cid",cid,!new)
            .toAwaitResponse()
    }

}

