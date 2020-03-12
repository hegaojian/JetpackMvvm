package me.hgj.jetpackmvvm.demo.adapter

import android.text.Html
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.ext.setAdapterAnimion
import me.hgj.jetpackmvvm.demo.app.util.SettingUtil
import me.hgj.jetpackmvvm.demo.app.weight.customview.CollectView
import me.hgj.jetpackmvvm.demo.data.bean.CollectUrlResponse


class CollectUrlAdapter(data: ArrayList<CollectUrlResponse>) :
    BaseQuickAdapter<CollectUrlResponse, BaseViewHolder>(
        R.layout.item_collecturl, data
    ) {

    private var mOnCollectViewClickListener: OnCollectViewClickListener? = null

    init {
        setAdapterAnimion(SettingUtil.getListMode())
    }
    override fun convert(helper: BaseViewHolder, item: CollectUrlResponse) {
        //赋值
        item.run {
            helper.setText(R.id.item_collecturl_name, Html.fromHtml(name))
            helper.setText(R.id.item_collecturl_link, link)
            helper.getView<CollectView>(R.id.item_collecturl_collect).isChecked = true
        }
        helper.getView<CollectView>(R.id.item_collecturl_collect)
            .setOnCollectViewClickListener(object : CollectView.OnCollectViewClickListener {
                override fun onClick(v: CollectView) {
                    mOnCollectViewClickListener?.onClick(item, v, helper.adapterPosition)
                }
            })
    }

    fun setOnCollectViewClickListener(onCollectViewClickListener: OnCollectViewClickListener) {
        mOnCollectViewClickListener = onCollectViewClickListener
    }

    interface OnCollectViewClickListener {
        fun onClick(item: CollectUrlResponse, v: CollectView, position: Int)
    }
}


