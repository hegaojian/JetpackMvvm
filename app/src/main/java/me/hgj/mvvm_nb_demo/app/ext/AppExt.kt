package me.hgj.mvvm_nb_demo.app.ext

import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import com.blankj.utilcode.util.Utils
import me.hgj.mvvm_nb.BaseVmActivity
import me.hgj.mvvm_nb.BaseVmDbActivity
import me.hgj.mvvm_nb.BaseVmDbFragment
import me.hgj.mvvm_nb.BaseVmFragment
import me.hgj.mvvm_nb_demo.App
import me.hgj.mvvm_nb_demo.viewmodel.AppViewModel


fun EditText.afterTextChange(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s.toString())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

        }
    })
}

fun EditText.toTextString(): String {
    return this.text.toString()
}

fun View.setVisible() {
    this.visibility = View.VISIBLE
}

fun View.setGone() {
    this.visibility = View.GONE
}

fun BaseVmActivity<*>.getAppViewModel(): AppViewModel {
    (Utils.getApp() as App).let {
        return it.getAppViewModelProvider().get(AppViewModel::class.java)
    }
}
fun BaseVmDbActivity<*,*>.getAppViewModel(): AppViewModel {
    (Utils.getApp() as App).let {
        return it.getAppViewModelProvider().get(AppViewModel::class.java)
    }
}

fun BaseVmFragment<*>.getAppViewModel(): AppViewModel {
    (Utils.getApp() as App).let {
        return it.getAppViewModelProvider().get(AppViewModel::class.java)
    }
}
fun BaseVmDbFragment<*,*>.getAppViewModel(): AppViewModel {
    (Utils.getApp() as App).let {
        return it.getAppViewModelProvider().get(AppViewModel::class.java)
    }
}