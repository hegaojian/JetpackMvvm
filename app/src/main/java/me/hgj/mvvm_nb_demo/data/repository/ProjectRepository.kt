package me.hgj.mvvm_nb_demo.data.repository

import me.hgj.mvvm_nb_demo.app.network.NetworkApi
import me.hgj.mvvm_nb_demo.data.ApiPagerResponse
import me.hgj.mvvm_nb_demo.data.ApiResponse
import me.hgj.mvvm_nb_demo.data.bean.AriticleResponse
import me.hgj.mvvm_nb_demo.data.bean.ClassifyResponse


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