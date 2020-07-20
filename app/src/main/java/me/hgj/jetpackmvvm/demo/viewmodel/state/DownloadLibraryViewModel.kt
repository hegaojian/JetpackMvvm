package me.hgj.jetpackmvvm.demo.viewmodel.state

import androidx.lifecycle.MutableLiveData
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloader
import me.hgj.jetpackmvvm.base.viewmodel.BaseViewModel
import me.hgj.jetpackmvvm.demo.app.ext.download.listenerExt
import me.hgj.jetpackmvvm.ext.download.DownloadResultState

/**
 * @author : hgj
 * @date   : 2020/7/14
 */
class DownloadLibraryViewModel : BaseViewModel() {
    var downloadData: MutableLiveData<DownloadResultState> = MutableLiveData()

    private var baseDownloadTask: BaseDownloadTask? = null

    private val fileDownloader by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        FileDownloader.getImpl()
    }

    /**
     * Apk三方库下载
     * @param path String 保存路径
     * @param url String 下载路径
     * @param tag Any 下载标识
     */
    fun downloadApkByLibrary(path: String, url: String, tag: Any) {
        baseDownloadTask = fileDownloader.create(url)
        baseDownloadTask?.let {
            it.setPath("$path/cxk_tmd.apk", false)
                .setAutoRetryTimes(5)
                //是否直接强制下载，不管文件是否存在，如果想每次都下载那就设置为true吧
                .setForceReDownload(false)
                .setCallbackProgressTimes(300)
                .setMinIntervalUpdateSpeed(400)
                .tag = tag
            it.listenerExt(downloadData)
                .start()
        }
    }

    /**
     * Apk取消下载
     */
    fun downloadCancel() {
        baseDownloadTask?.let { fileDownloader.clear(it.id, it.targetFilePath) }
    }

    /**
     * Apk暂停下载
     */
    fun downloadPause() {
        baseDownloadTask?.let { fileDownloader.pause(it.id) }
    }

    override fun onCleared() {
        super.onCleared()
    }

}