package me.hgj.jetpackmvvm.demo.app.core.widget.banner

/**
 * 作者　: hegaojian
 * 时间　: 2020/2/20
 * 描述　:
 */
import android.widget.TextView
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder
import me.hgj.jetpackmvvm.demo.R

class WelcomeBannerAdapter : BaseBannerAdapter<String>() {

    override fun bindData(
        holder: BaseViewHolder<String>,
        data: String?,
        position: Int,
        pageSize: Int
    ) {
        holder.findViewById<TextView>(R.id.banner_text).text = data
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.banner_itemwelcome;
    }
}
