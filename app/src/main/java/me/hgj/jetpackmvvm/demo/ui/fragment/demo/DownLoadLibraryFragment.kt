package me.hgj.jetpackmvvm.demo.ui.fragment.demo

import android.os.Bundle
import androidx.lifecycle.Observer
import com.liulishuo.filedownloader.FileDownloader
import kotlinx.android.synthetic.main.fragment_demo.*
import kotlinx.android.synthetic.main.fragment_download.*
import kotlinx.android.synthetic.main.fragment_download_library.*
import kotlinx.android.synthetic.main.include_toolbar.*
import kotlinx.android.synthetic.main.layout_custom_progress_dialog_view.*
import me.hgj.jetpackmvvm.base.appContext
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.base.BaseFragment
import me.hgj.jetpackmvvm.demo.app.ext.initClose
import me.hgj.jetpackmvvm.demo.app.ext.showMessage
import me.hgj.jetpackmvvm.demo.databinding.FragmentDownloadLibraryBinding
import me.hgj.jetpackmvvm.demo.viewmodel.state.DownloadLibraryViewModel
import me.hgj.jetpackmvvm.ext.download.DownloadResultState
import me.hgj.jetpackmvvm.ext.download.FileTool
import me.hgj.jetpackmvvm.ext.download.FileTool.getBasePath
import me.hgj.jetpackmvvm.ext.nav
import me.hgj.jetpackmvvm.ext.util.logd

/**集成了GitHub 高star的一个下载库 https://github.com/lingochamp/FileDownloader，供大家参考
 * @author : hgj
 * @date   : 2020/7/13
 */
class DownLoadLibraryFragment : BaseFragment<DownloadLibraryViewModel, FragmentDownloadLibraryBinding>() {

    override fun layoutId() = R.layout.fragment_download_library

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.click = ProxyClick()
        //第三方下载库注册， 可以直接放在application里面注册
        FileDownloader.setup(appContext)
        toolbar.initClose("三方库下载") {
            nav().navigateUp()
        }
    }

    override fun createObserver() {
        mViewModel.downloadData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is DownloadResultState.Pending -> {
                    //开始下载
                    "开始下载".logd()
                }
                is DownloadResultState.Progress -> {
                    //下载中
                    downloadLibraryProgressBar.progress = it.progress
                    "下载中 ${it.soFarBytes}/${it.totalBytes}".logd()
                    downloadLibraryProgress.text = "${it.progress}%"
                    downloadLibrarySize.text = "${FileTool.bytes2kb(it.soFarBytes)}/${FileTool.bytes2kb(it.totalBytes)}"
                }
                is DownloadResultState.Success -> {
                    //下载成功
                    downloadLibraryProgressBar.progress = 100
                    downloadLibraryProgress.text = "100%"
                    downloadLibrarySize.text ="${FileTool.bytes2kb(it.totalBytes)}/${FileTool.bytes2kb(it.totalBytes)}"
                    showMessage("下载成功--文件地址：${it.filePath}")
                }
                is DownloadResultState.Pause -> {
                    showMessage("下载暂停")
                }
                is DownloadResultState.Error -> {
                    //下载失败
                    showMessage("错误信息:${it.errorMsg}")
                }
            }
        })
    }

    inner class ProxyClick {
        fun downloadLibrary() {
            //测试一下利用三方库下载
            mViewModel.downloadApkByLibrary(
                getBasePath(),
                "https://down.qq.com/qqweb/QQlite/Android_apk/qqlite_4.0.1.1060_537064364.apk",
                "qq"
            )
        }
        fun cancel() {
            mViewModel.downloadCancel()
        }
        fun pause() {
            mViewModel.downloadPause()
        }
    }


}