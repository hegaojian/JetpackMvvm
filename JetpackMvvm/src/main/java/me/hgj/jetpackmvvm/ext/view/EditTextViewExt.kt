package me.hgj.jetpackmvvm.ext.view

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.TextView

/**
 * 优化输入框
 */
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


/**
 * 获取文本
 */
fun EditText.textString(): String {
    return this.text.toString()
}
/**
 * 获取去除空字符串的文本
 */
fun EditText.textStringTrim(): String {
    return this.textString().trim()
}
/**
 * 文本是否为空
 */
fun EditText.isEmpty(): Boolean {
    return this.textString().isEmpty()
}
/**
 * 去空字符串后文本是否为空
 */
fun EditText.isTrimEmpty(): Boolean {
    return this.textStringTrim().isEmpty()
}
/**
 * 获取文本
 */
fun TextView.textString(): String {
    return this.text.toString()
}
/**
 * 获取去除空字符串的文本
 */
fun TextView.textStringTrim(): String {
    return this.textString().trim()
}
/**
 * 文本是否为空
 */
fun TextView.isEmpty(): Boolean {
    return this.textString().isEmpty()
}
/**
 * 去空字符串后文本是否为空
 */
fun TextView.isTrimEmpty(): Boolean {
    return this.textStringTrim().isEmpty()
}