package me.hgj.jetpackmvvm.network.manager

import me.hgj.jetpackmvvm.base.Ktx
import me.hgj.jetpackmvvm.callback.livedata.UnPeekLiveData
import me.hgj.jetpackmvvm.network.NetworkUtil

/**
 * 作者　: hegaojian
 * 时间　: 2020/5/2
 * 描述　: 网络变化管理者
 */
class NetworkStateManager private constructor() {

    val mNetworkStateCallback = UnPeekLiveData<NetState>()
    
    init {
        mNetworkStateCallback.postValue(NetState(isSuccess = NetworkUtil.isNetworkAvailable(Ktx.app)))
    }

    companion object {
        val instance: NetworkStateManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkStateManager()
        }
    }
}