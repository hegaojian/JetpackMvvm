package me.hgj.jetpackmvvm.demo.data.repository

import me.hgj.jetpackmvvm.demo.app.network.NetworkApi
import me.hgj.jetpackmvvm.demo.data.ApiPagerResponse
import me.hgj.jetpackmvvm.demo.data.ApiResponse
import me.hgj.jetpackmvvm.demo.data.bean.TodoResponse

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/11
 * 描述　:
 */
class TodoRepository {

    suspend fun getTodoListData(pageNo: Int): ApiResponse<ApiPagerResponse<ArrayList<TodoResponse>>> {
        return NetworkApi().service.getTodoData(pageNo)
    }

    suspend fun delTodotData(id: Int): ApiResponse<Any?> {
        return NetworkApi().service.deleteTodo(id)
    }

    suspend fun doneTodotData(id: Int): ApiResponse<Any?> {
        //1完成
        return NetworkApi().service.doneTodo(id, 1)
    }

    suspend fun addTodotData(
        title: String,
        content: String,
        date: String,
        type: Int,
        priority: Int
    ): ApiResponse<Any?> {
        return NetworkApi().service.addTodo(title, content,date,type,priority)
    }
    suspend fun updateTodotData(
        title: String,
        content: String,
        date: String,
        type: Int,
        priority: Int,
        id: Int
    ): ApiResponse<Any?> {
        return NetworkApi().service.updateTodo(title, content,date,type,priority,id)
    }
}