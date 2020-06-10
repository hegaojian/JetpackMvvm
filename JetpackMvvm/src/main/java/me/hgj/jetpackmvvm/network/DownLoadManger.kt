package me.hgj.jetpackmvvm.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File

class DownLoadManger  {

    companion object {
        val instance: DownLoadManger by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            DownLoadManger()
        }
    }

    suspend fun load(url: String,file:File) {
        val request = Request.Builder().url(url).get().build()
        val newCall = OkHttpClient.Builder().build().newCall(request)
        withContext(Dispatchers.IO){
            val response  =  newCall.execute()
            val bytes = response.body()?.byteStream()
            if (file.exists()) {
                bytes?.apply {
                    val fos = file.outputStream()
                    val b = ByteArray(1024)
                    var len = this.read(b)
                    var process = 0
                    while (len != -1) {
                        fos.write(b, 0, len)
                        len = this.read(b)
                         process += len
//                         view.downLoading(process)
                    }
                }
            }
        }

    }
}