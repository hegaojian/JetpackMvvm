package me.hgj.jetpackmvvm.demo.app.core.util

import androidx.lifecycle.MutableLiveData
import me.hgj.jetpackmvvm.core.data.postValue
import me.hgj.jetpackmvvm.demo.data.model.entity.UserInfo
import me.hgj.jetpackmvvm.ext.util.cacheNullable

/**
 * 作者　：hegaojian
 * 时间　：2025/9/29
 * 说明　：用户信息管理
 */
object UserManager {

    // 使用 Cache 委托 获取本地缓存的用户信息
    private var userCache : UserInfo? by cacheNullable()

    // 内存缓存（加 @Volatile 确保多线程安全）
    @Volatile
    private var cachedUser: UserInfo? = null

    //  监听
    private val userLiveData = MutableLiveData(userCache)

    var user: UserInfo?
        get() {
            if (cachedUser == null) {
                cachedUser = userCache
                userLiveData.postValue = cachedUser
            }
            return cachedUser
        }
        set(value) {
            cachedUser = value
            userCache = value
            userLiveData.postValue = value
        }

    /** 是否已登录 */
    val isLoggedIn: Boolean
        get() = user != null

    /** 保存用户信息 */
    fun saveUser(userInfo: UserInfo) {
        user = userInfo
    }

    /** 清空用户信息 */
    fun clearUser() {
        user = null
        userLiveData.postValue = user
    }

    /** 监听用户信息变化 */
    fun observeUser(): MutableLiveData<UserInfo?> = userLiveData
}
