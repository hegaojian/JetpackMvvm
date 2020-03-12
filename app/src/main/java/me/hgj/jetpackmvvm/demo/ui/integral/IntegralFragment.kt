package me.hgj.jetpackmvvm.demo.ui.integral

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.kingja.loadsir.core.LoadService
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.fragment_integral.*
import kotlinx.android.synthetic.main.include_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*
import kotlinx.android.synthetic.main.include_toolbar.*
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.adapter.IntegralAdapter
import me.hgj.jetpackmvvm.demo.app.base.BaseFragment
import me.hgj.jetpackmvvm.demo.app.ext.*
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.EmptyCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.ErrorCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.LoadingCallback
import me.hgj.jetpackmvvm.demo.app.weight.recyclerview.DefineLoadMoreView
import me.hgj.jetpackmvvm.demo.app.weight.recyclerview.SpaceItemDecoration
import me.hgj.jetpackmvvm.demo.data.bean.BannerResponse
import me.hgj.jetpackmvvm.demo.data.bean.IntegralResponse
import me.hgj.jetpackmvvm.demo.databinding.FragmentIntegralBinding
import me.hgj.jetpackmvvm.ext.util.notNull
import me.hgj.jetpackmvvm.ext.view.gone

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/2
 * 描述　:积分排行
 */
class IntegralFragment : BaseFragment<IntegralViewModel, FragmentIntegralBinding>() {
    private var rank: IntegralResponse? = null
    //适配器
    private lateinit var integralAdapter: IntegralAdapter
    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>
    //recyclerview的底部加载view 因为要动态改变他的颜色，所以加了他这个属性
    private lateinit var footView: DefineLoadMoreView

    override fun layoutId() = R.layout.fragment_integral

    override fun initView() {
        mDatabind.vm = mViewModel
        rank = arguments?.getSerializable("rank") as? IntegralResponse
        rank.notNull({
            mViewModel.rank.set(rank)
        }, {
            integral_cardview.gone()
        })
        integralAdapter = IntegralAdapter(arrayListOf(), rank?.rank ?: -1)
        toolbar.run {
            inflateMenu(R.menu.integral_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.integral_guize -> {
                        Navigation.findNavController(this)
                            .navigate(R.id.action_integralFragment_to_webFragment,
                                Bundle().apply {
                                    putSerializable(
                                        "bannerdata",
                                        BannerResponse(
                                            title = "积分规则",
                                            url = "https://www.wanandroid.com/blog/show/2653"
                                        )
                                    )
                                }
                            )
                    }
                    R.id.integral_history -> {
                        Navigation.findNavController(this)
                            .navigate(R.id.action_integralFragment_to_integralHistoryFragment)
                    }
                }
                true
            }
            initClose("积分排行") {
                Navigation.findNavController(toolbar).navigateUp()
            }
        }
        //状态页配置
        loadsir = LoadServiceInit(swipeRefresh) {
            //点击重试时触发的操作
            loadsir.showCallback(LoadingCallback::class.java)
            mViewModel.getIntegralData(true)
        }
        //初始化recyclerView
        recyclerView.init(LinearLayoutManager(context), integralAdapter).let {
            it as SwipeRecyclerView
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            footView = it.initFooter(SwipeRecyclerView.LoadMoreListener {
                //触发加载更多时请求数据
                mViewModel.getIntegralData(false)
            })
            //初始化FloatingActionButton
            it.initFloatBtn(floatbtn)
        }
        //初始化 SwipeRefreshLayout
        swipeRefresh.init {
            //触发刷新监听时请求数据
            mViewModel.getIntegralData(true)
        }
        appViewModel.appColor.value?.let {
            setUiTheme(
                it,
                listOf(integral_merank, integral_mename, integral_mecount)
            )
        }
    }

    override fun lazyLoadData() {
        mViewModel.getIntegralData(true)
    }

    override fun createObserver() {
        mViewModel.integralDataState.observe(viewLifecycleOwner, Observer {
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
                        integralAdapter.setNewData(it.listData)
                    }
                    //不是第一页
                    else -> {
                        loadsir.showSuccess()
                        integralAdapter.addData(it.listData)
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
    }
}