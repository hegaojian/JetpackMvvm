package me.hgj.mvvm_nb_demo.app.weight.banner

/**
 * 作者　: hegaojian
 * 时间　: 2020/2/20
 * 描述　:
 */

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.zhpan.bannerview.holder.ViewHolder
import me.hgj.mvvm_nb_demo.R
import me.hgj.mvvm_nb_demo.data.bean.BannerResponse

class HomeBannerViewHolder : ViewHolder<BannerResponse> {
    override fun getLayoutId(): Int {
        return R.layout.banner_itemhome
    }

    override fun onBind(itemView: View, data: BannerResponse?, position: Int, size: Int) {
        val img = itemView.findViewById<ImageView>(R.id.bannerhome_img)
        data?.let {
            Glide.with(itemView.context)
                .load(it.imagePath)
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .into(img)
        }

    }

}
