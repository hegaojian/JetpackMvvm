package me.hgj.jetpackmvvm.demo.ui.fragment.publicNumber

import android.os.Bundle
import androidx.lifecycle.LiveData
import me.hgj.jetpackmvvm.core.data.ApiResult
import me.hgj.jetpackmvvm.demo.app.core.base.BaseArticleListFragment
import me.hgj.jetpackmvvm.demo.data.model.entity.ApiPagerResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.ArticleResponse
import me.hgj.jetpackmvvm.demo.data.vm.PublicNumberViewModel
import me.hgj.jetpackmvvm.ext.util.intent.bundle

/**
 * 作者　: hegaojian
 * 时间　: 2020/2/29
 * 描述　: 公众号子Fragment
 */
class PublicChildFragment : BaseArticleListFragment<PublicNumberViewModel, ArticleResponse>() {

    //该公众号对应的id
    private var cid by bundle(0)

    companion object {
        fun newInstance(cid: Int): PublicChildFragment {
            val args = Bundle()
            args.putInt("cid", cid)
            val fragment = PublicChildFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun provideRequest(
        isRefresh: Boolean,
        loadingXml: Boolean
    ): LiveData<ApiResult<ApiPagerResponse<ArticleResponse>>> {
        return  mViewModel.getPublicNumberData(refresh  = isRefresh, loadingXml = loadingXml,cid)
    }

}