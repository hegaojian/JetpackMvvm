package me.hgj.mvvm_nb_demo.app.ext

import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.blankj.utilcode.util.Utils
import me.hgj.mvvm_nb.*
import me.hgj.mvvm_nb_demo.App
import me.hgj.mvvm_nb_demo.app.AppViewModel
import me.hgj.mvvm_nb_demo.app.base.BaseActivity
import me.hgj.mvvm_nb_demo.app.base.BaseFragment
import me.hgj.mvvm_nb_demo.app.util.SettingUtil


fun BaseVmDbActivity<*, *>.getAppViewModel(): AppViewModel {
    (Utils.getApp() as App).let {
        return it.getAppViewModelProvider().get(AppViewModel::class.java)
    }
}
fun BaseVmDbFragment<*, *>.getAppViewModel(): AppViewModel {
    (Ktx.app as App).let {
        return it.getAppViewModelProvider().get(AppViewModel::class.java)
    }
}

fun BaseActivity<*, *>.showMessage(message: String) {
    MaterialDialog(this)
        .cancelable(false)
        .lifecycleOwner(this)
        .show {
            title(text = "温馨提示")
            message(text = message)
            positiveButton(text = "确定")
            getActionButton(WhichButton.POSITIVE).updateTextColor(SettingUtil.getColor(this@showMessage))
            getActionButton(WhichButton.NEGATIVE).updateTextColor(SettingUtil.getColor(this@showMessage))
        }
}

fun BaseFragment<*, *>.showMessage(message: String) {
    activity?.let {
        MaterialDialog(it)
            .cancelable(false)
            .lifecycleOwner(this)
            .show {
                title(text = "温馨提示")
                message(text = message)
                positiveButton(text = "确定")
                getActionButton(WhichButton.POSITIVE).updateTextColor(SettingUtil.getColor(it))
                getActionButton(WhichButton.NEGATIVE).updateTextColor(SettingUtil.getColor(it))
            }
    }

}