package me.hgj.jetpackmvvm.demo.ui

import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.base.BaseActivity
import me.hgj.jetpackmvvm.demo.app.util.SettingUtil
import me.hgj.jetpackmvvm.demo.app.util.StatusBarUtil
import me.hgj.jetpackmvvm.demo.databinding.ActivityMainBinding

class MainActivity : BaseActivity<MessageViewmodel, ActivityMainBinding>() {

    override fun layoutId() = R.layout.activity_main

    override fun initView() {
        supportActionBar?.setBackgroundDrawable(ColorDrawable(SettingUtil.getColor(this)))
        StatusBarUtil.setColor(this, SettingUtil.getColor(this), 0)
    }

    override fun createObserver() {
        appViewModel.appColor.observe(this, Observer {
            it?.let {
                supportActionBar?.setBackgroundDrawable(ColorDrawable(it))
                StatusBarUtil.setColor(this, it, 0)
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.main_navation).navigateUp()
    }

}
