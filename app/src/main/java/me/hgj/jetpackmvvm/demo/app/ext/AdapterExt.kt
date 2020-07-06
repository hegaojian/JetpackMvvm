package me.hgj.jetpackmvvm.demo.app.ext

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter

/**
 * 作者　: hegaojian
 * 时间　: 2020/4/16
 * 描述　:
 */

/**
 * 给adapter拓展的，防止重复点击item
 */
var adapterLastClickTime = 0L

fun BaseQuickAdapter<*, *>.setNbOnItemClickListener(interval: Long = 1000,action: (adapter: BaseQuickAdapter<*, *>, view: View, position: Int) -> Unit) {
    setOnItemClickListener { adapter, view, position ->
         val currentTime = System.currentTimeMillis()
         if (adapterLastClickTime != 0L && (currentTime - adapterLastClickTime < interval)) {
             return@setOnItemClickListener
         }
        adapterLastClickTime = currentTime
        action(adapter,view,position)
    }
}

/**
 * 给adapter拓展的，防止重复点击item
 */
var adapterchildlastClickTime = 0L
fun BaseQuickAdapter<*, *>.setNbOnItemChildClickListener(interval: Long = 1000,action: (adapter: BaseQuickAdapter<*, *>, view: View, position: Int) -> Unit) {
    setOnItemChildClickListener { adapter, view, position ->
        val currentTime = System.currentTimeMillis()
        if (adapterchildlastClickTime != 0L && (currentTime - adapterchildlastClickTime < interval)) {
            return@setOnItemChildClickListener
        }
        adapterchildlastClickTime = currentTime
        action(adapter,view,position)
    }
}