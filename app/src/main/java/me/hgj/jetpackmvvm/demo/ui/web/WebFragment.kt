package me.hgj.jetpackmvvm.demo.ui.web

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
import androidx.navigation.Navigation
import com.just.agentweb.AgentWeb
import kotlinx.android.synthetic.main.fragment_web.*
import kotlinx.android.synthetic.main.include_toolbar.*
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.CollectViewModel
import me.hgj.jetpackmvvm.demo.app.base.BaseFragment
import me.hgj.jetpackmvvm.demo.app.ext.hideSoftKeyboard
import me.hgj.jetpackmvvm.demo.app.ext.initClose
import me.hgj.jetpackmvvm.demo.app.ext.showMessage
import me.hgj.jetpackmvvm.demo.app.util.CacheUtil
import me.hgj.jetpackmvvm.demo.data.bean.*
import me.hgj.jetpackmvvm.demo.data.enums.CollectType
import me.hgj.jetpackmvvm.demo.databinding.FragmentWebBinding
import me.hgj.jetpackmvvm.navigation.NavHostFragment


/**
 * 作者　: hegaojian
 * 时间　: 2020/3/3
 * 描述　:
 */
@Suppress("DEPRECATED_IDENTITY_EQUALS")
class WebFragment : BaseFragment<CollectViewModel, FragmentWebBinding>() {
    //是否收藏
    var collect = false
    //收藏的Id
    var ariticleId = 0
    //标题
    private lateinit var showTitle: String
    //文章的网络访问路径
    lateinit var url: String
    //需要收藏的类型 具体参数说明请看 CollectType 枚举类
    private var collectType = 0

    private var mAgentWeb: AgentWeb? = null

    override fun layoutId() = R.layout.fragment_web

    override fun initView(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        arguments?.run {
            //点击文章进来的
            getSerializable("ariticleData")?.let {
                it as AriticleResponse
                ariticleId = it.id
                showTitle = it.title
                collect = it.collect
                url = it.link
                collectType = CollectType.Ariticle.type
            }
            //点击首页轮播图进来的
            getSerializable("bannerdata")?.let {
                it as BannerResponse
                ariticleId = it.id
                showTitle = it.title
                collect = false //从首页轮播图 没法判断是否已经收藏过，所以直接默认没有收藏
                url = it.url
                collectType = CollectType.Url.type
            }
            //从收藏文章列表点进来的
            getSerializable("collect")?.let {
                it as CollectResponse
                ariticleId = it.originId
                showTitle = it.title
                collect = true //从收藏列表过来的，肯定 是 true 了
                url = it.link
                collectType = CollectType.Ariticle.type
            }
            //点击收藏网址列表进来的
            getSerializable("collectUrl")?.let {
                it as CollectUrlResponse
                ariticleId = it.id
                showTitle = it.name
                collect = true//从收藏列表过来的，肯定 是 true 了
                url = it.link
                collectType = CollectType.Url.type
            }
        }
        toolbar.run {
            //设置menu 关键代码
            (activity as? AppCompatActivity)?.setSupportActionBar(this)
            initClose(showTitle) {
                hideSoftKeyboard(activity)
                Navigation.findNavController(it).navigateUp()
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
            .go(url)
        //添加返回键逻辑
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    mAgentWeb?.let {
                        if (it.webCreator.webView.canGoBack()) {
                            it.webCreator.webView.goBack()
                        } else {
                            NavHostFragment.findNavController(this@WebFragment).navigateUp()
                        }
                    }
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun createObserver() {
        mViewModel.collectUiState.observe(viewLifecycleOwner, Observer {
            if (it.isSuccess) {
                collect = it.collect
                appViewModel.collect.postValue(CollectBus(it.id, it.collect))
                //刷新一下menu
                activity?.window?.invalidatePanelMenu(Window.FEATURE_OPTIONS_PANEL)
                activity?.invalidateOptionsMenu()
            } else {
                showMessage(it.errorMsg)
            }
        })
        mViewModel.collectUrlUiState.observe(viewLifecycleOwner, Observer {
            if (it.isSuccess) {
                appViewModel.collect.postValue(CollectBus(it.id, it.collect))
                collect = it.collect
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
            if (collect) {
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
                    putExtra(Intent.EXTRA_TEXT, "$showTitle:$url")
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
                if (CacheUtil.isLogin()) {
                    //是否已经收藏了
                    if (collect) {
                        if (collectType == CollectType.Url.type) {
                            //取消收藏网址
                            mViewModel.uncollectUrl(ariticleId)
                        } else {
                            //取消收藏文章
                            mViewModel.uncollect(ariticleId)
                        }
                    } else {
                        if (collectType == CollectType.Url.type) {
                            //收藏网址
                            mViewModel.collectUrl(showTitle, url)
                        } else {
                            //收藏文章
                            mViewModel.collect(ariticleId)
                        }
                    }
                } else {
                    //跳转到登录页
                    Navigation.findNavController(webcontent)
                        .navigate(R.id.action_webFragment_to_loginFragment)
                }
            }
            R.id.web_liulanqi -> {
                //用浏览器打开
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
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