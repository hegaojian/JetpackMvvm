package me.hgj.jetpackmvvm.demo.ui.fragment.home

import android.os.Bundle
import androidx.lifecycle.LiveData
import com.drake.brv.utils.bindingAdapter
import me.hgj.jetpackmvvm.core.data.ApiResult
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.core.base.BaseArticleListFragment
import me.hgj.jetpackmvvm.demo.app.core.ext.init
import me.hgj.jetpackmvvm.demo.app.core.ext.nav
import me.hgj.jetpackmvvm.demo.data.model.entity.ApiPagerResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.ArticleResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.BannerParentResponse
import me.hgj.jetpackmvvm.demo.data.vm.HomeViewModel
import me.hgj.jetpackmvvm.demo.ui.fragment.MainFragmentDirections

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/27
 * 描述　:
 */
class HomeFragment : BaseArticleListFragment<HomeViewModel, ArticleResponse>() {

    override val showTitle = true

    override val isHome = true

    companion object {
        fun newInstance(): HomeFragment {
            val args = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        // 这里比较特殊，头部自定义
        baseBinding.includeToolbar.toolbar.run {
            init("首页")
            inflateMenu(R.menu.home_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.home_search -> {
                        nav().navigate(MainFragmentDirections.toSearchFragment())
                    }
                }
                true
            }
        }
    }

    override fun homeBanner() {
        //这里是判断如果adapter中如果没有添加Banner，那么添加一个Banner进去
        val bannerData = mBind.rv.bindingAdapter.models?.filterIsInstance<BannerParentResponse>()
        if(bannerData.isNullOrEmpty()){
            mBind.rv.bindingAdapter.addModels(listOf(BannerParentResponse(banners = mViewModel.bannerData)), index = 0)
        }
    }

    override fun provideRequest(
        isRefresh: Boolean,
        loadingXml: Boolean
    ): LiveData<ApiResult<ApiPagerResponse<ArticleResponse>>> {
        return  mViewModel.getHomeData(isRefresh = isRefresh, loadingXml = loadingXml)
    }
}