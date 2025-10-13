package me.hgj.jetpackmvvm.ext.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * 作者　：hegaojian
 * 时间　：2025/9/29
 * 说明　：GSON封装
 */
val gson: Gson by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { Gson() }

inline fun <reified T> String.toEntity(): T? {
    val type = object : TypeToken<T>() {}
    return try {
        gson.fromJson(this, type.type)
    } catch (e: Exception) {
        null
    }
}

inline fun <reified T> String.toArrayEntity(): ArrayList<T>? {
    val type = object : TypeToken<ArrayList<T>>() {}
    return try {
        gson.fromJson(this, type.type)
    } catch (e: Exception) {
        null
    }

}

fun Any?.toJsonStr(): String {
    return gson.toJson(this)
}