package me.hgj.jetpackmvvm.demo.viewmodel.request

import androidx.lifecycle.MutableLiveData
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.demo.app.network.apiService
import me.hgj.jetpackmvvm.demo.app.network.stateCallback.ListDataUiState
import me.hgj.jetpackmvvm.demo.app.network.stateCallback.UpdateUiState
import me.hgj.jetpackmvvm.demo.data.model.bean.TodoResponse
import me.hgj.jetpackmvvm.ext.request

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/11
 * 描述　:
 */
class RequestTodoViewModel : BaseViewModel() {

    var pageNo = 1

    //列表集合数据
    var todoDataState = MutableLiveData<ListDataUiState<TodoResponse>>()

    //删除的回调数据
    var delDataState = MutableLiveData<UpdateUiState<Int>>()

    //完成的回调数据
    var doneDataState = MutableLiveData<UpdateUiState<Int>>()

    //添加修改的回调数据
    var updateDataState = MutableLiveData<UpdateUiState<Int>>()


    fun getTodoData(isRefresh: Boolean) {
        if (isRefresh) {
            pageNo = 1
        }
        request({ apiService.getTodoData(pageNo) }, {
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
            todoDataState.value = listDataUiState
        }, {
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<TodoResponse>()
                )
            todoDataState.value = listDataUiState
        })
    }

    fun delTodo(id: Int, position: Int) {
        request({ apiService.deleteTodo(id) }, {
            val uistate = UpdateUiState(isSuccess = true, data = position)
            delDataState.value = uistate
        }, {
            val uistate = UpdateUiState(isSuccess = false, data = position, errorMsg = it.errorMsg)
            delDataState.value = uistate
        }, isShowDialog = true)
    }

    fun doneTodo(id: Int, position: Int) {
        request({ apiService.doneTodo(id, 1) }, {
            val uistate = UpdateUiState(isSuccess = true, data = position)
            doneDataState.value = uistate
        }, {
            val uistate = UpdateUiState(isSuccess = false, data = position, errorMsg = it.errorMsg)
            doneDataState.value = uistate
        }, isShowDialog = true)
    }

    fun addTodo(todoTitle: String, todoContent: String, todoTime: String, todoLeve: Int) {
        request({
            apiService.addTodo(todoTitle, todoContent, todoTime, 0, todoLeve)
        }, {
            val uistate = UpdateUiState(isSuccess = true, data = 0)
            updateDataState.value = uistate
        }, {
            val uistate = UpdateUiState(isSuccess = false, data = 0, errorMsg = it.errorMsg)
            updateDataState.value = uistate
        }, isShowDialog = true)
    }

    fun updateTodo(
        id: Int,
        todoTitle: String,
        todoContent: String,
        todoTime: String,
        todoLeve: Int
    ) {
        request({
            apiService.updateTodo(todoTitle, todoContent, todoTime, 0, todoLeve, id)
        }, {
            val uistate = UpdateUiState(isSuccess = true, data = 0)
            updateDataState.value = uistate
        }, {
            val uistate = UpdateUiState<Int>(isSuccess = false, errorMsg = it.errorMsg)
            updateDataState.value = uistate

        }, isShowDialog = true)
    }
}

