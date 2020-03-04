package me.hgj.mvvm_nb_demo.ui.project

import androidx.lifecycle.MutableLiveData
import me.hgj.mvvm_nb.ext.launchRequest
import me.hgj.mvvm_nb.state.ViewState
import me.hgj.mvvm_nb_demo.app.CollectViewModel
import me.hgj.mvvm_nb_demo.app.util.ListDataUiState
import me.hgj.mvvm_nb_demo.data.bean.AriticleResponse
import me.hgj.mvvm_nb_demo.data.bean.ClassifyResponse
import me.hgj.mvvm_nb_demo.data.repository.ProjectRepository

/**
 * 作者　: hegaojian
 * 时间　: 2020/2/28
 * 描述　:
 */
class ProjectViewModel : CollectViewModel() {

    //页码
    var pageNo = 1

    var titleData: MutableLiveData<ViewState<ArrayList<ClassifyResponse>>> = MutableLiveData()

    var projectDataState: MutableLiveData<ListDataUiState<AriticleResponse>> = MutableLiveData()

    private val repository: ProjectRepository by lazy { ProjectRepository() }

    fun getProjectTitleData() {
        launchRequest({ repository.getBannData() }, titleData)
    }

    fun getProjectData(isRefresh: Boolean, cid: Int, isNew: Boolean = false) {
        if (isRefresh) {
            pageNo = if (isNew) 0 else 1
        }
        launchRequestVM({repository.getProjectData(pageNo, cid, isNew)},{
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
            projectDataState.postValue(listDataUiState)
        },{
            //请求失败
            val listDataUiState = ListDataUiState(
                isSuccess = false,
                errMessage = it.errorMsg,
                isRefresh = isRefresh,
                listData = arrayListOf<AriticleResponse>()
            )
            projectDataState.postValue(listDataUiState)
        })
    }
}