package me.hgj.mvvm_nb_demo.data

import me.hgj.mvvm_nb.network.BaseResponse

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　:服务器返回数据的基类 根据自己项目的数据来改变字段,
 * 必须继承 BaseResponse 并传入相关的构造数据，同时要实现isSucces方法，根据自己项目的业务判断请求是否成功，如果你不想这么判断
 * 可以直接 return true  具体想自己拿到请求结果执行不同的业务逻辑，不用框架帮你处理请求结果是否成功，可以参考
 * LoginRegisterViewModel中的请求
 *
 */
open class ApiResponse<T>(data: T, errorCode: Int, errorMsg: String): BaseResponse<T>(data,errorCode,errorMsg){

    /**
     * 请求结果是否是正确的
     */
    override fun isSucces(): Boolean {
        // 这里是示例，wanandroid 网站返回的 请求码为0就代表请求成功，请你根据自己的需求来改变
        return errorCode == 0
    }
}