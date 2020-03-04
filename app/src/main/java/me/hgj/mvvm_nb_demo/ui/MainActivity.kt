package me.hgj.mvvm_nb_demo.ui

import android.graphics.drawable.ColorDrawable
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import me.hgj.mvvm_nb.BaseViewModel
import me.hgj.mvvm_nb_demo.R
import me.hgj.mvvm_nb_demo.app.base.BaseActivity
import me.hgj.mvvm_nb_demo.app.util.SettingUtil
import me.hgj.mvvm_nb_demo.app.util.StatusBarUtil
import me.hgj.mvvm_nb_demo.databinding.ActivityMainBinding

class MainActivity : BaseActivity<BaseViewModel, ActivityMainBinding>() {

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

    /* var exitTime: Long = 0
     override fun onBackPressed() {
             if (System.currentTimeMillis() - this.exitTime > 2000L) {
                 ToastUtils.showShort("再按一次退出App")
                 this.exitTime = System.currentTimeMillis()
                 return
             } else {
                 super.onBackPressed()
             }

     }*/

}
