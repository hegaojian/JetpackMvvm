package me.hgj.jetpackmvvm.demo.ui.fragment.tree

import android.os.Bundle
import androidx.lifecycle.LiveData
import me.hgj.jetpackmvvm.core.data.ApiResult
import me.hgj.jetpackmvvm.demo.app.core.base.BaseArticleListFragment
import me.hgj.jetpackmvvm.demo.data.model.entity.ApiPagerResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.ArticleResponse
import me.hgj.jetpackmvvm.demo.data.vm.TreeViewModel
import me.hgj.jetpackmvvm.ext.util.intent.bundle

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/3
 * 描述　: 体系栏目fragment
 */
class SystemChildFragment : BaseArticleListFragment<TreeViewModel, ArticleResponse>() {

    private var cid by bundle(-1)

    companion object {
        fun newInstance(cid: Int): SystemChildFragment {
            return SystemChildFragment().apply {
                arguments = Bundle().apply {
                    putInt("cid", cid)
                }
            }
        }
    }

    override fun provideRequest(
        isRefresh: Boolean,
        loadingXml: Boolean
    ): LiveData<ApiResult<ApiPagerResponse<ArticleResponse>>> {
        return mViewModel.getTreeChildData(isRefresh, loadingXml, cid)
    }

}