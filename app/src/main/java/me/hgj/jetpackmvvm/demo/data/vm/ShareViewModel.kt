package me.hgj.jetpackmvvm.demo.data.vm

import me.hgj.jetpackmvvm.base.vm.BaseViewModel
import me.hgj.jetpackmvvm.core.data.request
import me.hgj.jetpackmvvm.core.net.LoadingType
import me.hgj.jetpackmvvm.demo.app.core.net.NetUrl
import me.hgj.jetpackmvvm.demo.data.model.entity.ShareResponse
import rxhttp.wrapper.param.RxHttp
import rxhttp.wrapper.param.toAwaitResponse

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/11
 * 描述　:
 */
class ShareViewModel : BaseViewModel() {

    var pageNo = 1

    fun getShareArticleData(refresh: Boolean,loadingXml: Boolean) = request {
        if(refresh){
            pageNo = 0
        }
        onRequest {
            val data = RxHttp.get(NetUrl.Share.SELECT_SHARE_ARTICLE,pageNo).toAwaitResponse<ShareResponse>().await().shareArticles
            pageNo++
            data
        }
        loadingType = if(loadingXml) LoadingType.LOADING_XML else LoadingType.LOADING_NULL
    }

    fun delShareArticleData(id: String) = request {
        onRequest {
            RxHttp.postForm(NetUrl.Share.DELETE_SHARE_ARTICLE,id).toAwaitResponse<Any>().await()
        }
        loadingType = LoadingType.LOADING_DIALOG
    }

    fun addShareArticle(title: String,link: String) = request {
        onRequest {
            RxHttp.postForm(NetUrl.Share.ADD_SHARE_ARTICLE)
                .add("title",title)
                .add("link",link)
                .toAwaitResponse<Any>().await()
        }
        loadingType = LoadingType.LOADING_DIALOG
    }

}