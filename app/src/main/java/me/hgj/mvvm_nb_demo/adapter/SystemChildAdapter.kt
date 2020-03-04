package me.hgj.mvvm_nb_demo.adapter

import android.text.Html
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import me.hgj.mvvm_nb_demo.R
import me.hgj.mvvm_nb_demo.app.util.ColorUtil
import me.hgj.mvvm_nb_demo.data.bean.ClassifyResponse

class SystemChildAdapter(data: ArrayList<ClassifyResponse>) : BaseQuickAdapter<ClassifyResponse, BaseViewHolder>(R.layout.flow_layout,data) {

    override fun convert(helper: BaseViewHolder, item: ClassifyResponse) {
        helper.setText(R.id.flow_tag,Html.fromHtml(item.name))
        helper.setTextColor(R.id.flow_tag,ColorUtil.randomColor())
    }

}