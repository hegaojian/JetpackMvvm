package me.hgj.jetpackmvvm.callback.livedata.event

/**
 * @author : hgj
 * @date   : 2020/7/3
 */

class EventLiveData<T> : EventBaseLiveData<T> {
    /**
     * Creates a MutableLiveData initialized with the given `value`.
     *
     * @param value initial value
     */
    constructor(value: Event<T>?) : super(value!!) {}

    /**
     * Creates a MutableLiveData with no value assigned to it.
     */
    constructor() : super() {}

    fun postValue(value: T) {
        super.postEvent(Event(value))
    }

    fun setValue(value: T) {
        super.setEvent(Event(value))
    }
}