package me.hgj.jetpackmvvm.core.net

import kotlinx.coroutines.CoroutineScope
import me.hgj.jetpackmvvm.R
import me.hgj.jetpackmvvm.ext.util.getStringExt


/**
 * 作者　: hegaojian
 * 时间　: 2020/11/3
 * 描述　:
 */
data class LoadingEntity(
    @LoadingType var loadingType: Int = LoadingType.LOADING_NULL,
    var loadingMessage: String = getStringExt(R.string.helper_loading_tip),
    var isShow: Boolean = false,
    var coroutineScope: CoroutineScope? = null //请求绑定的作用域
)