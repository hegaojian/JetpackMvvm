package me.hgj.mvvm_nb_demo.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import me.hgj.mvvm_nb_demo.R
import me.hgj.mvvm_nb_demo.app.util.ColorUtil
import me.hgj.mvvm_nb_demo.data.bean.SearchResponse

class SearcHotAdapter(data: ArrayList<SearchResponse>) : BaseQuickAdapter<SearchResponse, BaseViewHolder>(R.layout.flow_layout,data) {

    override fun convert(helper: BaseViewHolder, item: SearchResponse) {
        helper.setText(R.id.flow_tag,item.name)
        helper.setTextColor(R.id.flow_tag,ColorUtil.randomColor())
    }

}