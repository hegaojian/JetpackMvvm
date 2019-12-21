package me.hgj.mvvm_nb_demo

import androidx.databinding.ViewDataBinding
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.custom_progress_dialog_view.*
import me.hgj.mvvm_nb.BaseViewModel
import me.hgj.mvvm_nb.BaseVmDbActivity

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/21
 * 描述　:
 */
abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding>: BaseVmDbActivity<VM, DB>() {

    private var dialog: MaterialDialog? = null

    /**
     * 打开等待框
     */
    override fun showLoading(message:String) {
        if (dialog == null) {
            dialog = this.let {
                MaterialDialog(it)
                    .cancelable(true)
                    .cancelOnTouchOutside(false)
                    .cornerRadius(8f)
                    .customView(R.layout.custom_progress_dialog_view)
                    .lifecycleOwner(this)

            }
            dialog?.getCustomView()?.apply {
                tvTip.text = message
            }
        }
        dialog?.show()
    }

    /**
     * 关闭等待框
     */
    override fun dismissLoading() {
        dialog?.dismiss()
    }

    /**
     * 显示消息弹窗
     */
    override fun showMessage(message: String) {
        MaterialDialog(this)
            .cancelable(false)
            .cornerRadius(8f)
            .lifecycleOwner(this)
            .show {
                title(text = "温馨提示")
                message(text = message)
                positiveButton(text = "确定")
            }
    }

    override fun showToast(message: String) {
        ToastUtils.showShort(message)
    }

}