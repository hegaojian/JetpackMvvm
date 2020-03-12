package me.hgj.jetpackmvvm.databind

import androidx.databinding.ObservableField

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/17
 * 描述　: 自定义的Boolean类型ObservableField 提供了默认值，防止返回的值出现空的情况
 */
class BooleanObservableField(value: Boolean = false) : ObservableField<Boolean>(value) {
    override fun get(): Boolean {
        return super.get()!!
    }

}