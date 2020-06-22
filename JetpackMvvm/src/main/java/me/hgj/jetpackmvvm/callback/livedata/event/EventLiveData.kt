package me.hgj.jetpackmvvm.callback.livedata.event

/**
 * 配合 MutableLiveData & EventViewModel 的使用 ,专门用户发送消息，发送完成后会把发送的对象自动销毁，防止内存增加
 * @author : hgj
 * @date   : 2020/6/8
 */
class EventLiveData<T> : EventBaseLiveData<T> {

    constructor(value: Event<T>?) : super(value!!)

    constructor() : super()

    public override fun postValue(value: Event<T>) {
        super.postValue(value)
    }

    fun postValue(value: T) {
        super.postValue(Event(value))
    }

    public override fun setValue(value: Event<T>) {
        super.setValue(value)
    }

    fun setValue(value: T) {
        super.postValue(Event(value))
    }

    /**
     * 什么值都不想传，想直接调用该方法就达到通知的目的，请调用这个方法，劳资直接给他传给null
     */
    fun call(){
        super.postValue(Event(null))
    }
}