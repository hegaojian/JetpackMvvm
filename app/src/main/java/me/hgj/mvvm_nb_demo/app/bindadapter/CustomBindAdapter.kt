package me.hgj.mvvm_nb_demo.app.bindadapter

import android.text.TextWatcher
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import androidx.databinding.BindingAdapter

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　: 自定义 BindingAdapter
 */
object CustomBindAdapter {

    @BindingAdapter("android:checkChange")
    @JvmStatic
    fun checkboxChange(view: CheckBox, listener: CompoundButton.OnCheckedChangeListener) {
        view.setOnCheckedChangeListener(listener)
    }

    @BindingAdapter("android:afterchange")
    @JvmStatic
    fun afterchange(view: EditText, textWatcher:TextWatcher) {
        view.addTextChangedListener(textWatcher)
    }
}