package me.hgj.jetpackmvvm.demo.ui.fragment.me

import android.os.Bundle
import me.hgj.jetpackmvvm.core.data.obs
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.core.base.BaseFragment
import me.hgj.jetpackmvvm.demo.app.core.ext.joinQQGroup
import me.hgj.jetpackmvvm.demo.app.core.ext.jumpByLogin
import me.hgj.jetpackmvvm.demo.app.core.ext.nav
import me.hgj.jetpackmvvm.demo.app.core.ext.onRefresh
import me.hgj.jetpackmvvm.demo.app.core.util.LocalDataUtil
import me.hgj.jetpackmvvm.demo.app.core.util.UserManager
import me.hgj.jetpackmvvm.demo.data.model.entity.BannerResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.UserInfo
import me.hgj.jetpackmvvm.demo.data.vm.IntegralViewModel
import me.hgj.jetpackmvvm.demo.data.vm.UserViewModel
import me.hgj.jetpackmvvm.demo.databinding.FragmentMeBinding
import me.hgj.jetpackmvvm.demo.ui.activity.SettingActivity
import me.hgj.jetpackmvvm.demo.ui.activity.WebActivity
import me.hgj.jetpackmvvm.demo.ui.fragment.MainFragmentDirections
import me.hgj.jetpackmvvm.ext.lifecycle.getViewModel
import me.hgj.jetpackmvvm.ext.util.clickNoRepeat
import me.hgj.jetpackmvvm.ext.util.intent.openActivity
import me.hgj.jetpackmvvm.ext.util.load
import me.hgj.jetpackmvvm.ext.util.loadCircle
import me.hgj.jetpackmvvm.ext.util.statusPadding

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　: 我的
 */

class MeFragment : BaseFragment<UserViewModel, FragmentMeBinding>() {

    companion object {
        fun newInstance(): MeFragment {
            val args = Bundle()
            val fragment = MeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    val integralVm by getViewModel<IntegralViewModel>()

    override fun initView(savedInstanceState: Bundle?) {
        mBind.topLayout.statusPadding()
        mBind.meSwipe.onRefresh {
            refreshIntegral()
        }
        refreshIntegral()
    }

    override fun onBindViewClick() {
        super.onBindViewClick()
        mBind.topLayout.clickNoRepeat {
            nav().jumpByLogin()
        }
        mBind.integralLayout.clickNoRepeat {
            //我的积分
            nav().jumpByLogin {
                it.navigate(MainFragmentDirections.toIntegralFragment(integralVm.integralData))
            }
        }
        mBind.collectLayout.clickNoRepeat {
            //我的收藏
            nav().jumpByLogin {
                it.navigate(MainFragmentDirections.toCollectFragment())
            }
        }
        mBind.articleLayout.clickNoRepeat {
            //我的文章
            nav().jumpByLogin {
                it.navigate(MainFragmentDirections.toArticleFragment())
            }
        }
        mBind.urlLayout.clickNoRepeat {
            //开源网站
            WebActivity.start(banner = BannerResponse(title = "玩Android网站",url = "https://www.wanandroid.com/"))
        }
        mBind.joinLayout.clickNoRepeat {
            //加入我们
            joinQQGroup("9n4i5sHt4189d4DvbotKiCHy-5jZtD4D")
        }
        mBind.settingLayout.clickNoRepeat {
            //设置
            openActivity<SettingActivity>()
        }
    }

    override fun createObserver() {
        super.createObserver()
        UserManager.observeUser().observe(viewLifecycleOwner) {
            updateUser(it)
        }
    }

    private fun updateUser(info: UserInfo?) {
        if (info != null) {
            mBind.userName.text = info.nickname.ifEmpty { info.username }
            mBind.headImg.loadCircle(LocalDataUtil.randomImage(), error = R.mipmap.ic_launcher)
            refreshIntegral()
        } else {
            mBind.userName.text = "请先登录~"
            mBind.userInfo.text = "id：--　排名：--"
            mBind.meIntegral.text = "0"
        }
    }

    private fun refreshIntegral() {
        if(!UserManager.isLoggedIn) {
            mBind.meSwipe.isRefreshing = false
            return
        }
        mBind.meSwipe.isRefreshing = true
        integralVm.getIntegralData().obs(viewLifecycleOwner)  {
            onSuccess {
                mBind.meSwipe.isRefreshing = false
                mBind.userInfo.text = "id：${it.userId}　排名：${it.rank}"
                mBind.meIntegral.text = it.coinCount.toString()
            }
            onError {
                mBind.meSwipe.isRefreshing = false
            }
        }
    }

}