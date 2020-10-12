package me.hgj.jetpackmvvm.demo.ui.adapter

import android.widget.Switch
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import me.hgj.jetpackmvvm.demo.R

/**
 * @author : hgj
 * @date   : 2020/9/7
 * 海王测试Adapter，以后海王再问 问题 发红包
 */

class TestAdapter(data: ArrayList<String>) :BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_integral, data) {

    var clickAction: (position: Int, item: String, state: Boolean) -> Unit = { _, _, _ -> }

    override fun convert(holder: BaseViewHolder, item: String) {
        holder.getView<Switch>(R.id.item_integral_rank).setOnCheckedChangeListener { _, isChecked ->
            clickAction.invoke(holder.adapterPosition, item, isChecked)
        }
    }
}


