package me.hgj.mvvm_nb_demo.app.weight.banner

/**
 * 作者　: hegaojian
 * 时间　: 2020/2/20
 * 描述　:
 */

import android.view.View
import android.widget.TextView
import com.zhpan.bannerview.holder.ViewHolder
import me.hgj.mvvm_nb_demo.R

class WelcomeBannerViewHolder : ViewHolder<String> {
    override fun getLayoutId(): Int {
        return R.layout.banner_itemwelcome
    }

    override fun onBind(itemView: View, data: String?, position: Int, size: Int) {
        val textView = itemView.findViewById<TextView>(R.id.banner_text)
        textView.text = data
    }

}
