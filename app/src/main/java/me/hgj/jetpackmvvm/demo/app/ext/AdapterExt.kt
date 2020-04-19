package me.hgj.jetpackmvvm.demo.app.ext

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter

/**
 * 作者　: hegaojian
 * 时间　: 2020/4/16
 * 描述　:
 */
var adapterlastClickTime = 0L
fun BaseQuickAdapter<*, *>.setNbOnItemClickListener(interval: Long = 500,action: (adapter: BaseQuickAdapter<*, *>, view: View, position: Int) -> Unit) {
    setOnItemClickListener { adapter, view, position ->
         val currentTime = System.currentTimeMillis()
         if (adapterlastClickTime != 0L && (currentTime - adapterlastClickTime < interval)) {
             return@setOnItemClickListener
         }
        adapterlastClickTime = currentTime
        action(adapter,view,position)
    }
}

var adapterchildlastClickTime = 0L
fun BaseQuickAdapter<*, *>.setNbOnItemChildClickListener(interval: Long = 500,action: (adapter: BaseQuickAdapter<*, *>, view: View, position: Int) -> Unit) {
    setOnItemChildClickListener { adapter, view, position ->
        val currentTime = System.currentTimeMillis()
        if (adapterchildlastClickTime != 0L && (currentTime - adapterchildlastClickTime < interval)) {
            return@setOnItemChildClickListener
        }
        adapterchildlastClickTime = currentTime
        action(adapter,view,position)
    }
}