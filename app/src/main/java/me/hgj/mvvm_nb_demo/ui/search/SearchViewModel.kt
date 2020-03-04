package me.hgj.mvvm_nb_demo.ui.search

import androidx.lifecycle.MutableLiveData
import me.hgj.mvvm_nb.ext.launchRequest
import me.hgj.mvvm_nb.state.ViewState
import me.hgj.mvvm_nb_demo.app.CollectViewModel
import me.hgj.mvvm_nb_demo.data.ApiPagerResponse
import me.hgj.mvvm_nb_demo.data.bean.AriticleResponse
import me.hgj.mvvm_nb_demo.data.bean.SearchResponse
import me.hgj.mvvm_nb_demo.data.repository.SearchRepository

/**
 * 作者　: hegaojian
 * 时间　: 2020/2/29
 * 描述　:
 */
class SearchViewModel : CollectViewModel() {
    var pageNo = 0
    //搜索热词数据
    var hotData: MutableLiveData<ViewState<ArrayList<SearchResponse>>> = MutableLiveData()
    //搜索结果数据
    var seachResultData: MutableLiveData<ViewState<ApiPagerResponse<ArrayList<AriticleResponse>>>> = MutableLiveData()
    //搜索历史词数据
    var historyData: MutableLiveData<ArrayList<String>> = MutableLiveData()

    private  val repository :SearchRepository by lazy { SearchRepository() }

    /**
     * 获取热门数据
     */
    fun getHotData(){
        launchRequest({repository.getHotData()},hotData)
    }

    /**
     * 获取历史数据
     */
    fun getHistoryData(){
        //
        historyData.postValue(repository.getHistoryData())
    }

    /**
     * 根据字符串搜索结果
     */
    fun getSearchResultData(searchKey:String,isRefresh:Boolean){
        if(isRefresh){
            pageNo = 0
        }
        launchRequest({repository.getSearchResultData(pageNo,searchKey)},seachResultData)
    }
}