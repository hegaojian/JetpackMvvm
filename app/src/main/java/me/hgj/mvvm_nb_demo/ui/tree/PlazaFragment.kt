package me.hgj.mvvm_nb_demo.ui.tree

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.kingja.loadsir.core.LoadService
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.include_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*
import me.hgj.mvvm_nb_demo.R
import me.hgj.mvvm_nb_demo.adapter.AriticleAdapter
import me.hgj.mvvm_nb_demo.app.base.BaseFragment
import me.hgj.mvvm_nb_demo.app.ext.*
import me.hgj.mvvm_nb_demo.app.util.CacheUtil
import me.hgj.mvvm_nb_demo.app.weight.customview.CollectView
import me.hgj.mvvm_nb_demo.app.weight.loadCallBack.EmptyCallback
import me.hgj.mvvm_nb_demo.app.weight.loadCallBack.ErrorCallback
import me.hgj.mvvm_nb_demo.app.weight.loadCallBack.LoadingCallback
import me.hgj.mvvm_nb_demo.app.weight.recyclerview.DefineLoadMoreView
import me.hgj.mvvm_nb_demo.app.weight.recyclerview.SpaceItemDecoration
import me.hgj.mvvm_nb_demo.data.bean.AriticleResponse
import me.hgj.mvvm_nb_demo.data.bean.CollectBus
import me.hgj.mvvm_nb_demo.databinding.IncludeListBinding

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/3
 * 描述　: 广场
 */
class PlazaFragment :BaseFragment<TreeViewModel,IncludeListBinding>(){

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>
    //recyclerview的底部加载view 因为要动态改变他的颜色，所以加了他这个属性
    private lateinit var footView: DefineLoadMoreView
    private val articleAdapter :AriticleAdapter by lazy { AriticleAdapter(arrayListOf(),showTag = true).apply {
        appViewModel.appAnimation.value?.let { setAdapterAnimion(it) }
    } }

    override fun layoutId() = R.layout.include_list

    override fun initView() {
        //状态页配置
        loadsir = LoadServiceInit(swipeRefresh) {
            //点击重试时触发的操作
            loadsir.showCallback(LoadingCallback::class.java)
            mViewModel.getPlazaData(true)
        }
        //初始化recyclerView
        recyclerView.init(LinearLayoutManager(context),articleAdapter).let {it as SwipeRecyclerView
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            footView = it.initFooter(SwipeRecyclerView.LoadMoreListener {
                mViewModel.getPlazaData(false)
            })
            //初始化FloatingActionButton
            it.initFloatBtn(floatbtn)
        }
        //初始化 SwipeRefreshLayout
        swipeRefresh.init {
            //触发刷新监听时请求数据
            mViewModel.getPlazaData(true)
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
                            .navigate(R.id.action_mainFragment_to_loginFragment)
                    }
                }
            })
            setOnItemClickListener { _, view, position ->
                Navigation.findNavController(view)
                    .navigate(R.id.action_mainfragment_to_webFragment, Bundle().apply {
                        putSerializable("ariticleData",articleAdapter.data[position])
                    })
            }
        }
    }

    override fun lazyLoadData() {
        mViewModel.getPlazaData(true)
    }

    override fun createObserver() {
        mViewModel.plazaDataState.observe(this, Observer {
            swipeRefresh.isRefreshing = false
            recyclerView.loadMoreFinish(it.isEmpty,it.hasMore)
            if(it.isSuccess){
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
            }else{
                //失败
                if (it.isRefresh){
                    //如果是第一页，则显示错误界面，并提示错误信息
                    loadsir.setErrorText(it.errMessage)
                    loadsir.showCallback(ErrorCallback::class.java)
                }else{
                    recyclerView.loadMoreError(0, it.errMessage)
                }
            }
        })
        mViewModel.collectUiState.observe(this, Observer {
            if (it.isSuccess) {
                //收藏或取消收藏操作成功，发送全局收藏消息
                appViewModel.collect.postValue(
                    CollectBus(
                        it.id,
                        it.collect
                    )
                )
            } else {
                showMessage(it.errorMsg)
            }
        })
        appViewModel.run {
            //监听账户信息是否改变 有值时(登录)将相关的数据设置为已收藏，为空时(退出登录)，将已收藏的数据变为未收藏
            userinfo.observe(this@PlazaFragment, Observer {
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
            //监听全局的主题颜色改变
            appColor.observe(this@PlazaFragment, Observer {
                setUiTheme(it, listOf(floatbtn, swipeRefresh, loadsir))
            })
            //监听全局的列表动画改编
            appAnimation.observe(this@PlazaFragment, Observer {
                articleAdapter.setAdapterAnimion(it)
            })
            //监听全局的收藏信息 收藏的Id跟本列表的数据id匹配则需要更新
            collect.observe(this@PlazaFragment, Observer {
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