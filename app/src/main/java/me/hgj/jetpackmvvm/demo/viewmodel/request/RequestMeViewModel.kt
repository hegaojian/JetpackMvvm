package me.hgj.jetpackmvvm.demo.viewmodel.request

import android.app.Application
import androidx.lifecycle.MutableLiveData
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.demo.data.model.bean.IntegralResponse
import me.hgj.jetpackmvvm.demo.data.repository.request.HttpRequestManger
import me.hgj.jetpackmvvm.ext.request
import me.hgj.jetpackmvvm.state.ResultState

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/27
 * 描述　:
 */
class RequestMeViewModel(application: Application) : BaseViewModel(application) {

    var meData = MutableLiveData<ResultState<IntegralResponse>>()

    fun getIntegral() {
        request({ HttpRequestManger.apiService.getIntegral() }, meData)
    }
}