package me.hgj.mvvm_nb_demo.ui.home

import androidx.lifecycle.MutableLiveData
import me.hgj.mvvm_nb.ext.launchRequest
import me.hgj.mvvm_nb.state.ViewState
import me.hgj.mvvm_nb_demo.app.CollectViewModel
import me.hgj.mvvm_nb_demo.app.util.ListDataUiState
import me.hgj.mvvm_nb_demo.data.bean.AriticleResponse
import me.hgj.mvvm_nb_demo.data.bean.BannerResponse
import me.hgj.mvvm_nb_demo.data.repository.HomeRepository

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/27
 * 描述　: 有两种回调方式：
 * 1.首页文章列表 将返回的数据放在Viewmodel中过滤包装给activity/fragment去使用
 * 2.首页轮播图 将返回的数据直接给activity/fragment去处理使用
 * 可以根据个人理解与喜好使用
 */
class HomeViewModel : CollectViewModel() {
    //页码 首页数据页码从0开始
    var pageNo: Int = 0
    //首页文章列表数据
    var homeDataState: MutableLiveData<ListDataUiState<AriticleResponse>> = MutableLiveData()
    //首页轮播图数据
    var bannerData: MutableLiveData<ViewState<ArrayList<BannerResponse>>> = MutableLiveData()
    //主页的请求数据仓库
    private val homeRepository: HomeRepository by lazy { HomeRepository() }

    /**
     * 获取首页文章列表数据
     * @param isRefresh 是否是刷新，即第一页
     */
    fun getHomeData(isRefresh: Boolean) {
        if (isRefresh) {
            pageNo = 0
        }
        launchRequestVM({ homeRepository.getHomeData(pageNo) }, {
            //请求成功
            pageNo++
            val listDataUiState = ListDataUiState(
                isSuccess = true,
                isRefresh = isRefresh,
                isEmpty = it.isEmpty(),
                hasMore = it.hasMore(),
                isFirstEmpty = isRefresh && it.isEmpty(),
                listData = it.datas
            )
            homeDataState.postValue(listDataUiState)
        },{
            //请求失败
            val listDataUiState = ListDataUiState(
                isSuccess = false,
                errMessage = it.errorMsg,
                isRefresh = isRefresh,
                listData = arrayListOf<AriticleResponse>()
            )
            homeDataState.postValue(listDataUiState)
        })
    }

    /**
     * 获取轮播图数据
     */
    fun getBannerData() {
        launchRequest({ homeRepository.getBannData() }, bannerData)
    }
}