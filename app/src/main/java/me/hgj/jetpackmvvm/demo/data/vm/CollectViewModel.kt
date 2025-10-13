package me.hgj.jetpackmvvm.demo.data.vm

import me.hgj.jetpackmvvm.base.vm.BaseViewModel
import me.hgj.jetpackmvvm.core.data.request
import me.hgj.jetpackmvvm.demo.data.repository.request.CollectRepository
import me.hgj.jetpackmvvm.core.net.LoadingType

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/3
 * 描述　: 收藏ViewModel
 */
open class CollectViewModel : BaseViewModel() {

    private var pageNo = 0

    /**
     * 收藏 文章
     *
     */
    fun collectArticle(id: String) = request {
        onRequest {
            CollectRepository.collectArticle(id).await()
        }
    }

    /**
     * 取消收藏 文章
     */
    fun unCollectArticle(id: String) = request {
        onRequest {
            CollectRepository.unCollectArticle(id).await()
        }
    }

    /**
     * 取消收藏 文章
     */
    fun unCollectOnCollect(id: String,originId: String) = request {
        onRequest {
            CollectRepository.unCollectOnCollect(id,originId).await()
        }
    }

    /**
     * 收藏 网址
     *
     */
    fun collectUrl(name: String, link: String) = request {
        onRequest {
            CollectRepository.collectUrl(name, link).await()
        }
    }

    /**
     * 取消收藏 网址
     */
    fun unCollectUrl(id: String) = request {
        onRequest {
            CollectRepository.unCollectUrl(id).await()
        }
    }

    /**
     * 获取收藏文章数据集合
     */
    fun getCollectArticleData(refresh: Boolean = true, loadingXml: Boolean = false) = request {
        if (refresh) {
            pageNo = 0
        }
        onRequest {
            CollectRepository.getCollectArticleList(pageNo).await().also { pageNo++ }
        }
        loadingType = if (loadingXml) LoadingType.LOADING_XML else LoadingType.LOADING_NULL
    }

    /**
     * 获取收藏网址数据集合
     */
    fun getCollectUrlData(refresh: Boolean = true, loadingXml: Boolean = false) = request {
        if (refresh) {
            pageNo = 0
        }
        onRequest {
            CollectRepository.getCollectUrlList(pageNo).await().also { pageNo++ }
        }
        loadingType = if (loadingXml) LoadingType.LOADING_XML else LoadingType.LOADING_NULL
    }

}