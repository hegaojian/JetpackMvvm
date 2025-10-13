package me.hgj.jetpackmvvm.core.net

import androidx.annotation.IntDef

/**
 * 作者　：hegaojian
 * 时间　：2025/9/24
 * 描述　：请求时的loading类型
 */
@IntDef(LoadingType.LOADING_NULL, LoadingType.LOADING_DIALOG, LoadingType.LOADING_XML)
@Retention(AnnotationRetention.SOURCE)
annotation class LoadingType {
    companion object {
        /** 请求时不需要Loading 一般用于静默请求 */
        const val LOADING_NULL = 0
        /** 请求时弹出 通用Dialog弹窗Loading */
        const val LOADING_DIALOG = 1
        /** 请求时 界面 Loading Error Empty */
        const val LOADING_XML = 2
    }
}