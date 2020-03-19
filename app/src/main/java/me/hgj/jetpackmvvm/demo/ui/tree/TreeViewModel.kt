package me.hgj.jetpackmvvm.demo.ui.tree

import androidx.lifecycle.MutableLiveData
import me.hgj.jetpackmvvm.demo.app.CollectViewModel
import me.hgj.jetpackmvvm.demo.app.network.stateCallback.ListDataUiState
import me.hgj.jetpackmvvm.demo.data.bean.AriticleResponse
import me.hgj.jetpackmvvm.demo.data.bean.NavigationResponse
import me.hgj.jetpackmvvm.demo.data.bean.SystemResponse
import me.hgj.jetpackmvvm.demo.data.repository.TreeRepository

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/2
 * 描述　:
 */
class TreeViewModel : CollectViewModel() {
    //页码，体系 广场的页码是从0开始的
    private var pageNo = 0
    //广场数据
    var plazaDataState: MutableLiveData<ListDataUiState<AriticleResponse>> = MutableLiveData()
    //每日一问数据
    var askDataState: MutableLiveData<ListDataUiState<AriticleResponse>> = MutableLiveData()
    //体系子栏目列表数据
    var systemChildDataState: MutableLiveData<ListDataUiState<AriticleResponse>> = MutableLiveData()
    //体系数据
    var systemDataState: MutableLiveData<ListDataUiState<SystemResponse>> = MutableLiveData()
    //导航数据
    var navigationDataState: MutableLiveData<ListDataUiState<NavigationResponse>> =
        MutableLiveData()


    //数据仓库
    private val repository: TreeRepository by lazy { TreeRepository() }

    /**
     * 获取广场数据
     */
    fun getPlazaData(isRefresh: Boolean) {
        if (isRefresh) {
            pageNo = 0
        }
        launchRequestVM({ repository.getPlazaData(pageNo) }, {
            //请求成功
            pageNo++
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isRefresh = isRefresh,
                    isEmpty = it.isEmpty(),
                    hasMore = it.hasMore(),
                    isFirstEmpty = isRefresh && it.isEmpty(),
                    listData = it.datas
                )
            plazaDataState.postValue(listDataUiState)
        }, {
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<AriticleResponse>()
                )
            plazaDataState.postValue(listDataUiState)
        })
    }

    /**
     * 获取每日一问数据
     */
    fun getAskData(isRefresh: Boolean) {
        if (isRefresh) {
            pageNo = 1 //每日一问的页码从1开始
        }
        launchRequestVM({ repository.getAskData(pageNo) }, {
            //请求成功
            pageNo++
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isRefresh = isRefresh,
                    isEmpty = it.isEmpty(),
                    hasMore = it.hasMore(),
                    isFirstEmpty = isRefresh && it.isEmpty(),
                    listData = it.datas
                )
            askDataState.postValue(listDataUiState)
        }, {
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<AriticleResponse>()
                )
            askDataState.postValue(listDataUiState)
        })
    }

    /**
     * 获取体系数据
     */
    fun getSystemData() {
        launchRequestVM({ repository.getSystemData() }, {
            //请求成功
            val dataUiState =
                ListDataUiState(
                    isSuccess = true,
                    listData = it
                )
            systemDataState.postValue(dataUiState)
        }, {
            //请求失败
            val dataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    listData = arrayListOf<SystemResponse>()
                )
            systemDataState.postValue(dataUiState)
        })
    }

    /**
     * 获取导航数据
     */
    fun getNavigationData() {
        launchRequestVM({ repository.getNavigationData() }, {
            //请求成功
            val dataUiState =
                ListDataUiState(
                    isSuccess = true,
                    listData = it
                )
            navigationDataState.postValue(dataUiState)
        }, {
            //请求失败
            val dataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    listData = arrayListOf<NavigationResponse>()
                )
            navigationDataState.postValue(dataUiState)
        })
    }

    /**
     * 获取体系子栏目列表数据
     */
    fun getSystemChildData(isRefresh: Boolean,cid:Int) {
        if (isRefresh) {
            pageNo = 0
        }
        launchRequestVM({ repository.getSystemChildData(pageNo,cid) }, {
            //请求成功
            pageNo++
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isRefresh = isRefresh,
                    isEmpty = it.isEmpty(),
                    hasMore = it.hasMore(),
                    isFirstEmpty = isRefresh && it.isEmpty(),
                    listData = it.datas
                )
            systemChildDataState.postValue(listDataUiState)
        }, {
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<AriticleResponse>()
                )
            plazaDataState.postValue(listDataUiState)
        })
    }


}