package me.hgj.jetpackmvvm.demo.ui.todo

import androidx.lifecycle.MutableLiveData
import me.hgj.jetpackmvvm.BaseViewModel
import me.hgj.jetpackmvvm.databind.IntObservableField
import me.hgj.jetpackmvvm.databind.StringObservableField
import me.hgj.jetpackmvvm.demo.app.network.stateCallback.ListDataUiState
import me.hgj.jetpackmvvm.demo.app.network.stateCallback.UpdateUiState
import me.hgj.jetpackmvvm.demo.data.bean.TodoResponse
import me.hgj.jetpackmvvm.demo.data.enums.TodoType
import me.hgj.jetpackmvvm.demo.data.repository.TodoRepository

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/11
 * 描述　:
 */
class TodoViewModel : BaseViewModel() {
    var pageNo = 1
    //列表集合数据
    var todoDataState = MutableLiveData<ListDataUiState<TodoResponse>>()
    //删除的回调数据
    var delDataState = MutableLiveData<UpdateUiState<Int>>()
    //完成的回调数据
    var doneDataState = MutableLiveData<UpdateUiState<Int>>()
    //添加修改的回调数据
    var updateDataState = MutableLiveData<UpdateUiState<Int>>()

    //标题
    var todoTitle = StringObservableField()
    //内容
    var todoContent = StringObservableField()
    //时间
    var todoTime = StringObservableField()
    //优先级
    var todoLeve = StringObservableField(TodoType.TodoType1.content)
    //优先级颜色
    var todoColor = IntObservableField(TodoType.TodoType1.color)

    //数据仓库
    private val repository: TodoRepository by lazy { TodoRepository() }

    fun getTodoData(isRefresh: Boolean) {
        if (isRefresh) {
            pageNo = 1
        }
        launchRequestVM({ repository.getTodoListData(pageNo) }, {
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
            todoDataState.postValue(listDataUiState)
        }, {
            //请求失败
            val listDataUiState =
                ListDataUiState(
                    isSuccess = false,
                    errMessage = it.errorMsg,
                    isRefresh = isRefresh,
                    listData = arrayListOf<TodoResponse>()
                )
            todoDataState.postValue(listDataUiState)
        })
    }

    fun delTodo(id: Int, position: Int) {
        launchRequestVM({ repository.delTodotData(id) }, {
            val uistate = UpdateUiState(isSuccess = true, data = position)
            delDataState.postValue(uistate)
        }, {
            val uistate = UpdateUiState(isSuccess = false, data = position, errorMsg = it.errorMsg)
            delDataState.postValue(uistate)
        }, isShowDialog = true)
    }
    fun doneTodo(id: Int, position: Int) {
        launchRequestVM({ repository.doneTodotData(id) }, {
            val uistate = UpdateUiState(isSuccess = true, data = position)
            doneDataState.postValue(uistate)
        }, {
            val uistate = UpdateUiState(isSuccess = false, data = position, errorMsg = it.errorMsg)
            doneDataState.postValue(uistate)
        }, isShowDialog = true)
    }
    fun addTodo() {
        launchRequestVM({
            repository.addTodotData(
                todoTitle.get(),
                todoContent.get(),
                todoTime.get(),
                0,
                TodoType.byValue(todoLeve.get()).type
            )
        }, {
            val uistate = UpdateUiState(isSuccess = true, data = 0)
            updateDataState.postValue(uistate)
        }, {
            val uistate = UpdateUiState(isSuccess = false, data = 0, errorMsg = it.errorMsg)
            updateDataState.postValue(uistate)
        }, isShowDialog = true)
    }

    fun updateTodo(id:Int) {
        launchRequestVM({
            repository.updateTodotData(
                todoTitle.get(),
                todoContent.get(),
                todoTime.get(),
                0,
                TodoType.byValue(todoLeve.get()).type,
                id
            )
        }, {
            val uistate = UpdateUiState(isSuccess = true, data = 0)
            updateDataState.postValue(uistate)
        }, {
            val uistate = UpdateUiState(isSuccess = false, data = 0, errorMsg = it.errorMsg)
            updateDataState.postValue(uistate)
        }, isShowDialog = true)
    }
}