package me.hgj.jetpackmvvm.demo.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.ext.setAdapterAnimion
import me.hgj.jetpackmvvm.demo.app.util.ColorUtil
import me.hgj.jetpackmvvm.demo.app.util.SettingUtil
import me.hgj.jetpackmvvm.demo.data.bean.SearchResponse

class SearcHotAdapter(data: ArrayList<SearchResponse>) :
    BaseQuickAdapter<SearchResponse, BaseViewHolder>(R.layout.flow_layout, data) {

    init {
        setAdapterAnimion(SettingUtil.getListMode())
    }

    override fun convert(helper: BaseViewHolder, item: SearchResponse) {
        helper.setText(R.id.flow_tag, item.name)
        helper.setTextColor(R.id.flow_tag, ColorUtil.randomColor())
    }

}