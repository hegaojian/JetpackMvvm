package me.hgj.jetpackmvvm.callback.livedata.event

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import me.hgj.jetpackmvvm.ext.util.notNull
import java.util.*


/**
 * TODO：UnPeekLiveData 的存在是为了在 "重回二级页面" 的场景下，解决 "数据倒灌" 的问题。
 * 对 "数据倒灌" 的状况不理解的小伙伴，可参考《jetpack MVVM 精讲》的解析
 * <p>
 * https://juejin.im/post/5dafc49b6fb9a04e17209922
 * <p>
 * 本类参考了官方 SingleEventLive 的非入侵设计，
 * TODO：并创新性地引入了 "延迟清空消息" 的设计，
 * 如此可确保：
 * 1.一条消息能被多个观察者消费
 * 2.延迟期结束，不再能够收到旧消息的推送
 * 3.并且旧消息在延迟期结束时能从内存中释放，避免内存溢出等问题
 * <p>
 * Create by KunMinX at 19/9/23
 */

class EventLiveData<T> : MutableLiveData<T>() {
    private var isCleaning = false
    private var hasHandled = true
    private var isDelaying = false
    //消息的生存时长 默认1秒
    private var DELAY_TO_CLEAR_EVENT = 1000
    private val mTimer = Timer()
    private var mTask: TimerTask? = null
    //是否允许传入 null value
    private var isAllowNullValue = false
    private var isAllowToClear = true
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
     * 重写的 setValue 方法，默认不接收 null
     * 可通过 Builder 配置允许接收
     * 可通过 Builder 配置消息延时清理的时间
     *
     *
     * override setValue, do not receive null by default
     * You can configure to allow receiving through Builder
     * And also, You can configure the delay time of message clearing through Builder
     *
     * @param value
     */
    override fun setValue(value: T?) {
        if (!isAllowNullValue && value == null && !isCleaning) {
            return
        }
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
        if (isAllowToClear) {
            isCleaning = true;
            super.postValue(null);
        } else {
            hasHandled = true;
            isDelaying = false;
        }
    }

    class Builder<T> {
        /**
         * 消息的生存时长 默认1秒
         */
        private var eventSurvivalTime = 1000

        /**
         * 是否允许传入 null value
         */
        private var isAllowNullValue = false

        /**
         * 是否允许自动清理，默认 true
         */
        private var isAllowToClear = true

        fun setEventSurvivalTime(eventSurvivalTime: Int): Builder<T> {
            this.eventSurvivalTime = eventSurvivalTime
            return this
        }

        fun setAllowNullValue(allowNullValue: Boolean): Builder<T> {
            isAllowNullValue = allowNullValue
            return this
        }

        fun setAllowToClear(allowToClear: Boolean): Builder<T> {
            isAllowToClear = allowToClear
            return this
        }

        fun create(): EventLiveData<T> {
            val liveData: EventLiveData<T> = EventLiveData()
            liveData.DELAY_TO_CLEAR_EVENT = eventSurvivalTime
            liveData.isAllowNullValue = isAllowNullValue
            liveData.isAllowToClear = this.isAllowToClear;
            return liveData
        }
    }

}