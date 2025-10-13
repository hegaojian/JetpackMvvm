package me.hgj.jetpackmvvm.ext.view

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
import java.util.WeakHashMap

/**
 * 作者　: hegaojian
 * 时间　: 2020/11/18
 * 描述　:
 */

/**
 * 显示消息弹窗
 * @param message 显示对话框的内容 必填项
 * @param title 显示对话框的标题 默认 温馨提示
 * @param positiveButtonText 确定按钮文字 默认确定
 * @param positiveAction 点击确定按钮触发的方法 默认空方法
 * @param negativeButtonText 取消按钮文字 默认空 不为空时显示该按钮
 * @param negativeAction 点击取消按钮触发的方法 默认空方法
 *
 */
fun AppCompatActivity.showDialogMessage(
    message: String,
    title: String = "温馨提示",
    positiveButtonText: String = "确定",
    positiveAction: () -> Unit = {},
    negativeButtonText: String = "",
    negativeAction: () -> Unit = {}
) {
    if (!this.isFinishing) {
        MaterialDialog(this)
            .cornerRadius(8f)
            .cancelOnTouchOutside(false)
            .show {
                title(text = title)
                message(text = message)
                positiveButton(text = positiveButtonText) {
                    positiveAction.invoke()
                }
                if (negativeButtonText.isNotEmpty()) {
                    negativeButton(text = negativeButtonText) {
                        negativeAction.invoke()
                    }
                }
            }
    }
}

/**
 * @param message 显示对话框的内容 必填项
 * @param title 显示对话框的标题 默认 温馨提示
 * @param positiveButtonText 确定按钮文字 默认确定
 * @param positiveAction 点击确定按钮触发的方法 默认空方法
 * @param negativeButtonText 取消按钮文字 默认空 不为空时显示该按钮
 * @param negativeAction 点击取消按钮触发的方法 默认空方法
 */
fun Fragment.showDialogMessage(
    message: String,
    title: String = "温馨提示",
    positiveButtonText: String = "确定",
    positiveAction: () -> Unit = {},
    negativeButtonText: String = "",
    negativeAction: () -> Unit = {}
) {
    activity?.let {
        if (!it.isFinishing) {
            MaterialDialog(it)
                .cancelOnTouchOutside(false)
                .cornerRadius(8f)
                .show {
                    title(text = title)
                    message(text = message)
                    positiveButton(text = positiveButtonText) {
                        positiveAction.invoke()
                    }
                    if (negativeButtonText.isNotEmpty()) {
                        negativeButton(text = negativeButtonText) {
                            negativeAction.invoke()
                        }
                    }
                }
        }
    }
}

/*****************************************loading框********************************************/
// 使用一个弱引用集合来管理所有的 loadingDialog，
private val loadingDialogs = WeakHashMap<Any, Dialog>() // 使用 WeakHashMap 防止内存泄漏

/**
 * 打开等待框
 */
fun Fragment.showLoadingExt(
    message: String = "请求网络中...",
    coroutineScope: CoroutineScope? = null
) {
    dismissLoadingExt() // 先关闭之前的
    activity?.let {
        if (!it.isFinishing) {
            val dialog = MaterialDialog(it)
                .show {
                    val dialogView = LayoutBaseLoadingViewBinding.inflate(LayoutInflater.from(it))
                    dialogView.loadingMessage.text = message
                    customView(view = dialogView.root, horizontalPadding = true)
                    cancelOnTouchOutside(false)
                    cornerRadius(4f)
                    .lifecycleOwner(it)
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

fun AppCompatActivity.showLoadingExt(
    message: String = "请求网络中...",
    coroutineScope: CoroutineScope? = null
) {
    dismissLoadingExt() // 先关闭之前的
    if (!isFinishing) {
        val dialog = MaterialDialog(this)
            .show {
                val dialogView = LayoutBaseLoadingViewBinding.inflate(LayoutInflater.from(this@showLoadingExt))
                dialogView.loadingMessage.text = message
                customView(view = dialogView.root, horizontalPadding = true)
                cornerRadius(4f)
                cancelOnTouchOutside(false)
                .lifecycleOwner(this@showLoadingExt)
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
fun AppCompatActivity.dismissLoadingExt() {
    loadingDialogs[this]?.dismiss()
    loadingDialogs.remove(this)
}

/**
 * 关闭等待框
 */
fun Fragment.dismissLoadingExt() {
    loadingDialogs[this]?.dismiss()
    loadingDialogs.remove(this)
}

// 全局关闭所有
fun dismissAllLoading() {
    loadingDialogs.values.forEach { it.dismiss() }
    loadingDialogs.clear()
}



