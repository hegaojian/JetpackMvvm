package me.hgj.jetpackmvvm.demo.data.repository

import me.hgj.jetpackmvvm.demo.app.network.NetworkApi
import me.hgj.jetpackmvvm.demo.data.ApiPagerResponse
import me.hgj.jetpackmvvm.demo.data.ApiResponse
import me.hgj.jetpackmvvm.demo.data.bean.AriticleResponse
import me.hgj.jetpackmvvm.demo.data.bean.ClassifyResponse


/**
 * 作者　: hegaojian
 * 时间　: 2020/2/23
 * 描述　:
 */
class ProjectRepository {

    //获取项目标题数据
    suspend fun getBannData(): ApiResponse<ArrayList<ClassifyResponse>> {
        return NetworkApi.service.getProjecTitle()
    }

    //获取项目标题数据
    suspend fun getProjectData(
        pageNo: Int,
        cid: Int = 0,
        isNew: Boolean = false
    ): ApiResponse<ApiPagerResponse<ArrayList<AriticleResponse>>> {
        return if (isNew) {
            NetworkApi.service.getProjecNewData(pageNo)
        } else {
            NetworkApi.service.getProjecDataByType(pageNo, cid)
        }
    }


}