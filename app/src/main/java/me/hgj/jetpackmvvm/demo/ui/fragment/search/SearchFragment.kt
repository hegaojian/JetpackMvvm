package me.hgj.jetpackmvvm.demo.ui.fragment.search

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.WhichButton
import com.afollestad.materialdialogs.actions.getActionButton
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.include_toolbar.*
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.appViewModel
import me.hgj.jetpackmvvm.demo.app.base.BaseFragment
import me.hgj.jetpackmvvm.demo.app.ext.init
import me.hgj.jetpackmvvm.demo.app.ext.initClose
import me.hgj.jetpackmvvm.demo.app.ext.setUiTheme
import me.hgj.jetpackmvvm.demo.app.util.CacheUtil
import me.hgj.jetpackmvvm.demo.app.util.SettingUtil
import me.hgj.jetpackmvvm.demo.databinding.FragmentSearchBinding
import me.hgj.jetpackmvvm.demo.ui.adapter.SearcHistoryAdapter
import me.hgj.jetpackmvvm.demo.ui.adapter.SearcHotAdapter
import me.hgj.jetpackmvvm.demo.viewmodel.request.RequestSearchViewModel
import me.hgj.jetpackmvvm.demo.viewmodel.state.SearchViewModel
import me.hgj.jetpackmvvm.ext.nav
import me.hgj.jetpackmvvm.ext.navigateAction
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.ext.util.toJson

/**
 * 作者　: hegaojian
 * 时间　: 2020/2/29
 * 描述　:
 */

class SearchFragment : BaseFragment<SearchViewModel, FragmentSearchBinding>() {

    private val historyAdapter: SearcHistoryAdapter by lazy { SearcHistoryAdapter(arrayListOf()) }

    private val hotAdapter: SearcHotAdapter by lazy { SearcHotAdapter(arrayListOf()) }

    private val requestSearchViewModel: RequestSearchViewModel by viewModels()

    override fun layoutId() = R.layout.fragment_search

    override fun initView(savedInstanceState: Bundle?) {
        setMenu()
        appViewModel.appColor.value?.let { setUiTheme(it, search_text1, search_text2) }
        //初始化搜搜历史Recyclerview
        search_historyRv.init(LinearLayoutManager(context), historyAdapter, false)
        //初始化热门Recyclerview
        val layoutManager = FlexboxLayoutManager(context)
        //方向 主轴为水平方向，起点在左端
        layoutManager.flexDirection = FlexDirection.ROW
        //左对齐
        layoutManager.justifyContent = JustifyContent.FLEX_START
        search_hotRv.init(layoutManager, hotAdapter, false)

        historyAdapter.run {
            setOnItemClickListener { adapter, view, position ->
                val queryStr = historyAdapter.data[position]
                updateKey(queryStr)
                nav().navigateAction(R.id.action_searchFragment_to_searchResultFragment,
                    Bundle().apply {
                        putString("searchKey", queryStr)
                    }
                )
            }
            addChildClickViewIds(R.id.item_history_del)
            setOnItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.item_history_del -> {
                        requestSearchViewModel.historyData.value?.let {
                            it.removeAt(position)
                            requestSearchViewModel.historyData.value= it
                        }
                    }
                }
            }
        }

        hotAdapter.run {
            setOnItemClickListener { adapter, view, position ->
                val queryStr = hotAdapter.data[position].name
                updateKey(queryStr)
                nav().navigateAction(R.id.action_searchFragment_to_searchResultFragment,
                    Bundle().apply {
                        putString("searchKey", queryStr)
                    }
                )
            }
        }

        search_clear.setOnClickListener {
            activity?.let {
                MaterialDialog(it)
                    .cancelable(false)
                    .lifecycleOwner(this)
                    .show {
                        title(text = "温馨提示")
                        message(text = "确定清空吗?")
                        negativeButton(text = "取消")
                        positiveButton(text = "清空") {
                            //清空
                            requestSearchViewModel.historyData.value = arrayListOf()
                        }
                        getActionButton(WhichButton.POSITIVE).updateTextColor(
                            SettingUtil.getColor(
                                it
                            )
                        )
                        getActionButton(WhichButton.NEGATIVE).updateTextColor(
                            SettingUtil.getColor(
                                it
                            )
                        )
                    }
            }
        }
    }

    override fun createObserver() {
        requestSearchViewModel.run {
            //监听热门数据变化
            hotData.observe(viewLifecycleOwner, Observer {resultState->
                parseState(resultState, {
                    hotAdapter.setList(it)
                })
            })
            //监听历史数据变化
            historyData.observe(viewLifecycleOwner, Observer {
                historyAdapter.data = it
                historyAdapter.notifyDataSetChanged()
                CacheUtil.setSearchHistoryData(it.toJson())

            })
        }
    }

    override fun lazyLoadData() {
        //获取历史搜索词数据
        requestSearchViewModel.getHistoryData()
        //获取热门数据
        requestSearchViewModel.getHotData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_search, menu)
        val searchView = menu.findItem(R.id.action_search)?.actionView as SearchView
        searchView.run {
            maxWidth = Integer.MAX_VALUE
            onActionViewExpanded()
            queryHint = "输入关键字搜索"
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                //searchview的监听
                override fun onQueryTextSubmit(query: String?): Boolean {
                    //当点击搜索时 输入法的搜索，和右边的搜索都会触发
                    query?.let { queryStr ->
                        updateKey(queryStr)
                        nav().navigateAction(R.id.action_searchFragment_to_searchResultFragment,
                            Bundle().apply {
                                putString("searchKey", queryStr)
                            }
                        )
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
            isSubmitButtonEnabled = true //右边是否展示搜索图标
            val field = javaClass.getDeclaredField("mGoButton")
            field.run {
                isAccessible = true
                val mGoButton = get(searchView) as ImageView
                mGoButton.setImageResource(R.drawable.ic_search)
            }
        }
        super.onCreateOptionsMenu(menu, inflater)
    }


    /**
     * 更新搜索词
     */
    fun updateKey(keyStr: String) {
        requestSearchViewModel.historyData.value?.let {
            if (it.contains(keyStr)) {
                //当搜索历史中包含该数据时 删除
                it.remove(keyStr)
            } else if (it.size >= 10) {
                //如果集合的size 有10个以上了，删除最后一个
                it.removeAt(it.size - 1)
            }
            //添加新数据到第一条
            it.add(0, keyStr)
            requestSearchViewModel.historyData.value = it
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        //当该Fragment重新获得视图时，重新设置Menu，防止退出WebFragment ActionBar被清空后，导致该界面的ActionBar无法显示bug
        if (!hidden) {
            setMenu()
        }
    }

    private fun setMenu() {
        setHasOptionsMenu(true)
        toolbar.run {
            //设置menu 关键代码
            mActivity.setSupportActionBar(this)
            initClose {
                nav().navigateUp()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mActivity.setSupportActionBar(null)
    }
}