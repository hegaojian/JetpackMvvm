package me.hgj.jetpackmvvm.callback.databind

import androidx.databinding.ObservableField

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/17
 * 描述　:自定义的 Short 类型 ObservableField  提供了默认值，避免取值的时候还要判空
 */
class ShortObservableField(value: Short = 0) : ObservableField<Short>(value) {

    override fun get(): Short {
        return super.get()!!
    }

}