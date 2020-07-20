package me.hgj.jetpackmvvm.base

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/14
 * 描述　: 对于写BaseApp，其实我是拒绝的，但是需要提供一个很有用的功能--在Activity/fragment中获取Application级别的ViewModel
 * 所以才硬着头皮加的，如果你不想继承BaseApp，又想获取Application级别的ViewModel功能
 * 那么你可以复制该类的代码到你的自定义Application中去，然后可以自己写获取ViewModel的拓展函数即 :
 * GetViewModelExt类的getAppViewModel方法
 */

open class BaseApp : Application(), ViewModelStoreOwner {

    private lateinit var mAppViewModelStore: ViewModelStore

    private var mFactory: ViewModelProvider.Factory? = null

    override fun getViewModelStore(): ViewModelStore {
        return mAppViewModelStore
    }

    override fun onCreate() {
        super.onCreate()
        mAppViewModelStore = ViewModelStore()
    }

    /**
     * 获取一个全局的ViewModel
     */
    fun getAppViewModelProvider(): ViewModelProvider {
        return ViewModelProvider(this, this.getAppFactory())
    }

    private fun getAppFactory(): ViewModelProvider.Factory {
        if (mFactory == null) {
            mFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(this)
        }
        return mFactory as ViewModelProvider.Factory
    }
}