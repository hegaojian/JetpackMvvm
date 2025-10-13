package me.hgj.jetpackmvvm.demo.ui.adapter

import androidx.recyclerview.widget.RecyclerView
import com.drake.brv.utils.setup
import com.zhpan.bannerview.BannerViewPager
import me.hgj.jetpackmvvm.demo.MainNavigationDirections
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.core.ext.nav
import me.hgj.jetpackmvvm.demo.app.core.widget.banner.HomeBannerAdapter
import me.hgj.jetpackmvvm.demo.app.core.widget.customview.CollectView
import me.hgj.jetpackmvvm.demo.data.model.entity.ArticleResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.BannerParentResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.BannerResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.CollectResponse
import me.hgj.jetpackmvvm.demo.databinding.IncludeBannerBinding
import me.hgj.jetpackmvvm.demo.databinding.ItemAriticleBinding
import me.hgj.jetpackmvvm.demo.databinding.ItemProjectBinding
import me.hgj.jetpackmvvm.demo.ui.activity.WebActivity
import me.hgj.jetpackmvvm.ext.util.load
import me.hgj.jetpackmvvm.ext.util.toHtml
import me.hgj.jetpackmvvm.ext.view.gone
import me.hgj.jetpackmvvm.ext.view.visibleOrGone

/**
 * 作者　：hegaojian
 * 时间　：2025/9/26
 * 说明　：
 */

/** 封装所有的文章+项目 统一 一个Adapter，这里是用了BRV库
 *  @param showTag 是否展示标签 tag ,主页才需要展示
 *  @param onCollectClick 点击收藏回调
 *  @param onBannerInit Banner初始化绑定生命周期
 */
fun RecyclerView.articleAdapter(showTag: Boolean = false,
                                canJumpLookInfo: Boolean = true,
                                onCollectClick: (ArticleResponse, Boolean, (Boolean) -> Unit) -> Unit = { _, _, _ ->},
                                onBannerInit: ((BannerViewPager<BannerResponse>) -> Unit)? = null) =
    this.setup {
        addType<ArticleResponse> {
            if (envelopePic.isEmpty()) {
                R.layout.item_ariticle
            } else {
                R.layout.item_project
            }
        }
        addType<BannerParentResponse>(R.layout.include_banner)
        onCreate {
            //在onCreate这里设置view的监听方法，防止重复设置
            getBindingOrNull<ItemAriticleBinding>()?.run {
                itemHomeCollect.setOnCollectViewClickListener(object : CollectView.OnCollectViewClickListener {
                    override fun onClick(v: CollectView) {
                        val model = getModel<ArticleResponse>()
                        // 把点击事件交出去，让UI层决定怎么请求
                        onCollectClick(model, v.isChecked) { success ->
                            if (!success) v.isChecked = !v.isChecked
                        }
                    }
                })
            }
            getBindingOrNull<ItemProjectBinding>()?.run {
                itemProjectCollect.setOnCollectViewClickListener(object : CollectView.OnCollectViewClickListener {
                    override fun onClick(v: CollectView) {
                        val model = getModel<ArticleResponse>()
                        // 把点击事件交出去，让UI层决定怎么请求
                        onCollectClick(model, v.isChecked) { success ->
                            if (!success) v.isChecked = !v.isChecked
                        }
                    }
                })
            }
        }
        onBind {
            getBindingOrNull<ItemAriticleBinding>()?.run {
                //文章
                val model = getModel<ArticleResponse>()
                itemHomeAuthor.text = model.author.ifEmpty { model.shareUser }
                itemHomeContent.text = model.title.toHtml()
                itemHomeType2.text = "${model.superChapterName}·${model.chapterName}".toHtml()
                itemHomeDate.text = model.niceDate
                itemHomeCollect.isChecked = model.collect
                if (showTag) {
                    itemHomeNew.visibleOrGone(model.fresh)
                    itemHomeTop.visibleOrGone(model.type == 1)
                    itemHomeType1.visibleOrGone(model.tags.isNotEmpty())
                    itemHomeType1.text = model.tags.getOrNull(0)?.name ?: ""
                } else {
                    itemHomeNew.gone()
                    itemHomeTop.gone()
                    itemHomeType1.gone()
                }
            }

            getBindingOrNull<ItemProjectBinding>()?.run {
                //项目
                val model = getModel<ArticleResponse>()
                itemProjectAuthor.text = model.author.ifEmpty { model.shareUser }
                itemProjectTitle.text = model.title.toHtml()
                itemProjectContent.text = model.desc.toHtml()
                itemProjectType.text = "${model.superChapterName}·${model.chapterName}".toHtml()
                itemProjectDate.text = model.niceDate
                if (showTag) {
                    itemProjectNew.visibleOrGone(model.fresh)
                    itemProjectTop.visibleOrGone(model.type == 1)
                    itemProjectType1.visibleOrGone(model.tags.isNotEmpty())
                    itemProjectType1.text = model.tags.getOrNull(0)?.name ?: ""
                } else {
                    itemProjectNew.gone()
                    itemProjectTop.gone()
                    itemProjectType1.gone()
                }
                itemProjectCollect.isChecked = model.collect
                itemProjectImageview.load(model.envelopePic)
            }
            getBindingOrNull<IncludeBannerBinding>()?.run {
                    val mViewPager: BannerViewPager<BannerResponse> = this.bannerView as BannerViewPager<BannerResponse>
                    val model = getModel<BannerParentResponse>()
                    mViewPager.adapter = HomeBannerAdapter()
                    onBannerInit?.invoke(mViewPager)
                    mViewPager.setOnPageClickListener { _, position ->
                        WebActivity.start(banner = model.banners[position])
                    }
                    mViewPager.create(model.banners)
            }
        }
        onClick(R.id.item_home_author, R.id.item_project_author) {
            if(!canJumpLookInfo) return@onClick
            val model = getModel<ArticleResponse>()
            nav(this@articleAdapter).navigate(MainNavigationDirections.toLookInfoFragment(model.userId,model.author.ifEmpty { model.shareUser }))
        }
        onClick(R.id.item_article_root, R.id.item_project_root) {
            WebActivity.start(article = getModel<ArticleResponse>())
        }
    }