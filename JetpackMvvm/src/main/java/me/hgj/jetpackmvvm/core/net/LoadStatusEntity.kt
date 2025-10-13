package me.hgj.jetpackmvvm.core.net


/**
 * 作者　: hegaojian
 * 时间　: 2020/11/4
 * 描述　: 请求失败，请求数据为空 状态类
 */
data class LoadStatusEntity(
    /** 错误码 */
    var code: String,
    /** 错误消息 */
    var msg: String,
    /** 异常 */
    var throwable: Throwable,
    /** 请求时 loading 类型 */
    @param:LoadingType var loadingType: Int = LoadingType.LOADING_NULL,
)