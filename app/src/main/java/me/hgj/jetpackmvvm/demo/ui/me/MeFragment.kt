package me.hgj.jetpackmvvm.demo.ui.me

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.blankj.utilcode.util.ToastUtils
import kotlinx.android.synthetic.main.fragment_me.*
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.base.BaseFragment
import me.hgj.jetpackmvvm.demo.app.ext.clickNoRepeatLogin
import me.hgj.jetpackmvvm.demo.app.ext.init
import me.hgj.jetpackmvvm.demo.app.ext.setUiTheme
import me.hgj.jetpackmvvm.demo.data.bean.BannerResponse
import me.hgj.jetpackmvvm.demo.data.bean.IntegralResponse
import me.hgj.jetpackmvvm.demo.databinding.FragmentMeBinding
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.ext.util.notNull
import me.hgj.jetpackmvvm.ext.view.clickNoRepeat

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　: 用法跟Activity一样
 */
class MeFragment : BaseFragment<MeViewModel, FragmentMeBinding>() {

    private var rank:IntegralResponse? = null

    override fun layoutId() = R.layout.fragment_me

    override fun initView() {
        mDatabind.vm = mViewModel
        appViewModel.appColor.value?.let { setUiTheme(it, listOf(me_linear, me_integral)) }
        appViewModel.userinfo.value?.let { mViewModel.name.set(if (it.nickname.isEmpty()) it.username else it.nickname) }

        me_swipe.init {
            mViewModel.getIntegral()
        }

        me_linear.clickNoRepeatLogin {}

        me_integralLinear.clickNoRepeatLogin {
            Navigation.findNavController(it).navigate(R.id.action_mainfragment_to_integralFragment,
                Bundle().apply {
                    putSerializable("rank",rank)
                }
            )
        }
        me_collect.clickNoRepeatLogin {
            Navigation.findNavController(it).navigate(R.id.action_mainfragment_to_collectFragment)
        }
        me_article.clickNoRepeatLogin {
            Navigation.findNavController(it).navigate(R.id.action_mainfragment_to_ariticleFragment)
        }
        me_todo.clickNoRepeatLogin {
            Navigation.findNavController(it).navigate(R.id.action_mainfragment_to_todoListFragment)
        }
        me_about.clickNoRepeat {
            Navigation.findNavController(it).navigate(R.id.action_mainfragment_to_webFragment,Bundle().apply {
                putSerializable("bannerdata",BannerResponse(title = "玩Android网站",url = "https://www.wanandroid.com/"))
            })
        }
        me_join.clickNoRepeat {
            joinQQGroup("9n4i5sHt4189d4DvbotKiCHy-5jZtD4D")
        }
        me_setting.clickNoRepeat {
            Navigation.findNavController(it).navigate(R.id.action_mainfragment_to_settingFragment)
        }
    }

    override fun lazyLoadData() {
        appViewModel.userinfo.value?.run {
            me_swipe.isRefreshing = true
            mViewModel.getIntegral()
        }
    }

    override fun createObserver() {
        mViewModel.meData.observe(viewLifecycleOwner, Observer { data ->
            me_swipe.isRefreshing = false
            parseState(data, {
                rank = it
                mViewModel.info.set("id：${it.userId}　排名：${it.rank}")
                mViewModel.integral.set(it.coinCount.toString())
            }, {
                ToastUtils.showShort(it.errorMsg)
            })
        })
        appViewModel.run {
            appColor.observe(viewLifecycleOwner, Observer {
                setUiTheme(it, listOf(me_linear,me_swipe,me_integral))
            })
            userinfo.observe(viewLifecycleOwner, Observer {
                it.notNull({
                    mViewModel.name.set(if (it.nickname.isEmpty()) it.username else it.nickname)
                    mViewModel.getIntegral()
                }, {
                    mViewModel.name.set("请先登录~")
                    mViewModel.info.set("id：--　排名：--")
                    mViewModel.integral.set("0")
                })
            })
        }
    }

    /**
     * 加入qq聊天群
     */
    fun joinQQGroup(key: String): Boolean {
        val intent = Intent()
        intent.data = Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D$key")
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return try {
            startActivity(intent)
            true
        } catch (e: Exception) {
            // 未安装手Q或安装的版本不支持
            ToastUtils.showShort("未安装手机QQ或安装的版本不支持")
            false
        }
    }
}