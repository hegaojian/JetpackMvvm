package me.hgj.jetpackmvvm.demo.ui.search

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.kingja.loadsir.core.LoadService
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.include_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*
import kotlinx.android.synthetic.main.include_toolbar.*
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.adapter.AriticleAdapter
import me.hgj.jetpackmvvm.demo.app.base.BaseFragment
import me.hgj.jetpackmvvm.demo.app.ext.*
import me.hgj.jetpackmvvm.demo.app.util.CacheUtil
import me.hgj.jetpackmvvm.demo.app.weight.customview.CollectView
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.EmptyCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.ErrorCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.LoadingCallback
import me.hgj.jetpackmvvm.demo.app.weight.recyclerview.DefineLoadMoreView
import me.hgj.jetpackmvvm.demo.app.weight.recyclerview.SpaceItemDecoration
import me.hgj.jetpackmvvm.demo.data.bean.AriticleResponse
import me.hgj.jetpackmvvm.demo.data.bean.CollectBus
import me.hgj.jetpackmvvm.demo.databinding.FragmentListBinding
import me.hgj.jetpackmvvm.ext.parseState
import me.hgj.jetpackmvvm.navigation.NavHostFragment

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/2
 * 描述　:
 */
class SearchResultFragment : BaseFragment<SearchViewModel, FragmentListBinding>() {

    private var searchKey = ""

    //适配器
    private val articleAdapter: AriticleAdapter by lazy { AriticleAdapter(arrayListOf(), true) }

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>
    //recyclerview的底部加载view 因为要动态改变他的颜色，所以加了他这个属性
    private lateinit var footView: DefineLoadMoreView

    override fun layoutId() = R.layout.fragment_list

    override fun initView() {
        arguments?.let { arguments -> arguments.getString("searchKey")?.let { searchKey = it } }
        toolbar.initClose(searchKey) {
            Navigation.findNavController(toolbar).navigateUp()
        }
        //状态页配置
        loadsir = LoadServiceInit(swipeRefresh) {
            //点击重试时触发的操作
            loadsir.showCallback(LoadingCallback::class.java)
            mViewModel.getSearchResultData(searchKey, true)
        }

        //初始化recyclerView
        recyclerView.init(LinearLayoutManager(context), articleAdapter).let {
            it as SwipeRecyclerView
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            footView = it.initFooter(SwipeRecyclerView.LoadMoreListener {
                //触发加载更多时请求数据
                mViewModel.getSearchResultData(searchKey, false)
            })
            //初始化FloatingActionButton
            it.initFloatBtn(floatbtn)
        }
        //初始化 SwipeRefreshLayout
        swipeRefresh.init {
            //触发刷新监听时请求数据
            mViewModel.getSearchResultData(searchKey, true)
        }

        articleAdapter.run {
            setOnCollectViewClickListener(object :
                AriticleAdapter.OnCollectViewClickListener {
                override fun onClick(item: AriticleResponse, v: CollectView, position: Int) {
                    if (CacheUtil.isLogin()) {
                        if (v.isChecked) {
                            mViewModel.uncollect(item.id)
                        } else {
                            mViewModel.collect(item.id)
                        }
                    } else {
                        v.isChecked = true
                        Navigation.findNavController(v)
                            .navigate(R.id.action_searchResultFragment_to_loginFragment)
                    }
                }
            })
            setOnItemClickListener { adapter, view, position ->
                NavHostFragment.findNavController(this@SearchResultFragment)
                    .navigate(R.id.action_searchResultFragment_to_webFragment, Bundle().apply {
                        putSerializable("ariticleData", articleAdapter.data[position])
                    })
            }
            addChildClickViewIds(R.id.item_home_author)
            addChildClickViewIds(R.id.item_project_author)
            setOnItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.item_home_author, R.id.item_project_author -> {
                        Navigation.findNavController(view).navigate(
                            R.id.action_searchResultFragment_to_lookInfoFragment,
                            Bundle().apply {
                                putInt("id", articleAdapter.data[position].userId)
                            })
                    }
                }
            }
        }

    }

    override fun lazyLoadData() {
        mViewModel.getSearchResultData(searchKey, true)
    }

    override fun createObserver() {
        mViewModel.seachResultData.observe(viewLifecycleOwner, Observer { viewState ->
            parseState(viewState, {
                swipeRefresh.isRefreshing = false
                //请求成功，页码+1
                mViewModel.pageNo++
                if (it.isRefresh() && it.datas.size == 0) {
                    //如果是第一页，并且没有数据，页面提示空布局
                    loadsir.showCallback(EmptyCallback::class.java)
                } else if (it.isRefresh()) {
                    //如果是刷新的，有数据
                    loadsir.showSuccess()
                    articleAdapter.setNewData(it.datas)
                } else {
                    //不是第一页
                    loadsir.showSuccess()
                    articleAdapter.addData(it.datas)
                }
                recyclerView.loadMoreFinish(it.isEmpty(), it.hasMore())
            }, {
                //这里代表请求失败
                swipeRefresh.isRefreshing = false
                if (articleAdapter.data.size == 0) {
                    //如果适配器数据没有值，则显示错误界面，并提示错误信息
                    loadsir.setErrorText(it.errorMsg)
                    loadsir.showCallback(ErrorCallback::class.java)
                } else {
                    recyclerView.loadMoreError(0, it.errorMsg)
                }
            })
        })
        mViewModel.collectUiState.observe(viewLifecycleOwner, Observer {
            if (it.isSuccess) {
                appViewModel.collect.postValue(CollectBus(it.id, it.collect))
            } else {
                showMessage(it.errorMsg)
                for (index in articleAdapter.data.indices) {
                    if (articleAdapter.data[index].id == it.id) {
                        articleAdapter.data[index].collect = it.collect
                        articleAdapter.notifyItemChanged(index)
                        break
                    }
                }
            }
        })
        appViewModel.run {
            //监听账户信息是否改变 有值时(登录)将相关的数据设置为已收藏，为空时(退出登录)，将已收藏的数据变为未收藏
            userinfo.observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    it.collectIds.forEach { id ->
                        for (item in articleAdapter.data) {
                            if (id.toInt() == item.id) {
                                item.collect = true
                                break
                            }
                        }
                    }
                } else {
                    for (item in articleAdapter.data) {
                        item.collect = false
                    }
                }
                articleAdapter.notifyDataSetChanged()
            })

            //监听全局的收藏信息 收藏的Id跟本列表的数据id匹配则需要更新
            collect.observe(viewLifecycleOwner, Observer {
                for (index in articleAdapter.data.indices) {
                    if (articleAdapter.data[index].id == it.id) {
                        articleAdapter.data[index].collect = it.collect
                        articleAdapter.notifyItemChanged(index)
                        break
                    }
                }
            })

        }

    }
}