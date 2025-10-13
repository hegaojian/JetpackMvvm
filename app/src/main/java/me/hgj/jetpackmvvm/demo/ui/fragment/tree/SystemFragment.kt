package me.hgj.jetpackmvvm.demo.ui.fragment.tree

import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import me.hgj.jetpackmvvm.core.data.ApiResult
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.core.base.BaseListFragment
import me.hgj.jetpackmvvm.demo.app.core.ext.nav
import me.hgj.jetpackmvvm.demo.app.core.util.LocalDataUtil
import me.hgj.jetpackmvvm.demo.data.model.entity.ClassifyResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.SystemResponse
import me.hgj.jetpackmvvm.demo.databinding.FlowLayoutBinding
import me.hgj.jetpackmvvm.demo.databinding.ItemSystemBinding
import me.hgj.jetpackmvvm.demo.ui.fragment.MainFragmentDirections
import me.hgj.jetpackmvvm.demo.data.vm.TreeViewModel
import me.hgj.jetpackmvvm.ext.util.toHtml
import me.hgj.jetpackmvvm.ext.view.divider
import me.hgj.jetpackmvvm.ext.view.flex
import me.hgj.jetpackmvvm.ext.view.vertical
import me.hgj.jetpackmvvm.util.decoration.DividerOrientation

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/3
 * 描述　: 体系
 */
class SystemFragment : BaseListFragment<TreeViewModel, SystemResponse>() {

    override fun provideRequest(
        isRefresh: Boolean,
        loadingXml: Boolean
    ): LiveData<ApiResult<ArrayList<SystemResponse>>> {
        return mViewModel.getTreeTitleData(loadingXml = loadingXml)
    }

    override fun setupAdapter(rv: RecyclerView) {
        mBind.rv.vertical().divider {
            includeVisible = true
            setDivider(8, true)
            orientation = DividerOrientation.VERTICAL
        }.setup outSetup@{
            addType<SystemResponse>(R.layout.item_system)
            // 视图在onCreate可以避免子列表滑动过程反复回调
            onCreate {
                //这里绑定的是子recycleView,防止重复调用
                getBindingOrNull<ItemSystemBinding>()?.let {
                    it.itemSystemRv.flex().setup {
                        addType<ClassifyResponse>(R.layout.flow_layout)
                        onBind {
                            val model = getModel<ClassifyResponse>()
                            val binding = getBinding<FlowLayoutBinding>()
                            binding.flowTag.text = model.name.toHtml()
                            binding.flowTag.setTextColor(LocalDataUtil.randomColor())
                        }
                        R.id.flow_tag.onClick {
                            val model = getModel<ClassifyResponse>()
                            val parentModels = this@outSetup.models as List<SystemResponse>
                            val index = parentModels.indexOfFirst { it.id == model.parentChapterId }
                            if(index == -1) return@onClick
                            val parentModel = this@outSetup.models?.get(index) as SystemResponse
                            nav().navigate(
                                MainFragmentDirections.toSystemArrFragment(
                                    parentModel,
                                    modelPosition
                                )
                            )
                        }
                    }
                }
                onBind {
                    val model = getModel<SystemResponse>()
                    val binding = getBinding<ItemSystemBinding>()
                    binding.itemSystemTitle.text = model.name
                    binding.itemSystemRv.models = model.children
                }
            }
        }
    }
}