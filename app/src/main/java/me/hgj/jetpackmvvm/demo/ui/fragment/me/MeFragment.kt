package me.hgj.jetpackmvvm.demo.ui.fragment.me

import android.os.Bundle
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.fragment_me.*
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.base.BaseFragment
import me.hgj.jetpackmvvm.demo.app.ext.init
import me.hgj.jetpackmvvm.demo.app.ext.joinQQGroup
import me.hgj.jetpackmvvm.demo.app.ext.jumpByLogin
import me.hgj.jetpackmvvm.demo.app.ext.setUiTheme
import me.hgj.jetpackmvvm.demo.data.model.bean.BannerResponse
import me.hgj.jetpackmvvm.demo.data.model.bean.IntegralResponse
import me.hgj.jetpackmvvm.demo.databinding.FragmentMeBinding
import me.hgj.jetpackmvvm.demo.viewmodel.request.RequestMeViewModel
import me.hgj.jetpackmvvm.demo.viewmodel.state.MeViewModel
import me.hgj.jetpackmvvm.ext.getViewModel
import me.hgj.jetpackmvvm.ext.nav
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.ext.util.notNull

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　: 我的
 */
class MeFragment : BaseFragment<MeViewModel, FragmentMeBinding>() {

    private var rank: IntegralResponse? = null

    /** 注意，在by lazy中使用getViewModel一定要使用泛型，虽然他提示不报错，但是你不写是不行的 */
    private val requestMeViewModel: RequestMeViewModel by lazy { getViewModel<RequestMeViewModel>() }

    override fun layoutId() = R.layout.fragment_me

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.vm = mViewModel
        mDatabind.click = ProxyClick()
        shareViewModel.appColor.value.let { setUiTheme(it, me_linear, me_integral) }
        shareViewModel.userinfo.value?.let { mViewModel.name.postValue(if (it.nickname.isEmpty()) it.username else it.nickname) }
        me_swipe.init {
            requestMeViewModel.getIntegral()
        }
    }

    override fun lazyLoadData() {
        shareViewModel.userinfo.value?.run {
            me_swipe.isRefreshing = true
            requestMeViewModel.getIntegral()
        }
    }

    override fun createObserver() {
        requestMeViewModel.meData.observe(viewLifecycleOwner, Observer { resultState ->
            me_swipe.isRefreshing = false
            parseState(resultState, {
                rank = it
                mViewModel.info.postValue("id：${it.userId}　排名：${it.rank}")
                mViewModel.integral.postValue(it.coinCount)
            }, {
                ToastUtils.showShort(it.errorMsg)
            })
        })
        shareViewModel.run {
            appColor.observe(viewLifecycleOwner, Observer {
                setUiTheme(it, me_linear, me_swipe, me_integral)
            })
            userinfo.observe(viewLifecycleOwner, Observer {
                it.notNull({
                    me_swipe.isRefreshing = true
                    mViewModel.name.postValue(if (it.nickname.isEmpty()) it.username else it.nickname)
                    requestMeViewModel.getIntegral()
                }, {
                    mViewModel.name.postValue("请先登录~")
                    mViewModel.info.postValue("id：--　排名：--")
                    mViewModel.integral.postValue(0)
                })
            })
        }
    }

    inner class ProxyClick {

        /** 登录 */
        fun login() {
            nav().jumpByLogin {}
        }

        /** 收藏 */
        fun collect() {
            nav().jumpByLogin {
                it.navigate(R.id.action_mainfragment_to_collectFragment)
            }
        }

        /** 积分 */
        fun integral() {
            nav().jumpByLogin {
                it.navigate(R.id.action_mainfragment_to_integralFragment,
                    Bundle().apply {
                        putParcelable("rank", rank)
                    }
                )
            }
        }

        /** 文章 */
        fun ariticle() {
            nav().jumpByLogin {
                it.navigate(R.id.action_mainfragment_to_ariticleFragment)
            }
        }

        /** Todo */
        fun todo() {
            nav().jumpByLogin {
                it.navigate(R.id.action_mainfragment_to_todoListFragment)
            }
        }

        /** 玩Android开源网站 */
        fun about() {
            nav().navigate(R.id.action_mainfragment_to_webFragment, Bundle().apply {
                putParcelable(
                    "bannerdata",
                    BannerResponse(
                        title = "玩Android网站",
                        url = "https://www.wanandroid.com/"
                    )
                )
            })
        }

        /** 加入我们 */
        fun join() {
            joinQQGroup("9n4i5sHt4189d4DvbotKiCHy-5jZtD4D")
        }

        /** 设置 */
        fun setting() {
            nav().navigate(R.id.action_mainfragment_to_settingFragment)
        }
    }
}