package me.hgj.mvvm_nb.network


/**
 * @author :Reginer in  2019/7/8 9:41.
 *         联系方式:QQ:282921012
 *         功能描述:错误
 */

class AppException : Exception {
    var errorMsg: String
    var errCode: Int = 0

    constructor(errCodeInput: Int,error: String?) : super(error) {
        errorMsg = error ?: "请求失败，请稍后再试"
        errCode = errCodeInput
    }

    constructor(error: ERROR) {
        errCode = error.getKey()
        errorMsg = error.getValue()
    }
}