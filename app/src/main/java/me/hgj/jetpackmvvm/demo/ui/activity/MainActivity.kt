package me.hgj.jetpackmvvm.demo.ui.activity

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.core.view.WindowInsetsControllerCompat
import androidx.navigation.findNavController
import me.hgj.jetpackmvvm.base.vm.BaseViewModel
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.core.base.BaseActivity
import me.hgj.jetpackmvvm.demo.databinding.ActivityMainBinding
import me.hgj.jetpackmvvm.ext.util.toast

/**
 * 作者　：hegaojian
 * 时间　：2025/9/30
 * 描述　：项目主页Activity
 */
class MainActivity : BaseActivity<BaseViewModel, ActivityMainBinding>() {

    override val showTitle = false

    var exitTime = 0L

    override fun initView(savedInstanceState: Bundle?) {
        //全面屏
        enableEdgeToEdge()
        //设置状态栏字体颜色为白色
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = false

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val nav = findNavController(R.id.host_fragment)
                if (nav.currentDestination != null && nav.currentDestination!!.id != R.id.mainFragment) {
                    //如果当前界面不是主页，那么直接调用返回即可
                    nav.navigateUp()
                } else {
                    if (System.currentTimeMillis() - exitTime > 2000) {
                        "再按一次退出程序".toast()
                        exitTime = System.currentTimeMillis()
                    } else {
                        finish()
                    }
                }
            }
        })
    }
}
