package me.hgj.base.databind

import androidx.databinding.ObservableField

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/17
 * 描述　:自定义的Int类型 ObservableField  提供了默认值，防止返回的值出现空的情况
 */
class DoubleObservableField(value: Double = 0.0) : ObservableField<Double>(value) {

    override fun get(): Double {
        return super.get()!!
    }

}