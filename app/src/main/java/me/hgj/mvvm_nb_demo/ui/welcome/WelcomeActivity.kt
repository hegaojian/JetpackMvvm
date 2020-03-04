package me.hgj.mvvm_nb_demo.ui.welcome

import android.content.Intent
import com.blankj.utilcode.util.ConvertUtils
import com.zhpan.bannerview.BannerViewPager
import kotlinx.android.synthetic.main.activity_welcome.*
import me.hgj.mvvm_nb.BaseViewModel
import me.hgj.mvvm_nb.ext.view.gone
import me.hgj.mvvm_nb.ext.view.visible
import me.hgj.mvvm_nb_demo.R
import me.hgj.mvvm_nb_demo.app.base.BaseActivity
import me.hgj.mvvm_nb_demo.app.ext.setPageListener
import me.hgj.mvvm_nb_demo.app.util.CacheUtil
import me.hgj.mvvm_nb_demo.app.weight.banner.WelcomeBannerViewHolder
import me.hgj.mvvm_nb_demo.databinding.ActivityWelcomeBinding
import me.hgj.mvvm_nb_demo.ui.MainActivity

/**
 * 作者　: hegaojian
 * 时间　: 2020/2/22
 * 描述　:
 */
class WelcomeActivity : BaseActivity<BaseViewModel, ActivityWelcomeBinding>() {

    var resList = arrayOf("唱", "跳", "r a p")

    private lateinit var mViewPager: BannerViewPager<String, WelcomeBannerViewHolder>

    override fun layoutId() = R.layout.activity_welcome

    override fun initView() {
        mViewPager = findViewById(R.id.banner_view)
        if (CacheUtil.isFirst()) {
            //是第一次打开App 显示引导页
            welcome_image.gone()
            mViewPager.setHolderCreator {
                WelcomeBannerViewHolder()
            }.setIndicatorMargin(0, 0, 0, ConvertUtils.dp2px(24f))
                .setPageListener {
                    if (it == resList.size - 1) {
                        button2.visible()
                    } else {
                        button2.gone()
                    }
                }
            mViewPager.create(resList.toList())
        } else {
            //不是第一次打开App 0.5秒后自动跳转到主页
            welcome_image.visible()
            mViewPager.postDelayed({
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }, 500)
        }
        button2.setOnClickListener {
            CacheUtil.setFirst(false)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    override fun createObserver() {

    }


}