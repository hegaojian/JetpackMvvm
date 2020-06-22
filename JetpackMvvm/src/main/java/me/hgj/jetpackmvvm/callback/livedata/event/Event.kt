package me.hgj.jetpackmvvm.callback.livedata.event

/**
 * 配合 MutableLiveData & EventViewModel 的使用
 * @author : hgj
 * @date   : 2020/6/8
 */
class Event<T>(private val content: T?) {
    private var hasHandled = false
    fun getContent(): T? {
        if (hasHandled) {
            return null
        }
        hasHandled = true
        return content
    }

}