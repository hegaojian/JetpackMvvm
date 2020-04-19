package me.hgj.jetpackmvvm.demo.app.base

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import me.hgj.jetpackmvvm.BaseViewModel
import me.hgj.jetpackmvvm.BaseVmDbActivity

/**
 * 作者　: hegaojian
 * 时间　: 2020/4/10
 * 描述　:
 */
abstract class MyBaseActivity<Vm:BaseViewModel,Db:ViewDataBinding> : BaseVmDbActivity<Vm,Db>() {

   abstract override fun layoutId(): Int

    abstract override fun initView(savedInstanceState: Bundle?)

    abstract override fun createObserver()

    override fun showLoading(message: String) {

    }

    override fun dismissLoading() {

    }



}