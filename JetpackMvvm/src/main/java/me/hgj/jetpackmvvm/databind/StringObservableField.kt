package me.hgj.jetpackmvvm.databind

import androidx.databinding.ObservableField

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/17
 * 描述　:自定义的String类型 ObservableField  提供了默认值，防止返回的值出现空的情况
 */
class StringObservableField(value: String = "") : ObservableField<String>(value) {

    override fun get(): String {
        return super.get()!!
    }

}