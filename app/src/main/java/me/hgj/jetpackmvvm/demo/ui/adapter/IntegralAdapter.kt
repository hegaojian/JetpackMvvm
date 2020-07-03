package me.hgj.jetpackmvvm.demo.ui.adapter

import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.ext.setAdapterAnimation
import me.hgj.jetpackmvvm.demo.app.util.SettingUtil
import me.hgj.jetpackmvvm.demo.data.model.bean.IntegralResponse

/**
 * 积分排行 adapter
 * @Author:         hegaojian
 * @CreateDate:     2019/9/1 9:52
 */
class IntegralAdapter(data: ArrayList<IntegralResponse>) : BaseQuickAdapter<IntegralResponse, BaseViewHolder>(R.layout.item_integral, data) {
    private var rankNum: Int = -1

    constructor(data: ArrayList<IntegralResponse>, rank: Int) : this(data) {
        this.rankNum = rank
    }
    init {
        setAdapterAnimation(SettingUtil.getListMode())
    }
    override fun convert(holder: BaseViewHolder, item: IntegralResponse) {
        //赋值
        item.run {
            if(rankNum==holder.adapterPosition+1){
                holder.setTextColor(R.id.item_integral_rank, SettingUtil.getColor(context))
                holder.setTextColor(R.id.item_integral_name,SettingUtil.getColor(context))
                holder.setTextColor(R.id.item_integral_count,SettingUtil.getColor(context))
            }else{
                holder.setTextColor(R.id.item_integral_rank,ContextCompat.getColor(context,R.color.colorBlack333))
                holder.setTextColor(R.id.item_integral_name,ContextCompat.getColor(context,R.color.colorBlack666))
                holder.setTextColor(R.id.item_integral_count,ContextCompat.getColor(context,R.color.textHint))
            }
            holder.setText(R.id.item_integral_rank, "${holder.adapterPosition + 1}")
            holder.setText(R.id.item_integral_name, username)
            holder.setText(R.id.item_integral_count, coinCount.toString())
        }
    }
}


