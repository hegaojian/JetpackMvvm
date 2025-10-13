package me.hgj.jetpackmvvm.demo.ui.fragment.search

import android.os.Bundle
import androidx.lifecycle.LiveData
import me.hgj.jetpackmvvm.core.data.ApiResult
import me.hgj.jetpackmvvm.demo.app.core.base.BaseArticleListFragment
import me.hgj.jetpackmvvm.demo.app.core.ext.initClose
import me.hgj.jetpackmvvm.demo.app.core.ext.nav
import me.hgj.jetpackmvvm.demo.data.model.entity.ApiPagerResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.ArticleResponse
import me.hgj.jetpackmvvm.demo.data.vm.SearchViewModel
import me.hgj.jetpackmvvm.ext.util.intent.bundle

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/2
 * 描述　: 搜索结果
 */
class SearchResultFragment : BaseArticleListFragment<SearchViewModel, ArticleResponse>() {

    private var searchKey by bundle("")

    override val showTitle = true

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        baseBinding.includeToolbar.toolbar.initClose(searchKey){
            nav().navigateUp()
        }
    }

    override fun provideRequest(
        isRefresh: Boolean,
        loadingXml: Boolean
    ): LiveData<ApiResult<ApiPagerResponse<ArticleResponse>>> {
        return mViewModel.getSearchDataByKey(searchKey,isRefresh,loadingXml)
    }
}