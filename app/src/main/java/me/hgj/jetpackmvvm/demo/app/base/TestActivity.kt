package me.hgj.jetpackmvvm.demo.app.base

import android.os.Bundle
import me.hgj.jetpackmvvm.BaseViewModel
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.databinding.ActivityTestBinding

/**
 * 作者　: hegaojian
 * 时间　: 2020/4/10
 * 描述　:
 */
class TestActivity : MyBaseActivity<BaseViewModel,ActivityTestBinding>() {

    override fun layoutId() = R.layout.activity_test

    override fun initView(savedInstanceState: Bundle?) {

    }

    override fun createObserver() {

    }
}