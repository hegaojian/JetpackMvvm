package me.hgj.jetpackmvvm.callback.livedata.event

import android.annotation.SuppressLint
import androidx.annotation.MainThread
import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.internal.SafeIterableMap
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

/**
 * @author : hgj
 * @date   : 2020/7/3
 */

@SuppressLint("RestrictedApi")
abstract class EventBaseLiveData<T> {
    private val mDataLock = Any()
    private val mObservers = SafeIterableMap<Observer<T>, ObserverWrapper>()
    private var mActiveCount = 0

    @Volatile
    private var mData: Any

   @Volatile
    var mPendingData = NOT_SET
    var version: Int
        private set
    private var mDispatchingValue = false
    private var mDispatchInvalidated = false
    private val mPostValueRunnable = Runnable {
        var newValue: Any
        synchronized(mDataLock) {
            newValue = mPendingData
            mPendingData = NOT_SET
        }
        setEvent(newValue as Event<T>)
    }

    /**
     * Creates a LiveData initialized with the given `value`.
     *
     * @param value initial value
     */
    constructor(value: Event<T>) {
        mData = value
        version = START_VERSION + 1
    }

    /**
     * Creates a LiveData with no value assigned to it.
     */
    constructor() {
        mData = NOT_SET
        version = START_VERSION
    }

    private fun considerNotify(observer: ObserverWrapper) {
        if (!observer.mActive) {
            return
        }
        // Check latest state b4 dispatch. Maybe it changed state but we didn't get the event yet.
        //
        // we still first check observer.active to keep it as the entrance for events. So even if
        // the observer moved to an active state, if we've not received that event, we better not
        // notify for a more predictable notification order.
        if (!observer.shouldBeActive()) {
            observer.activeStateChanged(false)
            return
        }
        if (observer.mLastVersion >= version) {
            return
        }
        observer.mLastVersion = version
        val data = (mData as Event<T>).getContent()
        if (data != null) {
            observer.mEventObserver.onChanged(data)
        }
    }

    fun dispatchingValue(initiator: ObserverWrapper?) {
        var initiator = initiator
        if (mDispatchingValue) {
            mDispatchInvalidated = true
            return
        }
        mDispatchingValue = true
        do {
            mDispatchInvalidated = false
            if (initiator != null) {
                considerNotify(initiator)
                initiator = null
            } else {
                val iterator: Iterator<Map.Entry<Observer<T>, ObserverWrapper>> = mObservers.iteratorWithAdditions()
                while (iterator.hasNext()) {
                    considerNotify(iterator.next().value)
                    if (mDispatchInvalidated) {
                        break
                    }
                }
            }
        } while (mDispatchInvalidated)
        mDispatchingValue = false
    }

    /**
     * Adds the given observer to the observers list within the lifespan of the given
     * owner. The events are dispatched on the main thread. If LiveData already has data
     * set, it will be delivered to the observer.
     *
     *
     * The observer will only receive events if the owner is in [Lifecycle.State.STARTED]
     * or [Lifecycle.State.RESUMED] state (active).
     *
     *
     * If the owner moves to the [Lifecycle.State.DESTROYED] state, the observer will
     * automatically be removed.
     *
     *
     * When data changes while the `owner` is not active, it will not receive any updates.
     * If it becomes active again, it will receive the last available data automatically.
     *
     *
     * LiveData keeps a strong reference to the observer and the owner as long as the
     * given LifecycleOwner is not destroyed. When it is destroyed, LiveData removes references to
     * the observer &amp; the owner.
     *
     *
     * If the given owner is already in [Lifecycle.State.DESTROYED] state, LiveData
     * ignores the call.
     *
     *
     * If the given owner, observer tuple is already in the list, the call is ignored.
     * If the observer is already in the list with another owner, LiveData throws an
     * [IllegalArgumentException].
     *
     * @param owner         The LifecycleOwner which controls the observer
     * @param eventObserver The observer that will receive the events
     */
    @MainThread
    fun observe(owner: LifecycleOwner, eventObserver: Observer<T>) {
        assertMainThread("observe")
        if (owner.lifecycle.currentState == Lifecycle.State.DESTROYED) {
            // ignore
            return
        }
        val wrapper = LifecycleBoundObserver(owner, eventObserver)
        val existing = mObservers.putIfAbsent(eventObserver, wrapper)
        require(!(existing != null && !existing.isAttachedTo(owner))) {
            ("Cannot add the same observer"
                    + " with different lifecycles")
        }
        if (existing != null) {
            return
        }
        owner.lifecycle.addObserver(wrapper)
    }

    /**
     * Adds the given observer to the observers list. This call is similar to
     * [EventBaseLiveData.observe] with a LifecycleOwner, which
     * is always active. This means that the given observer will receive all events and will never
     * be automatically removed. You should manually call [.removeObserver] to stop
     * observing this LiveData.
     * While LiveData has one of such observers, it will be considered
     * as active.
     *
     *
     * If the observer was already added with an owner to this LiveData, LiveData throws an
     * [IllegalArgumentException].
     *
     * @param eventObserver The observer that will receive the events
     */
    @MainThread
    fun observeForever(eventObserver: Observer<T>) {
        assertMainThread("observeForever")
        val wrapper = AlwaysActiveObserver(eventObserver)
        val existing = mObservers.putIfAbsent(eventObserver, wrapper)
        require(existing !is LifecycleBoundObserver) {
            ("Cannot add the same observer"
                    + " with different lifecycles")
        }
        if (existing != null) {
            return
        }
        wrapper.activeStateChanged(true)
    }

    /**
     * Removes the given observer from the observers list.
     *
     * @param eventObserver The Observer to receive events.
     */
    @MainThread
    fun removeObserver(eventObserver: Observer<T>) {
        assertMainThread("removeObserver")
        val removed = mObservers.remove(eventObserver) ?: return
        removed.detachObserver()
        removed.activeStateChanged(false)
    }

    /**
     * Removes all observers that are tied to the given [LifecycleOwner].
     *
     * @param owner The `LifecycleOwner` scope for the observers to be removed.
     */
    @MainThread
    fun removeObservers(owner: LifecycleOwner) {
        assertMainThread("removeObservers")
        for ((key, value1) in mObservers) {
            if (value1.isAttachedTo(owner)) {
                removeObserver(key)
            }
        }
    }

    /**
     * Posts a task to a main thread to set the given value. So if you have a following code
     * executed in the main thread:
     * <pre class="prettyprint">
     * liveData.postValue("a");
     * liveData.setValue("b");
    </pre> *
     * The value "b" would be set at first and later the main thread would override it with
     * the value "a".
     *
     *
     * If you called this method multiple times before a main thread executed a posted task, only
     * the last value would be dispatched.
     *
     * @param value The new value
     */
    protected fun postEvent(value: Event<T>) {
        var postTask: Boolean
        synchronized(mDataLock) {
            postTask = mPendingData === NOT_SET
            mPendingData = value
        }
        if (!postTask) {
            return
        }
        ArchTaskExecutor.getInstance().postToMainThread(mPostValueRunnable)
    }

    /**
     * Sets the value. If there are active observers, the value will be dispatched to them.
     *
     *
     * This method must be called from the main thread. If you need set a value from a background
     * thread, you can use [.postValue]
     *
     * @param value The new value
     */
    @MainThread
    protected fun setEvent(value: Event<T>) {
        assertMainThread("setValue")
        version++
        mData = value
        dispatchingValue(null)
    }

    /**
     * Returns the current value.
     * Note that calling this method on a background thread does not guarantee that the latest
     * value set will be received.
     *
     * @return the current value
     */
    val value: Event<T>?
        get() {
            val data = mData
            return if (data !== NOT_SET) {
                data as Event<T>
            } else null
        }

    /**
     * Called when the number of active observers change to 1 from 0.
     *
     *
     * This callback can be used to know that this LiveData is being used thus should be kept
     * up to date.
     */
    protected fun onActive() {}

    /**
     * Called when the number of active observers change from 1 to 0.
     *
     *
     * This does not mean that there are no observers left, there may still be observers but their
     * lifecycle states aren't [Lifecycle.State.STARTED] or [Lifecycle.State.RESUMED]
     * (like an Activity in the back stack).
     *
     *
     * You can check if there are observers via [.hasObservers].
     */
    protected fun onInactive() {}

    /**
     * Returns true if this LiveData has observers.
     *
     * @return true if this LiveData has observers
     */
    fun hasObservers(): Boolean {
        return mObservers.size() > 0
    }

    /**
     * Returns true if this LiveData has active observers.
     *
     * @return true if this LiveData has active observers
     */
    fun hasActiveObservers(): Boolean {
        return mActiveCount > 0
    }

    private inner class LifecycleBoundObserver(private val mOwner: LifecycleOwner, eventObserver: Observer<T>) : ObserverWrapper(eventObserver), LifecycleEventObserver {
        override fun shouldBeActive(): Boolean {
            return mOwner.lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)
        }

        override fun onStateChanged(source: LifecycleOwner,
                                    event: Lifecycle.Event) {
            if (mOwner.lifecycle.currentState == Lifecycle.State.DESTROYED) {
                removeObserver(mEventObserver)
                return
            }
            activeStateChanged(shouldBeActive())
        }

        override fun isAttachedTo(owner: LifecycleOwner): Boolean {
            return mOwner === owner
        }

        override fun detachObserver() {
            mOwner.lifecycle.removeObserver(this)
        }

    }

    abstract inner class ObserverWrapper internal constructor(eventObserver: Observer<T>) {
        val mEventObserver: Observer<T> = eventObserver
        var mActive = false
        var mLastVersion = START_VERSION
        abstract fun shouldBeActive(): Boolean
        open fun isAttachedTo(owner: LifecycleOwner): Boolean {
            return false
        }

        open fun detachObserver() {}
        fun activeStateChanged(newActive: Boolean) {
            if (newActive == mActive) {
                return
            }
            // immediately set active state, so we'd never dispatch anything to inactive
            // owner
            mActive = newActive
            val wasInactive = mActiveCount == 0
            mActiveCount += if (mActive) 1 else -1
            if (wasInactive && mActive) {
                onActive()
            }
            if (mActiveCount == 0 && !mActive) {
                onInactive()
            }
            if (mActive) {
                dispatchingValue(this)
            }
        }

    }

    private inner class AlwaysActiveObserver internal constructor(eventObserver: Observer<T>) : ObserverWrapper(eventObserver) {
        override fun shouldBeActive(): Boolean {
            return true
        }
    }

    companion object {
        const val START_VERSION = -1
        val NOT_SET = Any()
        fun assertMainThread(methodName: String) {
            check(ArchTaskExecutor.getInstance().isMainThread) {
                ("Cannot invoke " + methodName + " on a background"
                        + " thread")
            }
        }
    }
}