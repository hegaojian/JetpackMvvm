package me.hgj.jetpackmvvm.ext.util

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import java.util.regex.Pattern

/**
 * 是否为手机号  0开头 12开头的不支持
 */
fun String?.isPhone(): Boolean {
    return this?.let {
        Pattern.matches(it, "0?(13|14|15|16|17|18|19)[0-9]{9}")
    }?:let {
       false
    }
}

/**
 * 是否为座机号
 */
fun String?.isTel(): Boolean {
    return this?.let {
        val matcher1 = Pattern.matches("^0(10|2[0-5|789]|[3-9]\\d{2})\\d{7,8}\$", this)
        val matcher2 = Pattern.matches("^0(10|2[0-5|789]|[3-9]\\d{2})-\\d{7,8}$", this)
        val matcher3 = Pattern.matches("^400\\d{7,8}$", this)
        val matcher4 = Pattern.matches("^400-\\d{7,8}$", this)
        val matcher5 = Pattern.matches("^800\\d{7,8}$", this)
        val matcher6 = Pattern.matches("^800-\\d{7,8}$", this)
        return matcher1 || matcher2 || matcher3 || matcher4 || matcher5 || matcher6
    }?:let {
        false
    }
}

/**
 * 是否为邮箱号
 */
fun String?.isEmail(): Boolean {
    return this?.let {
        Pattern.matches(this, "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*\$")
    }?:let {
        false
    }
}

/**
 * 将对象转为JSON字符串
 */
fun Any?.toJson():String{
    return Gson().toJson(this)
}
