package me.hgj.jetpackmvvm.demo.viewmodel.state

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.ext.download.DownLoadManager
import me.hgj.jetpackmvvm.ext.download.DownloadResultState
import me.hgj.jetpackmvvm.ext.download.downLoadExt

/**
 * @author : hgj
 * @date   : 2020/7/14
 */

class DownloadViewModel : BaseViewModel() {

    var downloadData: MutableLiveData<DownloadResultState> = MutableLiveData()

    /**
     * Apk普通下载 框架自带
     */
    fun downloadApk(path: String, url: String, tag: String) {
        viewModelScope.launch {
            DownLoadManager.downLoad(tag, url, path, "tmd.apk", false, downLoadExt(downloadData))
        }
    }

    /**
     * Apk取消下载
     */
    fun downloadCancel(tag: String) {
        DownLoadManager.cancel(tag)
    }

    /**
     * Apk暂停下载
     */
    fun downloadPause(tag: String) {
        DownLoadManager.pause(tag)
    }

}