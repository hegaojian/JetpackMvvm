package me.hgj.jetpackmvvm.demo.ui.fragment.integral

import android.os.Bundle
import androidx.fragment.app.viewModels
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
import me.hgj.jetpackmvvm.demo.app.weight.recyclerview.SpaceItemDecoration
import me.hgj.jetpackmvvm.demo.databinding.FragmentListBinding
import me.hgj.jetpackmvvm.demo.ui.adapter.IntegralHistoryAdapter
import me.hgj.jetpackmvvm.demo.viewmodel.request.RequestIntegralViewModel
import me.hgj.jetpackmvvm.demo.viewmodel.state.IntegralViewModel
import me.hgj.jetpackmvvm.ext.nav

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/2
 * 描述　:积分排行
 */
class IntegralHistoryFragment : BaseFragment<IntegralViewModel, FragmentListBinding>() {

    //适配器
    private val integralAdapter: IntegralHistoryAdapter by lazy { IntegralHistoryAdapter(arrayListOf()) }

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    //请求的ViewModel /** */
    private val requestIntegralViewModel: RequestIntegralViewModel by viewModels()

    override fun layoutId() = R.layout.fragment_list

    override fun initView(savedInstanceState: Bundle?) {
        toolbar.initClose("积分记录") {
            nav().navigateUp()
        }
        //状态页配置
        loadsir = loadServiceInit(swipeRefresh) {
            //点击重试时触发的操作
            loadsir.showLoading()
            requestIntegralViewModel.getIntegralHistoryData(true)
        }
        //初始化recyclerView
        recyclerView.init(LinearLayoutManager(context), integralAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            it.initFooter(SwipeRecyclerView.LoadMoreListener {
                //触发加载更多时请求数据
                requestIntegralViewModel.getIntegralHistoryData(false)
            })
            //初始化FloatingActionButton
            it.initFloatBtn(floatbtn)
        }
        //初始化 SwipeRefreshLayout
        swipeRefresh.init {
            //触发刷新监听时请求数据
            requestIntegralViewModel.getIntegralHistoryData(true)
        }
    }

    override fun lazyLoadData() {
        //设置界面 加载中
        loadsir.showLoading()
        requestIntegralViewModel.getIntegralHistoryData(true)
    }

    override fun createObserver() {
        requestIntegralViewModel.integralHistoryDataState.observe(viewLifecycleOwner, Observer {
            //设值 新写了个拓展函数，搞死了这个恶心的重复代码
            loadListData(it, integralAdapter, loadsir, recyclerView,swipeRefresh)
        })
    }
}