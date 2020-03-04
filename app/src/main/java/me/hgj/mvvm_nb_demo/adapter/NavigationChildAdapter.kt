package me.hgj.mvvm_nb_demo.adapter

import android.text.Html
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import me.hgj.mvvm_nb_demo.R
import me.hgj.mvvm_nb_demo.app.util.ColorUtil
import me.hgj.mvvm_nb_demo.data.bean.AriticleResponse

class NavigationChildAdapter(data: ArrayList<AriticleResponse>) : BaseQuickAdapter<AriticleResponse, BaseViewHolder>(R.layout.flow_layout,data) {

    override fun convert(helper: BaseViewHolder, item: AriticleResponse) {
        helper.setText(R.id.flow_tag,Html.fromHtml(item.title))
        helper.setTextColor(R.id.flow_tag,ColorUtil.randomColor())
    }

}