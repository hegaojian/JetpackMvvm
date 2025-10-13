package me.hgj.jetpackmvvm.demo.ui.fragment.integral

import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.drake.brv.utils.setup
import me.hgj.jetpackmvvm.core.data.ApiResult
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.core.base.BasePageListFragment
import me.hgj.jetpackmvvm.demo.app.core.util.DatetimeUtil
import me.hgj.jetpackmvvm.demo.data.model.entity.ApiPagerResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.IntegralHistoryResponse
import me.hgj.jetpackmvvm.demo.databinding.ItemIntegralHistoryBinding
import me.hgj.jetpackmvvm.demo.data.vm.IntegralViewModel
import me.hgj.jetpackmvvm.ext.view.divider
import me.hgj.jetpackmvvm.ext.view.vertical
import me.hgj.jetpackmvvm.util.decoration.DividerOrientation

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/2
 * 描述　: 积分排行
 */
class IntegralHistoryFragment : BasePageListFragment<IntegralViewModel, IntegralHistoryResponse>() {

    override val showTitle = true

    override val title = "积分记录"

    override fun provideRequest(
        isRefresh: Boolean,
        loadingXml: Boolean
    ): LiveData<ApiResult<ApiPagerResponse<IntegralHistoryResponse>>> {
        return mViewModel.getIntegralHistoryData(isRefresh,loadingXml)
    }

    override fun setupAdapter(rv: RecyclerView) {
        rv.vertical().divider {
            setDivider(8,true)
            includeVisible = true
            orientation = DividerOrientation.VERTICAL
        }.setup {
            addType<IntegralHistoryResponse>(R.layout.item_integral_history)
            onBind {
                val model = getModel<IntegralHistoryResponse>()
                val binding = getBinding<ItemIntegralHistoryBinding>()
                val descStr = if (model.desc.contains("积分")) model.desc.subSequence(model.desc.indexOf("积分"), model.desc.length) else ""
                binding.itemIntegralhistoryTitle.text = "${model.reason}${descStr}"
                binding.itemIntegralhistoryCount.text = "+${model.coinCount}"
                binding.itemIntegralhistoryDate.text = DatetimeUtil.formatDate(model.date, DatetimeUtil.DATE_PATTERN_SS)
            }
        }
    }

}