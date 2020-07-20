package me.hgj.jetpackmvvm.callback.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import me.hgj.jetpackmvvm.ext.util.notNull
import java.util.*

/**
 * 防止数据倒灌的LiveData
 * @author : hgj
 * @date   : 2020/7/16
 */

class UnPeekLiveData<T> : MutableLiveData<T>() {
    private var isCleaning = false
    private var hasHandled = true
    private var isDelaying = false
    private val mTimer = Timer()
    private var mTask: TimerTask? = null
    private var DELAY_TO_CLEAR_EVENT = 1000

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, Observer {
            if (isCleaning) {
                hasHandled = true
                isDelaying = false
                isCleaning = false
                return@Observer
            }
            if (!hasHandled) {
                hasHandled = true
                isDelaying = true
                observer.onChanged(it)
            } else if (isDelaying) {
                observer.onChanged(it)
            }
        })
    }

    override fun observeForever(observer: Observer<in T>) {
        super.observeForever(Observer {
            if (isCleaning) {
                hasHandled = true
                isDelaying = false
                isCleaning = false
                return@Observer
            }
            if (!hasHandled) {
                hasHandled = true
                isDelaying = true
                observer.onChanged(it)
            } else if (isDelaying) {
                observer.onChanged(it)
            }
        })
    }

    /**
     * 重写的 setValue 方法
     * @param value
     */
    override fun setValue(value: T?) {
        hasHandled = false
        isDelaying = false
        super.setValue(value)
        mTask.notNull({
            it.cancel()
            mTimer.purge()
        })
        mTask = object : TimerTask() {
            override fun run() {
                clear()
            }
        }
        mTimer.schedule(mTask, DELAY_TO_CLEAR_EVENT.toLong())
    }

    private fun clear() {
        hasHandled = true
        isDelaying = false
    }
}