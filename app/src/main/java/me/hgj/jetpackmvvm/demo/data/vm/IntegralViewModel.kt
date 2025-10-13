package me.hgj.jetpackmvvm.demo.data.vm

import me.hgj.jetpackmvvm.base.vm.BaseViewModel
import me.hgj.jetpackmvvm.core.data.request
import me.hgj.jetpackmvvm.demo.data.model.entity.IntegralResponse
import me.hgj.jetpackmvvm.demo.data.repository.request.IntegralRepository
import me.hgj.jetpackmvvm.core.net.LoadingType

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/10
 * 描述　:
 */
class IntegralViewModel : BaseViewModel() {

    /** 积分数据，这里因为 UI层需要一个变量数据传递到下个页面，所以这里临时缓存了一下*/
    var integralData : IntegralResponse? = null

    private var pageNo = 1

    fun getIntegralData()  = request {
        onRequest{
            val data  = IntegralRepository.integral().await()
            integralData = data
            data
        }
    }

    fun getIntegralHistoryData(refresh: Boolean = true,loadingXml: Boolean = false)  = request {
        if(refresh){
            pageNo = 1
        }
        onRequest{
            IntegralRepository.integralHistory(pageNo).await().also { pageNo++ }
        }
        loadingType = if(loadingXml) LoadingType.LOADING_XML else LoadingType.LOADING_NULL
    }

    fun getIntegralRankData(refresh: Boolean = true,loadingXml: Boolean = false)  = request {
        if(refresh){
            pageNo = 1
        }
        onRequest{
            IntegralRepository.integralRank(pageNo).await().also { pageNo++ }
        }
        loadingType = if(loadingXml) LoadingType.LOADING_XML else LoadingType.LOADING_NULL
    }

}
