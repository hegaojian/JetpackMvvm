package me.hgj.jetpackmvvm.network

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/17
 * 描述　:自定义错误信息异常
 */
class AppException : Exception {
    var errorMsg: String
    var errCode: Int = 0

    constructor(errCodeInput: Int,error: String?) : super(error) {
        errorMsg = error ?: "请求失败，请稍后再试"
        errCode = errCodeInput
    }

    constructor(error: Error) {
        errCode = error.getKey()
        errorMsg = error.getValue()
    }
}