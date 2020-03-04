package me.hgj.mvvm_nb_demo.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import me.hgj.mvvm_nb_demo.R

class SearcHistoryAdapter(data: MutableList<String>) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_history,data) {
    override fun convert(helper: BaseViewHolder, item: String) {
        helper.setText(R.id.item_history_text,item)
    }

}