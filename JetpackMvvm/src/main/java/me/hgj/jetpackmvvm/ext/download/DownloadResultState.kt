package me.hgj.jetpackmvvm.ext.download

/**
 * @author : hgj
 * @date   : 2020/7/13
 */
sealed class DownloadResultState {

    companion object {

        fun onPending(): DownloadResultState = Pending

        fun onProgress(soFarBytes: Long, totalBytes: Long, progress: Int): DownloadResultState =  Progress(soFarBytes, totalBytes,progress)

        fun onSuccess(filePath: String,totalBytes:Long): DownloadResultState = Success(filePath,totalBytes)

        fun onPause(): DownloadResultState = Pause

        fun onError(errorMsg: String): DownloadResultState = Error(errorMsg)
    }

    object Pending : DownloadResultState()
    data class Progress(val soFarBytes: Long, val totalBytes: Long,val progress: Int) : DownloadResultState()
    data class Success(val filePath: String,val totalBytes:Long) : DownloadResultState()
    object Pause : DownloadResultState()
    data class Error(val errorMsg: String) : DownloadResultState()
}