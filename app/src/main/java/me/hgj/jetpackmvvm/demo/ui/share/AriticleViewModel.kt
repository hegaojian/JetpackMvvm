package me.hgj.jetpackmvvm.demo.ui.share

import androidx.lifecycle.MutableLiveData
import me.hgj.jetpackmvvm.BaseViewModel
import me.hgj.jetpackmvvm.databind.StringObservableField
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState
import me.hgj.jetpackmvvm.demo.app.network.stateCallback.ListDataUiState
import me.hgj.jetpackmvvm.demo.app.network.stateCallback.UpdateUiState
import me.hgj.jetpackmvvm.demo.data.bean.AriticleResponse
import me.hgj.jetpackmvvm.demo.data.repository.AriticleRepository

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/11
 * 描述　:
 */
class AriticleViewModel :BaseViewModel() {
    var pageNo  = 0

    var addData = MutableLiveData<ResultState<Any?>>()

    //分享文章标题
    var shareTitle = StringObservableField()
    //分享文章网址
    var shareUrl = StringObservableField()
    //分享文章的人
    var shareName = StringObservableField()

    //分享的列表集合数据
    var shareDataState = MutableLiveData<ListDataUiState<AriticleResponse>>()
    //删除分享文章回调数据
    var delDataState = MutableLiveData<UpdateUiState<Int>>()

    //数据仓库
    private val repository: AriticleRepository by lazy { AriticleRepository() }

    fun addAriticle(){
        request({repository.addAriticle(shareTitle.get(),shareUrl.get())},addData,true,"正在分享文章中...")
    }

    fun getShareData(isRefresh:Boolean){
        if(isRefresh){
            pageNo = 0
        }
        request({ repository.getShareAriticle(pageNo) }, {
            //请求成功
            pageNo++
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isRefresh = isRefresh,
                    isEmpty = it.shareArticles.isEmpty(),
                    hasMore = it.shareArticles.hasMore(),
                    isFirstEmpty = isRefresh && it.shareArticles.isEmpty(),
                    listData = it.shareArticles.datas
                )
            shareDataState.postValue(listDataUiState)
        }, {
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<AriticleResponse>()
                )
            shareDataState.postValue(listDataUiState)
        })
    }

    fun deleteShareData(id: Int,position:Int){
        request({repository.delShareAriticle(id)},{
            val updateUiState = UpdateUiState<Int>(isSuccess = true,data = position)
            delDataState.postValue(updateUiState)
        },{
            val updateUiState = UpdateUiState<Int>(isSuccess = false,data = position,errorMsg = it.errorMsg)
            delDataState.postValue(updateUiState)
        })
    }
}