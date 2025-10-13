package me.hgj.jetpackmvvm.ext.util

import android.os.Build



/**
 * 获取手机型号
 */
fun getPhoneModel(): String{
    return Build.MODEL
}

/**
 * 获取手机型号
 */
fun getPhoneBrand(): String{
    return Build.BRAND
}

/**
 * 获取手机型号
 */
fun getPhoneVersion(): String{
    return Build.VERSION.RELEASE
}

/**
 * 获取手机型号
 */
fun getPhoneCpu(): String{
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) Build.CPU_ABI else Build.SUPPORTED_ABIS[0]
}
