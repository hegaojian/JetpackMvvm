package me.hgj.jetpackmvvm.ext.view

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView

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

fun EditText.textString(): String {
    return this.text.toString()
}

fun TextView.textString(): String {
    return this.text.toString()
}