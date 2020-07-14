package me.hgj.jetpackmvvm.demo.app.ext.download

import androidx.lifecycle.MutableLiveData
import com.liulishuo.filedownloader.BaseDownloadTask
import com.liulishuo.filedownloader.FileDownloadSampleListener
import com.liulishuo.filedownloader.exception.FileDownloadOutOfSpaceException
import me.hgj.jetpackmvvm.ext.download.DownloadResultState

/**
 * @author : hgj
 * @date   : 2020/7/13
 *
 */

fun BaseDownloadTask.listenerExt(downloadResultState: MutableLiveData<DownloadResultState>): BaseDownloadTask {

    this.listener = object : FileDownloadSampleListener() {
        override fun pending(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
            //开始下载
            downloadResultState.postValue(DownloadResultState.onPending())
        }

        override fun progress(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
            //下载中
            downloadResultState.postValue(
                DownloadResultState.onProgress(
                    soFarBytes.toLong(),
                    totalBytes.toLong(),
                    (soFarBytes * 1.0f / totalBytes * 100).toInt()
                )
            )
        }

        override fun completed(task: BaseDownloadTask) {
            //下载完成
            downloadResultState.postValue(
                DownloadResultState.onSuccess(
                    task.targetFilePath,
                    task.smallFileTotalBytes.toLong()
                )
            )
        }

        override fun paused(task: BaseDownloadTask, soFarBytes: Int, totalBytes: Int) {
            //下载暂停
            downloadResultState.postValue(DownloadResultState.onPause())
        }

        override fun error(task: BaseDownloadTask, e: Throwable) {
            //下载错误
            if (task.errorCause is FileDownloadOutOfSpaceException) {
                downloadResultState.postValue(DownloadResultState.onError("内存容量不足"))
            } else {
                downloadResultState.postValue(DownloadResultState.onError("下载出错，请重新下载"))
            }
        }

        override fun warn(task: BaseDownloadTask) {
            //错误警告
            downloadResultState.postValue(DownloadResultState.onError("已经在下载队列中了"))
        }
    }
    return this
}




