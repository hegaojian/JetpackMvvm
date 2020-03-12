package me.hgj.jetpackmvvm.network

import java.io.Serializable

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/17
 * 描述　: 服务器返回数据的基类
 * 请必须继承它！！请注意：
 * 1.必须给他的构造方法赋值
 * 2.必须实现抽象方法，根据自己的业务判断返回请求结果是否成功
 */
abstract  class BaseResponse<T>(var data: T, var errorCode: Int, var errorMsg: String):Serializable{
    abstract  fun isSucces(): Boolean
}