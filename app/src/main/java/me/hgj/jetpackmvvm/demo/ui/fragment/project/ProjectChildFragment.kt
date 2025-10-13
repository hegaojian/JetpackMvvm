package me.hgj.jetpackmvvm.demo.ui.fragment.project

import android.os.Bundle
import androidx.lifecycle.LiveData
import me.hgj.jetpackmvvm.core.data.ApiResult
import me.hgj.jetpackmvvm.demo.app.core.base.BaseArticleListFragment
import me.hgj.jetpackmvvm.demo.data.model.entity.ApiPagerResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.ArticleResponse
import me.hgj.jetpackmvvm.demo.data.vm.ProjectViewModel
import me.hgj.jetpackmvvm.ext.util.intent.bundle

/**
 * 作者　: hegaojian
 * 时间　: 2020/2/28
 * 描述　: 项目子fragment
 */

class ProjectChildFragment : BaseArticleListFragment<ProjectViewModel, ArticleResponse>() {

    /** 是否是最新项目 */
    private var isNew by bundle(false)

    /** 该项目对应的id */
    private var cid by bundle(0)

    companion object {
        fun newInstance(cid: Int, isNew: Boolean): ProjectChildFragment {
            val args = Bundle()
            args.putInt("cid", cid)
            args.putBoolean("isNew", isNew)
            val fragment = ProjectChildFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun provideRequest(
        isRefresh: Boolean,
        loadingXml: Boolean
    ): LiveData<ApiResult<ApiPagerResponse<ArticleResponse>>> {
        return mViewModel.getProjectData(isRefresh,loadingXml,cid,isNew)
    }

}