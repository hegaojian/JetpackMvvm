package me.hgj.mvvm_nb_demo.ui.web

import android.content.Intent
import android.net.Uri
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.just.agentweb.AgentWeb
import kotlinx.android.synthetic.main.fragment_web.*
import kotlinx.android.synthetic.main.include_toolbar.*
import me.hgj.mvvm_nb_demo.R
import me.hgj.mvvm_nb_demo.app.base.BaseFragment
import me.hgj.mvvm_nb_demo.app.ext.hideSoftKeyboard
import me.hgj.mvvm_nb_demo.app.ext.initClose
import me.hgj.mvvm_nb_demo.app.util.CacheUtil
import me.hgj.mvvm_nb_demo.data.bean.AriticleResponse
import me.hgj.mvvm_nb_demo.data.bean.BannerResponse
import me.hgj.mvvm_nb_demo.data.bean.CollectResponse
import me.hgj.mvvm_nb_demo.data.bean.CollectUrlResponse
import me.hgj.mvvm_nb_demo.data.enums.CollectType
import me.hgj.mvvm_nb_demo.databinding.FragmentWebBinding

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/3
 * 描述　:
 */
class WebFragment : BaseFragment<WebViewModel, FragmentWebBinding>() {
    //是否收藏
    var collect = false
    //收藏的Id
    var ariticleId = 0
    //标题
    lateinit var showTitle: String
    //文章的网络访问路径
    lateinit var url: String
    //需要收藏的类型 具体参数说明请看 CollectType 枚举类
    private var collectType = 0

    private  var mAgentWeb: AgentWeb? = null

    override fun layoutId() = R.layout.fragment_web

    override fun initView() {
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
            val appCompatActivity = activity as AppCompatActivity?
            appCompatActivity?.let {
                it.setSupportActionBar(this)
            }
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
    }

    override fun createObserver() {

    }


    /* override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
         return if (mAgentWeb.handleKeyEvent(keyCode, event)) {
             true
         } else super.onKeyDown(keyCode, event)
     }*/


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

                        } else {
                            //取消收藏文章
                        }
                    } else {
                        if (collectType == CollectType.Url.type) {
                            //收藏网址

                        } else {
                            //收藏文章
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
        super.onDestroy()
    }
}