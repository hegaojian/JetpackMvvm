package me.hgj.mvvm_nb_demo.app.base

import android.widget.TextView
import androidx.databinding.ViewDataBinding
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.afollestad.materialdialogs.customview.getCustomView
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.blankj.utilcode.util.ToastUtils
import me.hgj.mvvm_nb.BaseViewModel
import me.hgj.mvvm_nb.BaseVmDbFragment
import me.hgj.mvvm_nb_demo.R
import me.hgj.mvvm_nb_demo.app.ext.getAppViewModel
import me.hgj.mvvm_nb_demo.viewmodel.AppViewModel

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/21
 * 描述　: 你项目中的Fragment基类，在这里实现显示弹窗，吐司，还有自己的需求操作 ，如果不想用Databind，请继承
 * BaseVmFragment例如
 * abstract class BaseFragment<VM : BaseViewModel> : BaseVmFragment<VM>() {
 */
abstract class BaseFragment<VM : BaseViewModel,DB:ViewDataBinding> : BaseVmDbFragment<VM,DB>() {

    private var dialog: MaterialDialog? = null

    //是否第一次加载
    private var isFirst: Boolean = true

    val appViewModel: AppViewModel by lazy { getAppViewModel() }

    /**
     * 当前Fragment绑定的视图布局
     */
    abstract override fun layoutId(): Int

    /**
     * 懒加载
     */
    abstract override fun lazyLoadData()

    /**
     * 创建观察者
     */
    abstract override fun createObserver()


    /**
     * Fragment执行onViewCreated后触发的方法
     */
    override fun initData() {

    }

    /**
     * 打开等待框
     */
    override fun showLoading(message: String) {
        if (dialog == null) {
            dialog = activity?.let {
                MaterialDialog(it)
                    .cancelable(true)
                    .cancelOnTouchOutside(false)
                    .cornerRadius(8f)
                    .customView(R.layout.custom_progress_dialog_view)
                    .lifecycleOwner(this)
            }
            dialog?.getCustomView()?.run {
                this.findViewById<TextView>(R.id.loading_tips).text = message
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
     * 消息弹窗
     */
    override fun showMessage(message: String) {
        activity?.let {
            MaterialDialog(it)
                .cancelable(false)
                .lifecycleOwner(this)
                .show {
                    title(text = "温馨提示")
                    message(text = message)
                    positiveButton(text = "确定")
                }
        }
    }

    /**
     * 吐司
     */
    override fun showToast(message: String) {
        ToastUtils.showShort(message)
    }
}