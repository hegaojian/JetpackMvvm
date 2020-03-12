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
import me.hgj.jetpackmvvm.demo.adapter.CollectUrlAdapter
import me.hgj.jetpackmvvm.demo.app.CollectViewModel
import me.hgj.jetpackmvvm.demo.app.base.BaseFragment
import me.hgj.jetpackmvvm.demo.app.ext.*
import me.hgj.jetpackmvvm.demo.app.weight.customview.CollectView
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.EmptyCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.ErrorCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.LoadingCallback
import me.hgj.jetpackmvvm.demo.app.weight.recyclerview.SpaceItemDecoration
import me.hgj.jetpackmvvm.demo.data.bean.CollectUrlResponse
import me.hgj.jetpackmvvm.demo.databinding.IncludeListBinding

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/10
 * 描述　: 收藏的网址集合Fragment
 */
class CollectUrlFragment : BaseFragment<CollectViewModel, IncludeListBinding>() {

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>
    private val articleAdapter: CollectUrlAdapter by lazy { CollectUrlAdapter(arrayListOf()) }

    override fun layoutId() = R.layout.include_list

    override fun initView() {
        //状态页配置
        loadsir = LoadServiceInit(swipeRefresh) {
            //点击重试时触发的操作
            loadsir.showCallback(LoadingCallback::class.java)
            mViewModel.getCollectUrlData()

        }
        //初始化recyclerView
        recyclerView.init(LinearLayoutManager(context), articleAdapter).let {
            it as SwipeRecyclerView
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            //初始化FloatingActionButton
            it.initFloatBtn(floatbtn)
        }
        //初始化 SwipeRefreshLayout
        swipeRefresh.init {
            //触发刷新监听时请求数据
            mViewModel.getCollectUrlData()
        }
        articleAdapter.run {
            setOnCollectViewClickListener(object :
                CollectUrlAdapter.OnCollectViewClickListener {
                override fun onClick(item: CollectUrlResponse, v: CollectView, position: Int) {
                    if (v.isChecked) {
                        mViewModel.uncollectUrl(item.id)
                    } else {
                        mViewModel.collectUrl(item.name, item.link)
                    }
                }
            })
            setOnItemClickListener { _, view, position ->
                Navigation.findNavController(view)
                    .navigate(R.id.action_collectFragment_to_webFragment, Bundle().apply {
                        putSerializable("collectUrl", articleAdapter.data[position])
                    })
            }
        }
    }

    override fun lazyLoadData() {
        mViewModel.getCollectUrlData()
    }

    override fun createObserver() {
        mViewModel.urlDataState.observe(viewLifecycleOwner, Observer {
            swipeRefresh.isRefreshing = false
            recyclerView.loadMoreFinish(it.isEmpty, it.hasMore)
            if (it.isSuccess) {
                //成功
                when {
                    //第一页并没有数据 显示空布局界面
                    it.isEmpty -> {
                        loadsir.showCallback(EmptyCallback::class.java)
                    }
                    else -> {
                        loadsir.showSuccess()
                        articleAdapter.setNewData(it.listData)
                    }
                }
            } else {
                //失败
                loadsir.setErrorText(it.errMessage)
                loadsir.showCallback(ErrorCallback::class.java)
            }
        })
        mViewModel.collectUrlUiState.observe(viewLifecycleOwner, Observer {
            if (it.isSuccess) {
                for (index in articleAdapter.data.indices) {
                    if (articleAdapter.data[index].id == it.id) {
                        articleAdapter.remove(index)
                        if (articleAdapter.data.size == 0) {
                            loadsir.showCallback(EmptyCallback::class.java)
                        }
                        return@Observer
                    }
                }
            } else {
                showMessage(it.errorMsg)
                for (index in articleAdapter.data.indices) {
                    if (articleAdapter.data[index].id == it.id) {
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
                    if (articleAdapter.data[index].id == it.id) {
                        articleAdapter.data.removeAt(index)
                        articleAdapter.notifyItemChanged(index)
                        if (articleAdapter.data.size == 0) {
                            loadsir.showCallback(EmptyCallback::class.java)
                        }
                        return@Observer
                    }
                }
                mViewModel.getCollectUrlData()
            })
        }
    }

}