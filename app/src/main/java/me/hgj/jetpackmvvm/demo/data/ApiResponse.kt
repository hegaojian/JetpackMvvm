package me.hgj.jetpackmvvm.demo.data

import me.hgj.jetpackmvvm.network.BaseResponse

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　:服务器返回数据的基类
 * 如果你的项目中有基类，那美滋滋，可以继承BaseResponse，请求时框架可以帮你自动脱壳，自动判断是否请求成功，怎么做：
 * 1.继承 BaseResponse
 * 2.重写isSucces 方法，编写你的业务需求，根据自己的条件判断数据是否请求成功
 * 3.重写 getResponseCode、getResponseData、getResponseMsg方法，传入你的 code data msg
 */
class ApiResponse<T>(var errorCode: Int, var errorMsg: String, var data: T) : BaseResponse<T>() {

    override fun isSucces(): Boolean {
        // 这里是示例，wanandroid 网站返回的 错误码为 0 就代表请求成功，请你根据自己的业务需求来改变
        return errorCode == 0
    }

    override fun getResponseCode(): Int {
        //返回你的code
        return errorCode
    }

    override fun getResponseData(): T {
        //返回你的 data
        return data
    }

    override fun getResponseMsg(): String {
        //返回你的 错误消息
        return errorMsg
    }


}