package me.hgj.jetpackmvvm.callback.livedata


/**
 * 作者　: hegaojian
 * 时间　: 2019/12/17
 * 描述　:自定义的Double类型 UnPeekNotNullLiveData  做了防止数据倒灌处理 提供了默认值，避免取值的时候还要判空
 */
class DoubleLiveData(var value: Double = 0.0) : UnPeekNotNullLiveData<Double>(value) {

}