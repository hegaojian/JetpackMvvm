package me.hgj.jetpackmvvm.demo.app.core.widget.banner

/**
 * 作者　: hegaojian
 * 时间　: 2020/2/20
 * 描述　:
 */

import android.widget.ImageView
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.data.model.entity.BannerResponse
import me.hgj.jetpackmvvm.ext.util.load

class HomeBannerAdapter : BaseBannerAdapter<BannerResponse>() {

    override fun bindData(
        holder: BaseViewHolder<BannerResponse>,
        data: BannerResponse?,
        position: Int,
        pageSize: Int
    ) {
        holder.findViewById<ImageView>(R.id.bannerhome_img).load(data!!.imagePath)
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.banner_itemhome;
    }

}
