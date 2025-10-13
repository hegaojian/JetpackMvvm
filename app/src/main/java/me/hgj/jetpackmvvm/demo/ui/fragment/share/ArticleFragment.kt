package me.hgj.jetpackmvvm.demo.ui.fragment.share

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.drake.brv.utils.setup
import me.hgj.jetpackmvvm.core.data.ApiResult
import me.hgj.jetpackmvvm.core.data.obs
import me.hgj.jetpackmvvm.demo.MainNavigationDirections
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.core.base.BasePageListFragment
import me.hgj.jetpackmvvm.demo.app.core.ext.nav
import me.hgj.jetpackmvvm.demo.app.event.EventViewModel
import me.hgj.jetpackmvvm.demo.data.model.entity.ApiPagerResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.ArticleResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.BannerResponse
import me.hgj.jetpackmvvm.demo.data.vm.ShareViewModel
import me.hgj.jetpackmvvm.demo.databinding.ItemShareAriticleBinding
import me.hgj.jetpackmvvm.demo.ui.activity.WebActivity
import me.hgj.jetpackmvvm.demo.ui.fragment.MainFragmentDirections
import me.hgj.jetpackmvvm.ext.view.divider
import me.hgj.jetpackmvvm.ext.view.showDialogMessage
import me.hgj.jetpackmvvm.ext.view.vertical
import me.hgj.jetpackmvvm.util.decoration.DividerOrientation

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/2
 * 描述　: 我分享的文章
 */
class ArticleFragment : BasePageListFragment<ShareViewModel, ArticleResponse>() {

    override val showTitle = true

    override val title = "我分享的文章"

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        baseBinding.includeToolbar.toolbar.run {
            inflateMenu(R.menu.todo_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.todo_add -> {
                        nav().navigate(MainFragmentDirections.toAddArticleFragment())
                    }
                }
                true
            }
        }
    }

    override fun provideRequest(
        isRefresh: Boolean,
        loadingXml: Boolean
    ): LiveData<ApiResult<ApiPagerResponse<ArticleResponse>>> {
        return mViewModel.getShareArticleData(refresh = isRefresh, loadingXml = loadingXml)
    }

    override fun setupAdapter(rv: RecyclerView) {
        mBind.rv.vertical().divider {
            includeVisible = true
            setDivider(8, true)
            orientation = DividerOrientation.VERTICAL
        }.setup {
                addType<ArticleResponse>(R.layout.item_share_ariticle)
                onBind {
                    val model = getModel<ArticleResponse>()
                    val binding = getBinding<ItemShareAriticleBinding>()
                    binding.itemShareTitle.text = model.title
                    binding.itemShareDate.text = model.niceDate
                }
                R.id.item_share_del.onClick {
                    val model = getModel<ArticleResponse>()
                    showDialogMessage(
                        "确定删除该文章吗?",
                        positiveButtonText = "删除",
                        negativeButtonText = "取消",
                        positiveAction = {
                            mViewModel.delShareArticleData(model.id).obs(this@ArticleFragment) {
                                onSuccess {
                                    this@setup.mutable.removeAt(modelPosition)
                                    this@setup.notifyItemRemoved(modelPosition)
                                    if(this@setup.mutable.isEmpty()) {
                                        onLoadRetry()
                                    }
                                }
                                onError {
                                    showDialogMessage(it.msg)
                                }
                            }
                        }
                    )
                }
                R.id.item_share_root.onClick {
                    val model = getModel<ArticleResponse>()
                    WebActivity.start(article = model)
                }
            }
    }

    override fun createObserver() {
        EventViewModel.shareArticleEvent.observe(viewLifecycleOwner){
            onLoadRetry()
        }
    }

}