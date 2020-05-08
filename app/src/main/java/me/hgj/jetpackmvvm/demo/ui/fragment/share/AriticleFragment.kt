package me.hgj.jetpackmvvm.demo.ui.fragment.share

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.kingja.loadsir.core.LoadService
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.include_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*
import kotlinx.android.synthetic.main.include_toolbar.*
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.base.BaseFragment
import me.hgj.jetpackmvvm.demo.app.ext.*
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.EmptyCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.ErrorCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.LoadingCallback
import me.hgj.jetpackmvvm.demo.app.weight.recyclerview.SpaceItemDecoration
import me.hgj.jetpackmvvm.demo.databinding.FragmentListBinding
import me.hgj.jetpackmvvm.demo.ui.adapter.ShareAdapter
import me.hgj.jetpackmvvm.demo.viewmodel.request.RequestAriticleViewModel
import me.hgj.jetpackmvvm.demo.viewmodel.state.AriticleViewModel
import me.hgj.jetpackmvvm.ext.getViewModel
import me.hgj.jetpackmvvm.ext.nav

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/2
 * 描述　:
 */
class AriticleFragment : BaseFragment<AriticleViewModel, FragmentListBinding>() {

    //适配器
    private val articleAdapter: ShareAdapter by lazy { ShareAdapter(arrayListOf()) }

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    //记得要写泛型，虽然在 by lazy中 提示不用写，但是你不写就会报错
    private val requestViewModel: RequestAriticleViewModel by lazy { getViewModel<RequestAriticleViewModel>() }

    override fun layoutId() = R.layout.fragment_list

    override fun initView(savedInstanceState: Bundle?) {
        toolbar.run {
            initClose("我分享的文章") {
                nav().navigateUp()
            }
            inflateMenu(R.menu.todo_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.todo_add -> {
                        nav().navigate(R.id.action_ariticleFragment_to_addAriticleFragment)
                    }
                }
                true
            }
        }
        //状态页配置
        loadsir = LoadServiceInit(swipeRefresh) {
            //点击重试时触发的操作
            loadsir.showCallback(LoadingCallback::class.java)
            requestViewModel.getShareData(true)
        }

        //初始化recyclerView
        recyclerView.init(LinearLayoutManager(context), articleAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            it.initFooter(SwipeRecyclerView.LoadMoreListener {
                //触发加载更多时请求数据
                requestViewModel.getShareData(false)
            })
            //初始化FloatingActionButton
            it.initFloatBtn(floatbtn)
        }
        //初始化 SwipeRefreshLayout
        swipeRefresh.init {
            //触发刷新监听时请求数据
            requestViewModel.getShareData(true)
        }

        articleAdapter.run {
            setNbOnItemClickListener { adapter, view, position ->
                nav().navigate(R.id.action_ariticleFragment_to_webFragment, Bundle().apply {
                    putParcelable("ariticleData", articleAdapter.data[position])
                })
            }
            addChildClickViewIds(R.id.item_share_del)
            setNbOnItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.item_share_del -> {
                        showMessage("确定删除该文章吗？", positiveButtonText = "删除", positiveAction = {
                            requestViewModel.deleteShareData(
                                articleAdapter.data[position].id,
                                position
                            )
                        }, negativeButtonText = "取消")
                    }
                }
            }
        }
    }

    override fun lazyLoadData() {
        requestViewModel.getShareData(true)
    }

    override fun createObserver() {
        requestViewModel.shareDataState.observe(viewLifecycleOwner, Observer {
            swipeRefresh.isRefreshing = false
            recyclerView.loadMoreFinish(it.isEmpty, it.hasMore)
            if (it.isSuccess) {
                //成功
                when {
                    //第一页并没有数据 显示空布局界面
                    it.isFirstEmpty -> {
                        loadsir.showCallback(EmptyCallback::class.java)
                    }
                    //是第一页
                    it.isRefresh -> {
                        loadsir.showSuccess()
                        articleAdapter.setNewInstance(it.listData)
                    }
                    //不是第一页
                    else -> {
                        loadsir.showSuccess()
                        articleAdapter.addData(it.listData)
                    }
                }
            } else {
                //失败
                if (it.isRefresh) {
                    //如果是第一页，则显示错误界面，并提示错误信息
                    loadsir.setErrorText(it.errMessage)
                    loadsir.showCallback(ErrorCallback::class.java)
                } else {
                    recyclerView.loadMoreError(0, it.errMessage)
                }
            }
        })
        requestViewModel.delDataState.observe(viewLifecycleOwner, Observer {
            if (it.isSuccess) {
                //删除成功 如果是删除的最后一个了，那么直接把界面设置为空布局
                if (articleAdapter.data.size == 1) {
                    loadsir.showCallback(EmptyCallback::class.java)
                }
                articleAdapter.remove(it.data)
            } else {
                //删除失败，提示
                showMessage(it.errorMsg)
            }
        })
        eventViewModel.shareArticle.observe(viewLifecycleOwner, Observer {
            if (articleAdapter.data.size == 0) {
                //界面没有数据时，变为加载中 增强一丢丢体验
                loadsir.showCallback(LoadingCallback::class.java)
            } else {
                //有数据时，swipeRefresh 显示刷新状态
                swipeRefresh.isRefreshing = true
            }
            requestViewModel.getShareData(true)
        })
    }
}