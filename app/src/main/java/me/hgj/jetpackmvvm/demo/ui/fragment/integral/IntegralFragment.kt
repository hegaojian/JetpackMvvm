package me.hgj.jetpackmvvm.demo.ui.fragment.integral

import android.os.Bundle
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.setup
import me.hgj.jetpackmvvm.core.data.obs
import me.hgj.jetpackmvvm.demo.MainNavigationDirections
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.core.base.BaseFragment
import me.hgj.jetpackmvvm.demo.app.core.ext.nav
import me.hgj.jetpackmvvm.demo.data.model.entity.BannerResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.IntegralResponse
import me.hgj.jetpackmvvm.demo.databinding.FragmentIntegralBinding
import me.hgj.jetpackmvvm.demo.databinding.ItemIntegralBinding
import me.hgj.jetpackmvvm.demo.data.vm.IntegralViewModel
import me.hgj.jetpackmvvm.demo.ui.activity.WebActivity
import me.hgj.jetpackmvvm.ext.util.intent.bundle
import me.hgj.jetpackmvvm.ext.util.loadListError
import me.hgj.jetpackmvvm.ext.util.loadListSuccess
import me.hgj.jetpackmvvm.ext.util.loadMore
import me.hgj.jetpackmvvm.ext.util.notNull
import me.hgj.jetpackmvvm.ext.util.refresh
import me.hgj.jetpackmvvm.ext.view.divider
import me.hgj.jetpackmvvm.ext.view.gone
import me.hgj.jetpackmvvm.ext.view.vertical
import me.hgj.jetpackmvvm.util.decoration.DividerOrientation

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/2
 * 描述　: 积分排行
 */
class IntegralFragment : BaseFragment<IntegralViewModel, FragmentIntegralBinding>() {

    override val showTitle = true

    override val title = "积分排行"

    private val rank: IntegralResponse? by bundle(null)

    override fun initView(savedInstanceState: Bundle?) {
        baseBinding.includeToolbar.toolbar.run {
            inflateMenu(R.menu.integral_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.integral_rules -> {
                        WebActivity.start(banner = BannerResponse(title = "积分规则",url = "https://www.wanandroid.com/blog/show/2653"))
                    }

                    R.id.integral_history -> {
                        nav().navigate(IntegralFragmentDirections.toIntegralHistoryFragment())
                    }
                }
                true
            }
        }
        rank.notNull({
            mBind.integralName.text = it.username
            mBind.integralRank.text = it.rank.toString()
            mBind.integralCount.text = it.coinCount.toString()
        }, {
            mBind.integralCardview.gone()
        })
        mBind.includeLayout.let {
            it.refresh.refresh {
                getList(isRefresh = true, loadingXml = false)
            }.loadMore {
                getList(isRefresh = false, loadingXml = false)
            }
            it.rv.vertical().divider {
                setDivider(8, true)
                includeVisible = true
                orientation = DividerOrientation.VERTICAL
            }.setup {
                addType<IntegralResponse>(R.layout.item_integral)
                onBind {
                    val model = getModel<IntegralResponse>()
                    val binding = getBinding<ItemIntegralBinding>()
                    binding.itemIntegralName.text = model.username
                    binding.itemIntegralRank.text = model.rank.toString()
                    binding.itemIntegralCount.text = model.coinCount.toString()
                }
            }
        }

    }

    override fun lazyLoadData() {
        onLoadRetry()
    }

    override fun onLoadRetry() {
        super.onLoadRetry()
        getList(isRefresh = true, loadingXml = true)
    }

    fun getList(isRefresh: Boolean = true, loadingXml: Boolean = false) {
        mViewModel.getIntegralRankData(isRefresh, loadingXml).obs(viewLifecycleOwner)  {
            onSuccess {
                loadListSuccess(it, mBind.includeLayout.rv.bindingAdapter,mBind.includeLayout.refresh,this@IntegralFragment)
            }
            onError {
                loadListError(it, mBind.includeLayout.refresh)
            }
        }
    }

    override fun getLoadingView() = mBind.includeLayout.refresh

}