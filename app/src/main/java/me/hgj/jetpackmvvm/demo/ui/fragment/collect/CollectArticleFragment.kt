package me.hgj.jetpackmvvm.demo.ui.fragment.collect

import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.drake.brv.utils.setup
import me.hgj.jetpackmvvm.core.data.ApiResult
import me.hgj.jetpackmvvm.core.data.obs
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.core.base.BasePageListFragment
import me.hgj.jetpackmvvm.demo.app.core.widget.customview.CollectView
import me.hgj.jetpackmvvm.demo.app.event.EventViewModel
import me.hgj.jetpackmvvm.demo.data.model.entity.ApiPagerResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.CollectBus
import me.hgj.jetpackmvvm.demo.data.model.entity.CollectResponse
import me.hgj.jetpackmvvm.demo.data.vm.CollectViewModel
import me.hgj.jetpackmvvm.demo.databinding.ItemAriticleBinding
import me.hgj.jetpackmvvm.demo.databinding.ItemProjectBinding
import me.hgj.jetpackmvvm.demo.ui.activity.WebActivity
import me.hgj.jetpackmvvm.ext.util.load
import me.hgj.jetpackmvvm.ext.util.toHtml
import me.hgj.jetpackmvvm.ext.util.toast
import me.hgj.jetpackmvvm.ext.view.divider
import me.hgj.jetpackmvvm.ext.view.gone
import me.hgj.jetpackmvvm.ext.view.vertical
import me.hgj.jetpackmvvm.util.decoration.DividerOrientation

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/10
 * 描述　: 收藏的文章集合Fragment
 */
class CollectArticleFragment : BasePageListFragment<CollectViewModel, CollectResponse>() {
    override fun provideRequest(
        isRefresh: Boolean,
        loadingXml: Boolean
    ): LiveData<ApiResult<ApiPagerResponse<CollectResponse>>> {
        return mViewModel.getCollectArticleData(refresh = isRefresh, loadingXml = loadingXml)
    }

    override fun setupAdapter(rv: RecyclerView) {
        rv.vertical().divider {
            includeVisible = true
            setDivider(8, true)
            orientation = DividerOrientation.VERTICAL
        }.setup {
            onCreate {
                //在这里设置view的监听方法
                getBindingOrNull<ItemAriticleBinding>()?.run {
                    itemHomeCollect.setOnCollectViewClickListener(object :
                        CollectView.OnCollectViewClickListener {
                        override fun onClick(v: CollectView) {
                            val model = getModel<CollectResponse>()
                            mViewModel.unCollectOnCollect(model.id,model.originId).obs(this@CollectArticleFragment){
                                onSuccess {
                                    this@setup.mutable.removeAt(modelPosition)
                                    this@setup.notifyItemRemoved(modelPosition)
                                    if(this@setup.mutable.isEmpty()) {
                                        onLoadRetry()
                                    }
                                    EventViewModel.collectEvent.value = CollectBus(model.originId, false)
                                }
                                onError {
                                    v.isChecked = true
                                    it.msg.toast()
                                }
                            }
                        }
                    })
                }
                //在这里设置view的监听方法
                getBindingOrNull<ItemProjectBinding>()?.run {
                    itemProjectCollect.setOnCollectViewClickListener(object :
                        CollectView.OnCollectViewClickListener {
                        override fun onClick(v: CollectView) {
                            val model = getModel<CollectResponse>()
                            mViewModel.unCollectArticle(model.id).obs(this@CollectArticleFragment) {
                                onSuccess {
                                    this@setup.mutable.removeAt(modelPosition)
                                    this@setup.notifyItemRemoved(modelPosition)
                                    if(this@setup.mutable.isEmpty()) {
                                        onLoadRetry()
                                    }
                                    EventViewModel.collectEvent.value = CollectBus(model.id, false)
                                }
                                onError {
                                    v.isChecked = true
                                    it.msg.toast()
                                }
                            }
                        }
                    })
                }
            }
            addType<CollectResponse> {
                if (envelopePic.isEmpty()) {
                    R.layout.item_ariticle
                } else {
                    R.layout.item_project
                }
            }
            onBind {
                val model = getModel<CollectResponse>()
                getBindingOrNull<ItemAriticleBinding>()?.run {
                    //文章
                    itemHomeAuthor.text = model.author.ifEmpty { "匿名" }
                    itemHomeContent.text = model.title.toHtml()
                    itemHomeType2.text = model.chapterName.toHtml()
                    itemHomeDate.text = model.niceDate
                    itemHomeCollect.isChecked = true
                    itemHomeNew.gone()
                    itemHomeTop.gone()
                    itemHomeType1.gone()
                }

                getBindingOrNull<ItemProjectBinding>()?.run {
                    //项目
                    itemProjectAuthor.text = model.author.ifEmpty { "匿名" }
                    itemProjectTitle.text = model.title.toHtml()
                    itemProjectContent.text = model.desc.toHtml()
                    itemProjectType.text = model.chapterName.toHtml()
                    itemProjectDate.text = model.niceDate
                    itemProjectNew.gone()
                    itemProjectTop.gone()
                    itemProjectType1.gone()
                    itemProjectCollect.isChecked = true
                    itemProjectImageview.load(model.envelopePic)
                }
            }
            onClick(R.id.item_article_root, R.id.item_project_root) {
                WebActivity.start(collect = getModel<CollectResponse>())
            }
        }
    }
}