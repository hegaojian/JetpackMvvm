package me.hgj.jetpackmvvm.demo.data.vm

import me.hgj.jetpackmvvm.base.vm.BaseViewModel
import me.hgj.jetpackmvvm.core.data.request
import me.hgj.jetpackmvvm.demo.data.repository.request.PublicNumberRepository
import me.hgj.jetpackmvvm.core.net.LoadingType

/**
 * 作者　: hegaojian
 * 时间　: 2020/2/29
 * 描述　:
 */
class PublicNumberViewModel : BaseViewModel() {

    private var pageNo = 1

    /**
     * 获取公众号标题
     */
    fun getPublicNumberTitle()  = request {
        onRequest{
            PublicNumberRepository.getPublicTitle().await()
        }
        loadingType = LoadingType.LOADING_XML
    }

    /**
     * 获取公众号数据
     */
    fun getPublicNumberData(refresh: Boolean = true,loadingXml: Boolean = false,id: Int)  = request {
        if(refresh){
            pageNo = 1
        }
        onRequest{
            PublicNumberRepository.getPublicData(pageNo, id).await().also { pageNo++ }
        }
        loadingType = if(loadingXml) LoadingType.LOADING_XML else LoadingType.LOADING_NULL
    }

}