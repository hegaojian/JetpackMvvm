package me.hgj.jetpackmvvm.demo.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import com.just.agentweb.AgentWeb
import me.hgj.jetpackmvvm.core.data.obs
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.core.base.BaseActivity
import me.hgj.jetpackmvvm.demo.app.core.ext.hideSoftKeyboard
import me.hgj.jetpackmvvm.demo.app.core.ext.initClose
import me.hgj.jetpackmvvm.demo.app.core.util.UserManager
import me.hgj.jetpackmvvm.demo.app.event.EventViewModel
import me.hgj.jetpackmvvm.demo.data.model.entity.ArticleResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.BannerResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.CollectBus
import me.hgj.jetpackmvvm.demo.data.model.entity.CollectResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.CollectUrlResponse
import me.hgj.jetpackmvvm.demo.data.model.enums.CollectType
import me.hgj.jetpackmvvm.demo.data.vm.CollectViewModel
import me.hgj.jetpackmvvm.demo.data.vm.WebViewModel
import me.hgj.jetpackmvvm.demo.databinding.ActivityWebBinding
import me.hgj.jetpackmvvm.ext.lifecycle.getViewModel
import me.hgj.jetpackmvvm.ext.util.currentActivity
import me.hgj.jetpackmvvm.ext.util.getDrawableExt
import me.hgj.jetpackmvvm.ext.util.intent.bundle
import me.hgj.jetpackmvvm.ext.util.intent.openActivity


/**
 * 作者　: hegaojian
 * 时间　: 2020/3/3
 * 描述　:
 */
class WebActivity : BaseActivity<WebViewModel, ActivityWebBinding>() {

    override val showTitle = true

    private var mAgentWeb: AgentWeb? = null

    private var preWeb: AgentWeb.PreAgentWeb? = null

    val article: ArticleResponse? by bundle()

    val banner: BannerResponse? by bundle()

    val collect: CollectResponse? by bundle()

    val collectUrl: CollectUrlResponse? by bundle()

    /** 收藏viewModel */
    val collectVm: CollectViewModel by getViewModel()

    companion object {
        fun start(
            banner: BannerResponse? = null,
            article: ArticleResponse? = null,
            collect: CollectResponse? = null,
            collectUrl: CollectUrlResponse? = null
        ) {
            val extras = listOfNotNull(
                banner?.let { "banner" to it },
                article?.let { "article" to it },
                collect?.let { "collect" to it },
                collectUrl?.let { "collectUrl" to it }
            )
            (currentActivity as? AppCompatActivity)?.openActivity<WebActivity>(*extras.toTypedArray())
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        //点击文章进来的
        article?.let {
            mViewModel.ariticleId = it.id
            mViewModel.showTitle = it.title
            mViewModel.collect = it.collect
            mViewModel.url = it.link
            mViewModel.collectType = CollectType.Article.type
        }
        //点击首页轮播图进来的
        banner?.let {
            mViewModel.ariticleId = it.id
            mViewModel.showTitle = it.title
            //从首页轮播图 没法判断是否已经收藏过，所以直接默认没有收藏
            mViewModel.collect = false
            mViewModel.url = it.url
            mViewModel.collectType = CollectType.Url.type
        }
        //从收藏文章列表点进来的
        collect?.let {
            mViewModel.ariticleId = it.originId
            mViewModel.showTitle = it.title
            //从收藏列表过来的，肯定 是 true 了
            mViewModel.collect = true
            mViewModel.url = it.link
            mViewModel.collectType = CollectType.Article.type
        }
        //点击收藏网址列表进来的
        collectUrl?.let {
            mViewModel.ariticleId = it.id
            mViewModel.showTitle = it.name
            //从收藏列表过来的，肯定 是 true 了
            mViewModel.collect = true
            mViewModel.url = it.link
            mViewModel.collectType = CollectType.Url.type
        }
        mToolbar.run {
            //设置menu 关键代码
            setSupportActionBar(this)
            initClose(mViewModel.showTitle) {
                hideSoftKeyboard(this@WebActivity)
                mAgentWeb?.let { web ->
                    if (web.webCreator.webView.canGoBack()) {
                        web.webCreator.webView.goBack()
                    } else {
                        finish()
                    }
                }
            }
        }

        // 使用 MenuProvider 替代旧 API
        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.web_menu, menu)
                updateCollectIcon(menu)
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                return when (item.itemId) {
                    R.id.web_share -> {
                        sharePage(); true
                    }

                    R.id.web_refresh -> {
                        mAgentWeb?.urlLoader?.reload(); true
                    }

                    R.id.web_collect -> {
                        handleCollect(); true
                    }

                    R.id.web_liulanqi -> {
                        openInBrowser(); true
                    }

                    else -> false
                }
            }

            override fun onPrepareMenu(menu: Menu) {
                updateCollectIcon(menu)
            }
        }, this) // 生命周期绑定

        preWeb = AgentWeb.with(this)
            .setAgentWebParent(mBind.webContent, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .createAgentWeb()
            .ready()
        //加载网页
        mAgentWeb = preWeb?.go(mViewModel.url)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (mAgentWeb!!.handleKeyEvent(keyCode, event)) {
            return true
        }
        return super.onKeyDown(keyCode, event)
    }
    /**
     * 修改收藏的状态
     */
    private fun updateCollectIcon(menu: Menu) {
        val iconRes = if (mViewModel.collect) R.drawable.ic_collected else R.drawable.ic_collect
        menu.findItem(R.id.web_collect).icon = getDrawableExt(iconRes)
    }

    /**
     * 分享
     */
    private fun sharePage() {
        startActivity(
            Intent.createChooser(Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "${mViewModel.showTitle}:${mViewModel.url}")
                type = "text/plain"
            }, "分享到")
        )
    }

    /**
     * 浏览器打开
     */
    private fun openInBrowser() {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(mViewModel.url)))
    }

    /**
     * 收藏
     */
    private fun handleCollect() {
        if (!UserManager.isLoggedIn) {
            openActivity<LoginActivity>()
            return
        }

        val id = mViewModel.ariticleId
        val url = mViewModel.url
        val title = mViewModel.showTitle

        val doSuccess: (Boolean) -> Unit = { collected ->
            mViewModel.collect = collected
            EventViewModel.collectEvent.value = CollectBus(id, collected)
            refreshMenu()
        }

        if (mViewModel.collect) {
            // 已收藏 -> 取消收藏
            if (mViewModel.collectType == CollectType.Url.type) {
                collectVm.unCollectUrl(id).obs(this)  { onSuccess { doSuccess(false) } }
            } else {
                collectVm.unCollectArticle(id).obs(this)  { onSuccess { doSuccess(false) } }
            }
        } else {
            // 未收藏 -> 添加收藏
            if (mViewModel.collectType == CollectType.Url.type) {
                collectVm.collectUrl(title, url).obs(this)  { onSuccess { doSuccess(true) } }
            } else {
                collectVm.collectArticle(id).obs(this)  { onSuccess { doSuccess(true) } }
            }
        }
    }

    /**
     * 刷新
     */
    private fun refreshMenu() {
        invalidateOptionsMenu() // 会触发 onPrepareMenu
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
        super.onDestroy()
    }

}