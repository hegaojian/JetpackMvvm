package me.hgj.jetpackmvvm.demo.ui.fragment.collect

import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.setup
import me.hgj.jetpackmvvm.core.data.ApiResult
import me.hgj.jetpackmvvm.core.data.obs
import me.hgj.jetpackmvvm.demo.MainNavigationDirections
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.core.base.BaseListFragment
import me.hgj.jetpackmvvm.demo.app.event.EventViewModel
import me.hgj.jetpackmvvm.demo.app.core.ext.nav
import me.hgj.jetpackmvvm.demo.app.core.widget.customview.CollectView
import me.hgj.jetpackmvvm.demo.data.model.entity.CollectUrlResponse
import me.hgj.jetpackmvvm.demo.databinding.ItemCollecturlBinding
import me.hgj.jetpackmvvm.demo.data.vm.CollectViewModel
import me.hgj.jetpackmvvm.demo.ui.activity.WebActivity
import me.hgj.jetpackmvvm.ext.util.toast
import me.hgj.jetpackmvvm.ext.view.divider
import me.hgj.jetpackmvvm.ext.view.vertical
import me.hgj.jetpackmvvm.util.decoration.DividerOrientation

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/10
 * 描述　: 收藏的文章集合Fragment
 */
class CollectUrlFragment : BaseListFragment<CollectViewModel, CollectUrlResponse>() {

    override fun provideRequest(
        isRefresh: Boolean,
        loadingXml: Boolean
    ): LiveData<ApiResult<ArrayList<CollectUrlResponse>>> {
        return mViewModel.getCollectUrlData(refresh = isRefresh, loadingXml = loadingXml)
    }

    override fun setupAdapter(rv: RecyclerView) {
        rv.vertical().divider {
            includeVisible = true
            setDivider(8, true)
            orientation = DividerOrientation.VERTICAL
        }.setup {
            onCreate {
                //在这里设置view的监听方法
                getBindingOrNull<ItemCollecturlBinding>()?.run {
                    itemCollectUrlCollect.setOnCollectViewClickListener(object :
                        CollectView.OnCollectViewClickListener {
                        override fun onClick(v: CollectView) {
                            val model = getModel<CollectUrlResponse>()
                            mViewModel.unCollectUrl(model.id).obs(this@CollectUrlFragment){
                                onSuccess {
                                    this@setup.mutable.removeAt(modelPosition)
                                    this@setup.notifyItemRemoved(modelPosition)
                                    if(this@setup.mutable.isEmpty()) {
                                        onLoadRetry()
                                    }
                                }
                                onError {
                                    v.isChecked = true
                                    it.msg.toast()
                                }
                            }
                        }
                    })
                }
            }
            addType<CollectUrlResponse>(R.layout.item_collecturl)
            onBind {
                val model = getModel<CollectUrlResponse>()
                val binding = getBinding<ItemCollecturlBinding>()
                binding.itemCollectUrlName.text = model.name
                binding.itemCollectUrlLink.text = model.link
                binding.itemCollectUrlCollect.isChecked = true
            }
            onClick(R.id.item_collect_url_root) {
                WebActivity.start(collectUrl = getModel<CollectUrlResponse>())
            }
        }
    }

    override fun createObserver() {
        EventViewModel.collectEvent.observe(viewLifecycleOwner) {
            val models = mBind.rv.bindingAdapter.models
            models?.forEachIndexed { index, item ->
                if (item is CollectUrlResponse && item.id == it.id) {
                    if (!it.collect) {
                        mBind.rv.bindingAdapter.notifyItemChanged(index)
                        return@observe
                    }
                }
            }
            onLoadRetry()
        }
    }
}