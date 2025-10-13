package me.hgj.jetpackmvvm.demo.ui.fragment.search

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.setup
import me.hgj.jetpackmvvm.core.data.obs
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.core.base.BaseFragment
import me.hgj.jetpackmvvm.demo.app.core.ext.initClose
import me.hgj.jetpackmvvm.demo.app.core.ext.nav
import me.hgj.jetpackmvvm.demo.app.core.util.LocalDataUtil
import me.hgj.jetpackmvvm.demo.data.model.entity.SearchResponse
import me.hgj.jetpackmvvm.demo.databinding.FlowLayoutBinding
import me.hgj.jetpackmvvm.demo.databinding.FragmentSearchBinding
import me.hgj.jetpackmvvm.demo.databinding.ItemHistoryBinding
import me.hgj.jetpackmvvm.demo.data.vm.SearchViewModel
import me.hgj.jetpackmvvm.ext.util.clickNoRepeat
import me.hgj.jetpackmvvm.ext.view.flex
import me.hgj.jetpackmvvm.ext.view.showDialogMessage
import me.hgj.jetpackmvvm.ext.view.vertical

/**
 * 作者　: hegaojian
 * 时间　: 2020/2/29
 * 描述　:
 */

class SearchFragment : BaseFragment<SearchViewModel, FragmentSearchBinding>() {

    override val showTitle = true

    override fun initView(savedInstanceState: Bundle?) {
        initToolbar()
        setupMenu()
        initHot()
        initHistory()
    }

    override fun onBindViewClick() {
        mBind.searchClear.clickNoRepeat {
            showDialogMessage(
                "确定清空历史搜索吗?",
                positiveButtonText = "清空",
                negativeButtonText = "取消",
                positiveAction = {
                    mViewModel.historyCacheData.clear()
                    mViewModel.updateHistoryCache()
                    mBind.searchHistoryRv.bindingAdapter.models = arrayListOf()
                }
            )
        }
    }

    private fun initHistory() {
        mBind.searchHistoryRv.vertical().setup {
            addType<String>(R.layout.item_history)
            onBind {
                val model = getModel<String>()
                val binding = getBinding<ItemHistoryBinding>()
                binding.itemHistoryText.text = model
            }
            R.id.item_history_del.onClick {
                this@setup.notifyItemRemoved(modelPosition)
                mViewModel.historyCacheData.remove(getModel())
                mViewModel.updateHistoryCache()
            }
            R.id.history_root.onClick {
                val key = getModel<String>()
                updateKey(key)
                nav().navigate(SearchFragmentDirections.toSearchResultFragment(key))
            }
        }.models = mViewModel.historyCacheData
    }

    private fun initHot() {
        mBind.searchHotRv.flex().setup {
            addType<SearchResponse>(R.layout.flow_layout)
            onBind {
                val model = getModel<SearchResponse>()
                val binding = getBinding<FlowLayoutBinding>()
                binding.flowTag.text = model.name
                binding.flowTag.setTextColor(LocalDataUtil.randomColor())
            }
            R.id.flow_tag.onClick {
                val key = getModel<SearchResponse>().name
                updateKey(key)
                nav().navigate(SearchFragmentDirections.toSearchResultFragment(key))
            }
        }
    }

    override fun lazyLoadData() {
        onLoadRetry()
    }

    override fun onLoadRetry() {
        mViewModel.getHotSearchData().obs(viewLifecycleOwner) {
            onSuccess {
                mBind.searchHotRv.bindingAdapter.models = it
            }
        }
    }

    /**
     * 初始化 Toolbar
     */
    private fun initToolbar() {
        baseBinding.includeToolbar.toolbar.run {
            mActivity.setSupportActionBar(this)
            initClose { nav().navigateUp() }
        }
    }


    /**
     * 更新搜索词
     */
    fun updateKey(keyStr: String) {
        mViewModel.historyCacheData.let {
            if (it.contains(keyStr)) {
                //当搜索历史中包含该数据时 删除
                it.remove(keyStr)
            } else if (it.size >= 10) {
                //如果集合的size 有10个以上了，删除最后一个
                it.removeAt(it.size - 1)
            }
            //添加新数据到第一条
            it.add(0, keyStr)
            mViewModel.updateHistoryCache()
            mBind.searchHistoryRv.bindingAdapter.notifyItemRangeChanged(0,it.size)
        }
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_search, menu)

                val searchItem = menu.findItem(R.id.action_search)
                val searchView = searchItem?.actionView as? SearchView ?: return

                searchView.apply {
                    maxWidth = Int.MAX_VALUE
                    onActionViewExpanded()
                    queryHint = "输入关键字搜索"
                    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            query?.takeIf { it.isNotBlank() }?.let { queryStr ->
                                updateKey(queryStr)
                                nav().navigate(SearchFragmentDirections.toSearchResultFragment(queryStr))
                            }
                            clearFocus()
                            return true
                        }

                        override fun onQueryTextChange(newText: String?): Boolean = false
                    })

                    isSubmitButtonEnabled = true
                    this.findViewById<ImageView>(androidx.appcompat.R.id.search_go_btn)?.setImageResource(R.drawable.ic_search)
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // 可以在这里处理菜单点击
                return false
            }
        }, viewLifecycleOwner) // 生命周期感知
    }

    override fun onDestroy() {
        super.onDestroy()
        mActivity.setSupportActionBar(null)
    }

    override fun getLoadingView() = mBind.searchHotRv
}