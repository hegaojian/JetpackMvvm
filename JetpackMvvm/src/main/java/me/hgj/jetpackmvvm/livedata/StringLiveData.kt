package me.hgj.jetpackmvvm.livedata

import androidx.lifecycle.MutableLiveData

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/17
 * 描述　:自定义的Double类型 MutableLiveData  提供了默认值，防止返回的值出现空的情况
 */
class StringLiveData(value: String = "") : MutableLiveData<String>(value) {

    override fun getValue(): String {
        return super.getValue()!!
    }
}