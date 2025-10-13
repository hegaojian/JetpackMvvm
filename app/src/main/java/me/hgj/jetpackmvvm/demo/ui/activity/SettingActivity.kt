package me.hgj.jetpackmvvm.demo.ui.activity

import android.os.Bundle
import me.hgj.jetpackmvvm.base.vm.BaseViewModel
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.core.base.BaseActivity
import me.hgj.jetpackmvvm.demo.databinding.FragmentSettingBinding
import me.hgj.jetpackmvvm.demo.ui.fragment.setting.SettingFragment

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/9
 * 描述　: 系统设置
 */
class SettingActivity : BaseActivity<BaseViewModel, FragmentSettingBinding>() {

    override val showTitle =  true

    override val title = "设置"

    override fun initView(savedInstanceState: Bundle?) {
        // 在容器里加载 PreferenceFragment
        if (supportFragmentManager.findFragmentByTag("setting") == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, SettingFragment(), "setting")
                .commitNowAllowingStateLoss()
        }
    }
}

