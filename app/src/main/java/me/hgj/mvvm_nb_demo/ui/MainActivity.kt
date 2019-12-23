package me.hgj.mvvm_nb_demo.ui

import android.content.Intent
import androidx.lifecycle.Observer
import me.hgj.mvvm_nb_demo.R
import me.hgj.mvvm_nb_demo.app.base.BaseActivity
import me.hgj.mvvm_nb_demo.databinding.ActivityMainBinding
import me.hgj.mvvm_nb_demo.viewmodel.MainViewModel

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override fun layoutId() = R.layout.activity_main

    override fun initView() {
        mDatabind.viewmodel = mViewModel
        mDatabind.click = MainClickPox()
    }

    override fun createObserver() {
        appViewModel.userinfo.observe(this, Observer {
            mViewModel.username.set("嗯，你登录成功了，你登录的用户名是${it.username}")
        })
    }

    inner class MainClickPox {
        fun gologin() {
            //去登录
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }
    }


}
