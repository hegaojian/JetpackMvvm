package me.hgj.jetpackmvvm.callback.livedata

import androidx.lifecycle.MutableLiveData

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/17
 * 描述　:自定义的Boolean类型 MutableLiveData  提供了默认值，避免取值的时候还要判空
 */
class IntLiveData(value: Int = 0) : MutableLiveData<Int>(value) {

    override fun getValue(): Int {
        return super.getValue()!!
    }
}