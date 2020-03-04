package me.hgj.mvvm_nb_demo.app

import androidx.lifecycle.MutableLiveData
import me.hgj.mvvm_nb.BaseViewModel
import me.hgj.mvvm_nb_demo.data.repository.CollectRepository

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/3
 * 描述　: 专门为了收藏而写的Viewmodel，因为玩安卓很多界面都有收藏的功能，所有普通的Viewmodel
 * 需要收藏功能的话就可以继承它拥有收藏功能
 */
open class CollectViewModel : BaseViewModel() {

    private val collectRepositiory: CollectRepository by lazy { CollectRepository() }

    val collectUiState: MutableLiveData<CollectUiState> = MutableLiveData()

    /**
     * 收藏
     * 提醒一下，玩安卓的收藏 和取消收藏 成功后返回的data值为null，所以在CollectRepository中的返回值一定要加上 非空？
     * 不然会出错
     */
    fun collect(id: Int) {
        launchRequestVM({ collectRepositiory.collect(id) }, {
            val uiState = CollectUiState(isSuccess = true, collect = true, id = id)
            collectUiState.postValue(uiState)
        }, {
            val uiState =
                CollectUiState(isSuccess = false, collect = false, errorMsg = it.errorMsg, id = id)
            collectUiState.postValue(uiState)
        })
    }

    /**
     * 取消收藏
     * 提醒一下，玩安卓的收藏 和取消收藏 成功后返回的data值为null，所以在CollectRepository中的返回值一定要加上 非空？
     * 不然会出错
     */
    fun uncollect(id: Int) {
        launchRequestVM({ collectRepositiory.uncollect(id) }, {
            val uiState = CollectUiState(isSuccess = true, collect = false, id = id)
            collectUiState.postValue(uiState)
        }, {
            val uiState =
                CollectUiState(isSuccess = false, collect = true, errorMsg = it.errorMsg, id = id)
            collectUiState.postValue(uiState)
        })
    }

    data class CollectUiState(
        //请求是否成功
        var isSuccess: Boolean = true,
        //收藏
        var collect: Boolean = false,
        //收藏Id
        var id: Int = -1,
        //请求失败的错误信息
        var errorMsg: String = ""
    )
}