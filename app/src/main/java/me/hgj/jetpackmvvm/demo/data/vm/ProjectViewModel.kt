package me.hgj.jetpackmvvm.demo.data.vm

import me.hgj.jetpackmvvm.base.vm.BaseViewModel
import me.hgj.jetpackmvvm.core.data.request
import me.hgj.jetpackmvvm.demo.data.repository.request.ProjectRepository
import me.hgj.jetpackmvvm.core.net.LoadingType

/**
 * 作者　: hegaojian
 * 时间　: 2020/2/28
 * 描述　:
 */
class ProjectViewModel : BaseViewModel() {

    private var pageNo = 1

    fun getProjectTitle()  = request {
        onRequest {
            ProjectRepository.getProjectTitle().await()
        }
        loadingType = LoadingType.LOADING_XML
    }

    fun getProjectData(refresh: Boolean = true,loadingXml: Boolean = false,cid:Int,new: Boolean = false)  = request {
        if(refresh){
            pageNo = if(new) 0 else 1
        }
        onRequest {
            ProjectRepository.getProjectData(pageNo, cid,new).await().also { pageNo++ }
        }
        loadingType = if(loadingXml) LoadingType.LOADING_XML else LoadingType.LOADING_NULL
    }
}