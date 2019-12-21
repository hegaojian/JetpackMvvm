package me.hgj.mvvm_nb.network


/**
 * 服务器返回数据的基类,请必须继承它！！请注意：
 * 1.必须给他的构造方法赋值
 * 2.必须实现抽象方法，根据自己的业务返回请求结果是否成功
 */
abstract  class BaseResponse<T>(var data: T, var errorCode: Int, var errorMsg: String){
    abstract  fun isSucces(): Boolean
}