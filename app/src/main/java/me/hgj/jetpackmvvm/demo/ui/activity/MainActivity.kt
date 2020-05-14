package me.hgj.jetpackmvvm.demo.ui.activity

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.blankj.utilcode.util.ToastUtils
import com.tencent.bugly.beta.Beta
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.base.BaseActivity
import me.hgj.jetpackmvvm.demo.app.util.SettingUtil
import me.hgj.jetpackmvvm.demo.app.util.StatusBarUtil
import me.hgj.jetpackmvvm.demo.databinding.ActivityMainBinding
import me.hgj.jetpackmvvm.demo.viewmodel.state.MainViewModel
import me.hgj.jetpackmvvm.network.manager.NetState

/**
 * 项目主页Activity
 */
class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override fun layoutId() = R.layout.activity_main

    override fun initView(savedInstanceState: Bundle?) {
        supportActionBar?.setBackgroundDrawable(ColorDrawable(SettingUtil.getColor(this)))
        StatusBarUtil.setColor(this, SettingUtil.getColor(this), 0)
        //进入首页检查更新
        Beta.checkUpgrade(false, true)
    }

    override fun createObserver() {
        shareViewModel.appColor.observe(this, Observer {
            it?.let {
                supportActionBar?.setBackgroundDrawable(ColorDrawable(it))
                StatusBarUtil.setColor(this, it, 0)
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return Navigation.findNavController(this, R.id.main_navation).navigateUp()
    }

    /**
     * 示例，在Activity/Fragment中如果想监听网络变化，可重写onNetworkStateChanged该方法
     */
    override fun onNetworkStateChanged(netState: NetState) {
        super.onNetworkStateChanged(netState)
        if (netState.isSuccess) {
            ToastUtils.showShort("有网络")
        } else {
            ToastUtils.showShort("没有网络")
        }
    }
}
