package me.hgj.jetpackmvvm.demo.app.core.ext

import android.app.Dialog
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import me.hgj.jetpackmvvm.databinding.LayoutBaseLoadingViewBinding
import me.hgj.jetpackmvvm.ext.util.hideOffKeyboard
import java.util.WeakHashMap

/**
 * 作者　: hegaojian
 * 时间　: 2020/11/18
 * 描述　: 这里可以仿写一个你自己项目风格的loadingDialog 然后在基类调用
 */

// 使用一个弱引用集合来管理所有的 loadingDialog
private val loadingDialogs = WeakHashMap<Any, Dialog>() // 使用 WeakHashMap 防止内存泄漏

/**
 * 打开等待框
 */
fun Fragment.showAppLoadingExt(message: String = "请求网络中...", coroutineScope: CoroutineScope? = null) {
    dismissAppLoadingExt() // 先关闭之前的
    activity?.let {
        if (!it.isFinishing) {
            val dialog = MaterialDialog(it)
                .show {
                    val dialogView = LayoutBaseLoadingViewBinding.inflate(LayoutInflater.from(it))
                    dialogView.loadingMessage.text = message
                    customView(view = dialogView.root,horizontalPadding = false)
                    cancelOnTouchOutside(false)
                    cornerRadius(4f)
                    .lifecycleOwner(this@showAppLoadingExt)
                }.onDismiss {
                    //关闭弹窗时 将请求也关闭了
                    coroutineScope?.cancel()
                    loadingDialogs.remove(this) // 从集合中移除
                }
            loadingDialogs[this] = dialog // 以 Fragment 实例为 key 存储
            dialog.show()
        }
    }
}

fun AppCompatActivity.showAppLoadingExt(message: String = "请求网络中...", coroutineScope: CoroutineScope? = null) {
    dismissAppLoadingExt() // 先关闭之前的
    if (!isFinishing) {
        //弹出loading时 把当前界面的输入法关闭
        this@showAppLoadingExt.hideOffKeyboard()
        val dialog = MaterialDialog(this)
            .show {
                val dialogView = LayoutBaseLoadingViewBinding.inflate(LayoutInflater.from(this@showAppLoadingExt))
                dialogView.loadingMessage.text = message
                customView(view = dialogView.root,horizontalPadding = true)
                cornerRadius(4f)
                cancelOnTouchOutside(false)
                .lifecycleOwner(this@showAppLoadingExt)
            }.onDismiss {
                //关闭弹窗时 将请求也关闭了
                coroutineScope?.cancel()
                loadingDialogs.remove(this) // 从集合中移除
            }
        loadingDialogs[this] = dialog // 以 Fragment 实例为 key 存储
        dialog.show()
    }
}

/**
 * 关闭等待框
 */
fun AppCompatActivity.dismissAppLoadingExt() {
    loadingDialogs[this]?.dismiss()
    loadingDialogs.remove(this)
}

/**
 * 关闭等待框
 */
fun Fragment.dismissAppLoadingExt() {
    loadingDialogs[this]?.dismiss()
    loadingDialogs.remove(this)
}

// 全局关闭所有
fun dismissAppAllLoading() {
    loadingDialogs.values.forEach { it.dismiss() }
    loadingDialogs.clear()
}


