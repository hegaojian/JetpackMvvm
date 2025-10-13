package me.hgj.jetpackmvvm.demo.ui.activity

import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.viewpager2.widget.ViewPager2
import com.zhpan.bannerview.BannerViewPager
import me.hgj.jetpackmvvm.base.vm.BaseViewModel
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.core.base.BaseActivity
import me.hgj.jetpackmvvm.demo.app.core.widget.banner.WelcomeBannerAdapter
import me.hgj.jetpackmvvm.demo.databinding.ActivityWelcomeBinding
import me.hgj.jetpackmvvm.ext.util.Cache
import me.hgj.jetpackmvvm.ext.util.clickNoRepeat
import me.hgj.jetpackmvvm.ext.util.getColorExt
import me.hgj.jetpackmvvm.ext.util.intent.openActivity
import me.hgj.jetpackmvvm.ext.view.gone
import me.hgj.jetpackmvvm.ext.view.visible
import me.hgj.jetpackmvvm.ext.view.visibleOrGone

/**
 * 作者　: hegaojian
 * 时间　: 2020/2/22
 * 描述　:
 */
@Suppress("DEPRECATED_IDENTITY_EQUALS")
class SplashActivity : BaseActivity<BaseViewModel, ActivityWelcomeBinding>() {

    override val showTitle = false

    private var resList = arrayOf("唱", "跳", "r a p")

    var isFirst by Cache(true)

    private lateinit var mViewPager: BannerViewPager<String>

    override fun initView(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.auto(
                getColorExt(R.color.colorPrimary),
                getColorExt(R.color.colorPrimary)
            )
        )
        // 2. 设置导航栏颜色
        window.navigationBarColor = getColorExt(R.color.colorPrimary)
        mViewPager = mBind.bannerView as BannerViewPager<String>
        if (isFirst) {
            //是第一次打开App 显示引导页
            mBind.welcomeImage.gone()
            mViewPager.apply {
                adapter = WelcomeBannerAdapter()
                registerLifecycleObserver(lifecycle)
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        mBind.welcomeJoin.visibleOrGone(position == resList.size - 1)
                    }
                })
                create(resList.toList())
            }
        } else {
            //不是第一次打开App 0.3秒后自动跳转到主页
            mBind.welcomeImage.visible()
            mViewPager.postDelayed({
                toMain()
            }, 300)
        }
    }

    override fun onBindViewClick() {
        mBind.welcomeJoin.clickNoRepeat {
            toMain()
        }
    }

    fun toMain() {
        isFirst = false
        openActivity<MainActivity>()
        finish()
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

    }

}