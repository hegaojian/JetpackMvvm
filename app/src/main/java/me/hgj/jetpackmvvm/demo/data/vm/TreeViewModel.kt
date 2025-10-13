package me.hgj.jetpackmvvm.demo.data.vm

import me.hgj.jetpackmvvm.base.vm.BaseViewModel
import me.hgj.jetpackmvvm.core.data.request
import me.hgj.jetpackmvvm.demo.data.repository.request.PlazaRepository
import me.hgj.jetpackmvvm.core.net.LoadingType

/**
 * 作者　：hegaojian
 * 时间　：2025/9/30
 * 描述　：
 */
class TreeViewModel : BaseViewModel() {

    private var pageNo = 0

    /**
     * 获取广场数据
     */
    fun getPlazaData(refresh: Boolean = true, loadingXml: Boolean = false) = request {
        if (refresh) {
            pageNo = 0
        }
        onRequest {
            val data = PlazaRepository.getPlazaData(pageNo).await()
            pageNo++
            data
        }
        loadingType = if (loadingXml) LoadingType.LOADING_XML else LoadingType.LOADING_NULL
    }

    /**
     * 获取每日一问数据
     */
    fun getAskData(refresh: Boolean = true, loadingXml: Boolean = false) = request {
        if (refresh) {
            pageNo = 1
        }
        onRequest {
            val data = PlazaRepository.getAskData(pageNo).await()
            pageNo++
            data
        }
        loadingType = if (loadingXml) LoadingType.LOADING_XML else LoadingType.LOADING_NULL
    }

    /**
     * 获取体系分类数据
     */
    fun getTreeTitleData(loadingXml: Boolean = false) = request {
        onRequest {
            PlazaRepository.getTreeTitleData().await()
        }
        loadingType = if (loadingXml) LoadingType.LOADING_XML else LoadingType.LOADING_NULL
    }

    /**
     * 获取体系分类下的子数据
     */
    fun getTreeChildData(refresh: Boolean = true, loadingXml: Boolean = false, cid: Int) = request {
        if (refresh) {
            pageNo = 0
        }
        onRequest {
            PlazaRepository.getTreeChildData(pageNo, cid).await()
        }
        loadingType = if (loadingXml) LoadingType.LOADING_XML else LoadingType.LOADING_NULL
    }

    /**
     * 获取导航数据
     */
    fun getNavigationData(loadingXml: Boolean = false) = request {
        onRequest {
            PlazaRepository.getNavigationData().await()
        }
        loadingType = if (loadingXml) LoadingType.LOADING_XML else LoadingType.LOADING_NULL
    }
}