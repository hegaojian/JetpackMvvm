package me.hgj.jetpackmvvm.demo.ui.adapter

import android.text.TextUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.ext.setAdapterAnimation
import me.hgj.jetpackmvvm.demo.app.util.SettingUtil
import me.hgj.jetpackmvvm.demo.app.weight.customview.CollectView
import me.hgj.jetpackmvvm.demo.data.model.bean.CollectResponse
import me.hgj.jetpackmvvm.ext.util.toHtml


class CollectAdapter(data: ArrayList<CollectResponse>) :
    BaseDelegateMultiAdapter<CollectResponse, BaseViewHolder>(data) {
    //文章类型
    private val Ariticle = 1
    //项目类型 本来打算不区分文章和项目布局用统一布局的，但是布局完以后发现差异化蛮大的，所以还是分开吧
    private val Project = 2
    private var collectAction: (item: CollectResponse, v: CollectView, position: Int) -> Unit =
        { _: CollectResponse, _: CollectView, _: Int -> }

    init {

        setAdapterAnimation(SettingUtil.getListMode())

        // 第一步，设置代理
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<CollectResponse>() {
            override fun getItemType(data: List<CollectResponse>, position: Int): Int {
                //根据是否有图片 判断为文章还是项目，好像有点low的感觉。。。我看实体类好像没有相关的字段，就用了这个，也有可能是我没发现
                return if (TextUtils.isEmpty(data[position].envelopePic)) Ariticle else Project
            }
        })
        // 第二步，绑定 item 类型
        getMultiTypeDelegate()?.let {
            it.addItemType(Ariticle, R.layout.item_ariticle)
            it.addItemType(Project, R.layout.item_project)
        }
    }

    override fun convert(holder: BaseViewHolder, item: CollectResponse) {
        when (holder.itemViewType) {
            Ariticle -> {
                //文章布局的赋值
                item.run {
                    holder.setText(R.id.item_home_author, if (author.isEmpty()) "匿名用户" else author)
                    holder.setText(R.id.item_home_content, title.toHtml())
                    holder.setText(R.id.item_home_type2, chapterName.toHtml())
                    holder.setText(R.id.item_home_date, niceDate)
                    holder.getView<CollectView>(R.id.item_home_collect).isChecked = true
                    //隐藏所有标签
                    holder.setGone(R.id.item_home_top, true)
                    holder.setGone(R.id.item_home_type1, true)
                    holder.setGone(R.id.item_home_new, true)
                }
                holder.getView<CollectView>(R.id.item_home_collect)
                    .setOnCollectViewClickListener(object : CollectView.OnCollectViewClickListener {
                        override fun onClick(v: CollectView) {
                            collectAction.invoke(item, v, holder.adapterPosition)
                        }
                    })
            }
            Project -> {
                //项目布局的赋值
                item.run {
                    holder.setText(
                        R.id.item_project_author,
                        if (author.isEmpty()) "匿名用户" else author
                    )
                    holder.setText(R.id.item_project_title, title.toHtml())
                    holder.setText(R.id.item_project_content, desc.toHtml())
                    holder.setText(R.id.item_project_type, chapterName.toHtml())
                    holder.setText(R.id.item_project_date, niceDate)
                    //隐藏所有标签
                    holder.setGone(R.id.item_project_top, true)
                    holder.setGone(R.id.item_project_type1, true)
                    holder.setGone(R.id.item_project_new, true)
                    holder.getView<CollectView>(R.id.item_project_collect).isChecked = true
                    Glide.with(context).load(envelopePic)
                        .transition(DrawableTransitionOptions.withCrossFade(500))
                        .into(holder.getView(R.id.item_project_imageview))
                }
                holder.getView<CollectView>(R.id.item_project_collect)
                    .setOnCollectViewClickListener(object : CollectView.OnCollectViewClickListener {
                        override fun onClick(v: CollectView) {
                            collectAction.invoke(item, v, holder.adapterPosition)
                        }
                    })
            }
        }

    }

    fun setCollectClick(inputCollectAction: (item: CollectResponse, v: CollectView, position: Int) -> Unit) {
        this.collectAction = inputCollectAction
    }

}


