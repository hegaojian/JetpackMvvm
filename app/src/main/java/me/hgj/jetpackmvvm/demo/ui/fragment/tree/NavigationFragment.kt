package me.hgj.jetpackmvvm.demo.ui.fragment.tree

import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import me.hgj.jetpackmvvm.core.data.ApiResult
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.core.base.BaseListFragment
import me.hgj.jetpackmvvm.demo.app.core.util.LocalDataUtil
import me.hgj.jetpackmvvm.demo.data.model.entity.ArticleResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.BannerResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.NavigationResponse
import me.hgj.jetpackmvvm.demo.databinding.FlowLayoutBinding
import me.hgj.jetpackmvvm.demo.databinding.ItemSystemBinding
import me.hgj.jetpackmvvm.demo.data.vm.TreeViewModel
import me.hgj.jetpackmvvm.demo.ui.activity.WebActivity
import me.hgj.jetpackmvvm.ext.util.toHtml
import me.hgj.jetpackmvvm.ext.view.divider
import me.hgj.jetpackmvvm.ext.view.flex
import me.hgj.jetpackmvvm.ext.view.vertical
import me.hgj.jetpackmvvm.util.decoration.DividerOrientation

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/3
 * 描述　: 导航
 */
class NavigationFragment : BaseListFragment<TreeViewModel, NavigationResponse>() {

    override fun provideRequest(
        isRefresh: Boolean,
        loadingXml: Boolean
    ): LiveData<ApiResult<ArrayList<NavigationResponse>>> {
        return mViewModel.getNavigationData(loadingXml = loadingXml)
    }

    override fun setupAdapter(rv: RecyclerView) {
        mBind.rv.vertical().divider {
            includeVisible = true
            setDivider(8, true)
            orientation = DividerOrientation.VERTICAL
        }.setup {
            addType<NavigationResponse>(R.layout.item_system)
            // 视图在onCreate可以避免子列表滑动过程反复回调
            onCreate {
                //这里绑定的是子recycleView,防止重复调用
                getBindingOrNull<ItemSystemBinding>()?.let {
                    it.itemSystemRv.flex().setup {
                        addType<ArticleResponse>(R.layout.flow_layout)
                        onBind {
                            val model = getModel<ArticleResponse>()
                            val binding = getBinding<FlowLayoutBinding>()
                            binding.flowTag.text = model.title.toHtml()
                            binding.flowTag.setTextColor(LocalDataUtil.randomColor())
                        }
                        R.id.flow_tag.onClick {
                            val model = getModel<ArticleResponse>()
                            WebActivity.start(banner = BannerResponse(title = model.title, url = model.link))
                        }
                    }
                }
            }
            onBind {
                val model = getModel<NavigationResponse>()
                val binding = getBinding<ItemSystemBinding>()
                binding.itemSystemTitle.text = model.name
                binding.itemSystemRv.models = model.articles
            }
        }
    }

}