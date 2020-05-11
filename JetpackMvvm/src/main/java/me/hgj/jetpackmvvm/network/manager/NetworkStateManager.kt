package me.hgj.jetpackmvvm.network.manager

import me.hgj.jetpackmvvm.callback.livedata.UnPeekLiveData

/**
 * 作者　: hegaojian
 * 时间　: 2020/5/2
 * 描述　: 网络变化管理者
 */
class NetworkStateManager private constructor() {

    val mNetworkStateCallback = UnPeekLiveData<NetState>()

    init {
        //mNetworkStateCallback值为null时或者,不为空但是没有网络时才能初始化设值有网络
        if (mNetworkStateCallback.value == null || !mNetworkStateCallback.value!!.isSuccess) {
            mNetworkStateCallback.postValue(NetState(isSuccess = true))
        }
    }

    companion object {
        val instance: NetworkStateManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkStateManager()
        }
    }
}