package me.hgj.jetpackmvvm.demo.ui.fragment.web

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.Window
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.just.agentweb.AgentWeb
import kotlinx.android.synthetic.main.fragment_web.*
import kotlinx.android.synthetic.main.include_toolbar.*
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.base.BaseFragment
import me.hgj.jetpackmvvm.demo.app.ext.hideSoftKeyboard
import me.hgj.jetpackmvvm.demo.app.ext.initClose
import me.hgj.jetpackmvvm.demo.app.ext.showMessage
import me.hgj.jetpackmvvm.demo.data.model.bean.*
import me.hgj.jetpackmvvm.demo.data.model.enums.CollectType
import me.hgj.jetpackmvvm.demo.databinding.FragmentWebBinding
import me.hgj.jetpackmvvm.demo.viewmodel.request.RequestCollectViewModel
import me.hgj.jetpackmvvm.demo.viewmodel.state.WebViewModel
import me.hgj.jetpackmvvm.ext.getViewModel
import me.hgj.jetpackmvvm.ext.nav

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/3
 * 描述　:
 */
@Suppress("DEPRECATED_IDENTITY_EQUALS")
class WebFragment : BaseFragment<WebViewModel, FragmentWebBinding>() {

    private var mAgentWeb: AgentWeb? = null

    /** 注意，在by lazy中使用getViewModel一定要使用泛型，虽然他提示不报错，但是你不写是不行的 */
    private val requestCollectViewModel: RequestCollectViewModel by lazy { getViewModel<RequestCollectViewModel>() }

    override fun layoutId() = R.layout.fragment_web

    override fun initView(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        arguments?.run {
            //点击文章进来的
            getParcelable<AriticleResponse>("ariticleData")?.let {
                mViewModel.ariticleId = it.id
                mViewModel.showTitle = it.title
                mViewModel.collect = it.collect
                mViewModel.url = it.link
                mViewModel.collectType = CollectType.Ariticle.type
            }
            //点击首页轮播图进来的
            getParcelable<BannerResponse>("bannerdata")?.let {
                mViewModel.ariticleId = it.id
                mViewModel.showTitle = it.title
                //从首页轮播图 没法判断是否已经收藏过，所以直接默认没有收藏
                mViewModel.collect = false
                mViewModel.url = it.url
                mViewModel.collectType = CollectType.Url.type
            }
            //从收藏文章列表点进来的
            getParcelable<CollectResponse>("collect")?.let {
                mViewModel.ariticleId = it.originId
                mViewModel.showTitle = it.title
                //从收藏列表过来的，肯定 是 true 了
                mViewModel.collect = true
                mViewModel.url = it.link
                mViewModel.collectType = CollectType.Ariticle.type
            }
            //点击收藏网址列表进来的
            getParcelable<CollectUrlResponse>("collectUrl")?.let {
                mViewModel.ariticleId = it.id
                mViewModel.showTitle = it.name
                //从收藏列表过来的，肯定 是 true 了
                mViewModel.collect = true
                mViewModel.url = it.link
                mViewModel.collectType = CollectType.Url.type
            }
        }
        toolbar.run {
            //设置menu 关键代码
            (activity as? AppCompatActivity)?.setSupportActionBar(this)
            initClose(mViewModel.showTitle) {
                hideSoftKeyboard(activity)
                nav().navigateUp()
            }
        }
    }

    override fun lazyLoadData() {
        //加载网页
        mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(webcontent, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .createAgentWeb()
            .ready()
            .go(mViewModel.url)
        //添加返回键逻辑
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    mAgentWeb?.let {
                        if (it.webCreator.webView.canGoBack()) {
                            it.webCreator.webView.goBack()
                        } else {
                            nav().navigateUp()
                        }
                    }
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun createObserver() {
        requestCollectViewModel.collectUiState.observe(viewLifecycleOwner, Observer {
            if (it.isSuccess) {
                mViewModel.collect = it.collect
                eventViewModel.collect.postValue(CollectBus(it.id, it.collect))
                //刷新一下menu
                activity?.window?.invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL)
                activity?.invalidateOptionsMenu()
            } else {
                showMessage(it.errorMsg)
            }
        })
        requestCollectViewModel.collectUrlUiState.observe(viewLifecycleOwner, Observer {
            if (it.isSuccess) {
                eventViewModel.collect.postValue(CollectBus(it.id, it.collect))
                mViewModel.collect = it.collect
                //刷新一下menu
                activity?.window?.invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL)
                activity?.invalidateOptionsMenu()
            } else {
                showMessage(it.errorMsg)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.web_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        //如果收藏了，右上角的图标相对应改变
        context?.let {
            if (mViewModel.collect) {
                menu.findItem(R.id.web_collect).icon =
                    ContextCompat.getDrawable(it, R.drawable.ic_collected)
            } else {
                menu.findItem(R.id.web_collect).icon =
                    ContextCompat.getDrawable(it, R.drawable.ic_collect)
            }
        }

        return super.onPrepareOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.web_share -> {
                //分享
                startActivity(Intent.createChooser(Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "$mViewModel.showTitle:$mViewModel.url")
                    type = "text/plain"
                }, "分享到"))
            }
            R.id.web_refresh -> {
                //刷新网页
                mAgentWeb?.urlLoader?.reload()
            }
            R.id.web_collect -> {
                //点击收藏
                //是否已经登录了，没登录需要跳转到登录页去
                if (shareViewModel.isLogin.value) {
                    //是否已经收藏了
                    if (mViewModel.collect) {
                        if (mViewModel.collectType == CollectType.Url.type) {
                            //取消收藏网址
                            requestCollectViewModel.uncollectUrl(mViewModel.ariticleId)
                        } else {
                            //取消收藏文章
                            requestCollectViewModel.uncollect(mViewModel.ariticleId)
                        }
                    } else {
                        if (mViewModel.collectType == CollectType.Url.type) {
                            //收藏网址
                            requestCollectViewModel.collectUrl(mViewModel.showTitle, mViewModel.url)
                        } else {
                            //收藏文章
                            requestCollectViewModel.collect(mViewModel.ariticleId)
                        }
                    }
                } else {
                    //跳转到登录页
                    nav().navigate(R.id.action_webFragment_to_loginFragment)
                }
            }
            R.id.web_liulanqi -> {
                //用浏览器打开
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(mViewModel.url)))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        mAgentWeb?.webLifeCycle?.onPause()
        super.onPause()
    }

    override fun onResume() {
        mAgentWeb?.webLifeCycle?.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        mAgentWeb?.webLifeCycle?.onDestroy()
        (activity as? AppCompatActivity)?.setSupportActionBar(null)
        super.onDestroy()
    }

}