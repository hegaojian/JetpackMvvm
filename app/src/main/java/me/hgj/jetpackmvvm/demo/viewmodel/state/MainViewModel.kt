package me.hgj.jetpackmvvm.demo.viewmodel.state

import android.app.Application
import androidx.lifecycle.MutableLiveData
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.callback.livedata.StringLiveData
import me.hgj.jetpackmvvm.callback.livedata.UnPeekLiveData
import me.hgj.jetpackmvvm.callback.livedata.UnPeekNotNullLiveData
import me.hgj.jetpackmvvm.demo.app.ext.showMessage

class MainViewModel(application: Application):BaseViewModel(application) {

    var name = UnPeekNotNullLiveData("你是谁")
    var name1 = UnPeekNotNullLiveData(0.0)
    var name2 = UnPeekNotNullLiveData<String>()
    init {
        name2.value = "你是谁111"
    }

}