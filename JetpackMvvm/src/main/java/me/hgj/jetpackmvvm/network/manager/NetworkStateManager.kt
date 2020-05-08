package me.hgj.jetpackmvvm.network.manager

import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import me.hgj.jetpackmvvm.callback.livedata.UnPeekLiveData

/**
 * 作者　: hegaojian
 * 时间　: 2020/5/2
 * 描述　: 网络变化管理者
 */
class NetworkStateManager private constructor() : DefaultLifecycleObserver {

    val mNetworkStateCallback = UnPeekLiveData<NetState>()


    private var mNetworkStateReceive: NetworkStateReceive? = null

    override fun onResume(owner: LifecycleOwner) {
        mNetworkStateReceive = NetworkStateReceive()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        if (owner is AppCompatActivity) {
            owner.registerReceiver(mNetworkStateReceive, filter)
        } else if (owner is Fragment) {
            owner.requireActivity()
                .registerReceiver(mNetworkStateReceive, filter)
        }
    }

    init {
        //mNetworkStateCallback值为null时或者,不为空但是没有网络时才能初始化设值有网络
        if(mNetworkStateCallback.value==null||!mNetworkStateCallback.value!!.isSuccess){
            mNetworkStateCallback.postValue(NetState(isSuccess = true))
        }
    }
    override fun onPause(owner: LifecycleOwner) {
        if (owner is AppCompatActivity) {
            owner.unregisterReceiver(mNetworkStateReceive)
        } else if (owner is Fragment) {
            owner.requireActivity()
                .unregisterReceiver(mNetworkStateReceive)
        }
    }

    companion object {
        val instance: NetworkStateManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            NetworkStateManager()
        }
    }
}