package me.hgj.jetpackmvvm.demo.data.model.entity



data class ApiResponse<T>(val errorCode: Int, val errorMsg: String, val data: T)