package me.hgj.jetpackmvvm.demo.ui.lookinfo

import androidx.lifecycle.MutableLiveData
import me.hgj.jetpackmvvm.databind.StringObservableField
import me.hgj.jetpackmvvm.demo.app.CollectViewModel
import me.hgj.jetpackmvvm.demo.app.network.stateCallback.ListDataUiState
import me.hgj.jetpackmvvm.demo.data.bean.AriticleResponse
import me.hgj.jetpackmvvm.demo.data.repository.ShareRepository
import me.hgj.jetpackmvvm.ext.request

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/4
 * 描述　:
 */
class LookInfoViewModel : CollectViewModel() {

    var pageNo = 1

    var name: StringObservableField = StringObservableField("--")

    var imageUrl: StringObservableField =
        StringObservableField("https://upload.jianshu.io/users/upload_avatars/9305757/93322613-ff1a-445c-80f9-57f088f7c1b1.jpg?imageMogr2/auto-orient/strip|imageView2/1/w/300/h/300/format/webp")

    var info: StringObservableField = StringObservableField()

    var shareListDataUistate: MutableLiveData<ListDataUiState<AriticleResponse>> = MutableLiveData()

    private val repository: ShareRepository by lazy { ShareRepository() }

    fun getLookinfo(id: Int, isRefresh: Boolean) {
        if (isRefresh) {
            pageNo = 1
        }
        request({ repository.getLookinfoById(id, pageNo) }, {
            //请求成功
            pageNo++
            name.set(it.coinInfo.username)
            info.set("积分 : ${it.coinInfo.coinCount}　排名 : ${it.coinInfo.rank}")
            val listDataUiState =
                ListDataUiState(
                    isSuccess = true,
                    isRefresh = it.shareArticles.isRefresh(),
                    isEmpty = it.shareArticles.isEmpty(),
                    hasMore = it.shareArticles.hasMore(),
                    isFirstEmpty = isRefresh && it.shareArticles.isEmpty(),
                    listData = it.shareArticles.datas
                )
            shareListDataUistate.postValue(listDataUiState)
        }, {
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<AriticleResponse>()
                )
            shareListDataUistate.postValue(listDataUiState)
        })
    }
}