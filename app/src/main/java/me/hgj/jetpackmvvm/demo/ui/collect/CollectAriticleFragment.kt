package me.hgj.jetpackmvvm.demo.ui.collect

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.kingja.loadsir.core.LoadService
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.include_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.adapter.CollectAdapter
import me.hgj.jetpackmvvm.demo.app.CollectViewModel
import me.hgj.jetpackmvvm.demo.app.base.BaseFragment
import me.hgj.jetpackmvvm.demo.app.ext.*
import me.hgj.jetpackmvvm.demo.app.weight.customview.CollectView
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.EmptyCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.ErrorCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.LoadingCallback
import me.hgj.jetpackmvvm.demo.app.weight.recyclerview.DefineLoadMoreView
import me.hgj.jetpackmvvm.demo.app.weight.recyclerview.SpaceItemDecoration
import me.hgj.jetpackmvvm.demo.data.bean.CollectBus
import me.hgj.jetpackmvvm.demo.data.bean.CollectResponse
import me.hgj.jetpackmvvm.demo.databinding.IncludeListBinding

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/10
 * 描述　: 收藏的文章集合Fragment
 */
class CollectAriticleFragment : BaseFragment<CollectViewModel, IncludeListBinding>() {

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>
    //recyclerview的底部加载view 因为要动态改变他的颜色，所以加了他这个属性
    private lateinit var footView: DefineLoadMoreView
    private val articleAdapter: CollectAdapter by lazy { CollectAdapter(arrayListOf()) }

    override fun layoutId() = R.layout.include_list

    override fun initView(savedInstanceState: Bundle?)  {
        //状态页配置
        loadsir = LoadServiceInit(swipeRefresh) {
            //点击重试时触发的操作
            loadsir.showCallback(LoadingCallback::class.java)
            mViewModel.getCollectAriticleData(true)
        }
        //初始化recyclerView
        recyclerView.init(LinearLayoutManager(context), articleAdapter).let {
            it as SwipeRecyclerView
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            footView = it.initFooter(SwipeRecyclerView.LoadMoreListener {
                mViewModel.getCollectAriticleData(false)
            })
            //初始化FloatingActionButton
            it.initFloatBtn(floatbtn)
        }
        //初始化 SwipeRefreshLayout
        swipeRefresh.init {
            //触发刷新监听时请求数据
            mViewModel.getCollectAriticleData(true)
        }
        articleAdapter.run {
            setOnCollectViewClickListener(object :
                CollectAdapter.OnCollectViewClickListener {
                override fun onClick(item: CollectResponse, v: CollectView, position: Int) {
                    if (v.isChecked) {
                        mViewModel.uncollect(item.originId)
                    } else {
                        mViewModel.collect(item.originId)
                    }
                }
            })
            setNbOnItemClickListener { _, view, position ->
                Navigation.findNavController(view)
                    .navigate(R.id.action_collectFragment_to_webFragment, Bundle().apply {
                        putSerializable("collect", articleAdapter.data[position])
                    })
            }
        }
    }

    override fun lazyLoadData() {
        mViewModel.getCollectAriticleData(true)
    }

    override fun createObserver() {
        mViewModel.ariticleDataState.observe(viewLifecycleOwner, Observer {
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
                        articleAdapter.setNewData(it.listData)
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
        mViewModel.collectUiState.observe(viewLifecycleOwner, Observer {
            if (it.isSuccess) {
                //收藏或取消收藏操作成功，发送全局收藏消息
                appViewModel.collect.postValue(CollectBus(it.id, it.collect))
            } else {
                showMessage(it.errorMsg)
                for (index in articleAdapter.data.indices) {
                    if (articleAdapter.data[index].originId == it.id) {
                        articleAdapter.notifyItemChanged(index)
                        break
                    }
                }
            }
        })
        appViewModel.run {
            //监听全局的收藏信息 收藏的Id跟本列表的数据id匹配则 需要删除他 否则则请求最新收藏数据
            collect.observe(viewLifecycleOwner, Observer {
                for (index in articleAdapter.data.indices) {
                    if (articleAdapter.data[index].originId == it.id) {
                        articleAdapter.remove(index)
                        if (articleAdapter.data.size == 0) {
                            loadsir.showCallback(EmptyCallback::class.java)
                        }
                        return@Observer
                    }
                }
                mViewModel.getCollectAriticleData(true)
            })
        }
    }

}