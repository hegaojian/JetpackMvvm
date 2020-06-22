package me.hgj.jetpackmvvm.demo.ui.activity

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.blankj.utilcode.util.ToastUtils
import com.tencent.bugly.beta.Beta
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.base.BaseActivity
import me.hgj.jetpackmvvm.demo.app.ext.showMessage
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
        //进入首页检查更新
        Beta.checkUpgrade(false, true)
    }

    override fun createObserver() {
        shareViewModel.appColor.observe(this, Observer {
            supportActionBar?.setBackgroundDrawable(ColorDrawable(it))
            StatusBarUtil.setColor(this, it, 0)
        })
    }
    /**
     * 示例，在Activity/Fragment中如果想监听网络变化，可重写onNetworkStateChanged该方法
     */
    override fun onNetworkStateChanged(netState: NetState) {
        super.onNetworkStateChanged(netState)
        if (netState.isSuccess) {
            Toast.makeText(this,"我特么终于有网了啊!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this,"我特么怎么断网了!", Toast.LENGTH_SHORT).show()
        }
    }

    var exitTime = 0L
    override fun onBackPressed() {
        val nav = Navigation.findNavController(this, R.id.host_fragment)
        if (nav.currentDestination != null && nav.currentDestination!!.id != R.id.mainfragment) {
            //如果当前界面不是主页，那么直接调用返回即可
            nav.navigateUp()
        } else {
            //是主页
            if (System.currentTimeMillis() - exitTime > 2000) {
                ToastUtils.showShort("再按一次退出程序")
                exitTime = System.currentTimeMillis()
            } else {
                super.onBackPressed()
            }
        }
    }

}
