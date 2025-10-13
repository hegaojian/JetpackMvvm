package me.hgj.jetpackmvvm.demo.data.repository.request

import me.hgj.jetpackmvvm.demo.app.core.net.NetUrl
import me.hgj.jetpackmvvm.demo.data.model.entity.ApiPagerResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.IntegralHistoryResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.IntegralResponse
import rxhttp.wrapper.coroutines.Await
import rxhttp.wrapper.param.RxHttp
import rxhttp.wrapper.param.toAwaitResponse

/**
 * 作者　: hegaojian
 * 时间　: 2020/11/2
 * 描述　: 用户积分
 */
object IntegralRepository {

    /**
     * 用户积分
     */
    fun integral(): Await<IntegralResponse> {
        return RxHttp.get(NetUrl.Integral.USER_INTEGRAL)
            .toAwaitResponse()
    }

    /**
     * 获取积分排行榜
     */
    fun integralRank(pageIndex: Int): Await<ApiPagerResponse<IntegralResponse>> {
        return RxHttp.get(NetUrl.Integral.INTEGRAL_RANK,pageIndex)
            .toAwaitResponse()
    }

    /**
     * 获取积分历史
     */
    fun integralHistory(pageIndex: Int): Await<ApiPagerResponse<IntegralHistoryResponse>> {
        return RxHttp.get(NetUrl.Integral.INTEGRAL_HISTORY,pageIndex)
            .toAwaitResponse()
    }

}

