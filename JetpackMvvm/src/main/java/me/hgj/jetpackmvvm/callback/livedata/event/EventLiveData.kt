package me.hgj.jetpackmvvm.callback.livedata.event

import com.kunminx.architecture.ui.callback.UnPeekLiveData

/**
 * 最新版的发送消息LiveData使用 https://github.com/KunMinX/UnPeek-LiveData 的最新版，因为跟其他类名（UnPeekLiveData）一致 所以继承换了一个名字
 * 在Activity中observe 调用observeInActivity 在Fragment中使用调用 observeInFragment
 * 具体写法请参考 https://github.com/KunMinX/UnPeek-LiveData的示例
 */
class EventLiveData<T> : UnPeekLiveData<T>()