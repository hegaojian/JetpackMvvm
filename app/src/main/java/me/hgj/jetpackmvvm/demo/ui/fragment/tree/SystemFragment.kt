package me.hgj.jetpackmvvm.demo.ui.fragment.tree

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.blankj.utilcode.util.ConvertUtils
import com.kingja.loadsir.core.LoadService
import kotlinx.android.synthetic.main.include_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.base.BaseFragment
import me.hgj.jetpackmvvm.demo.app.ext.*
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.ErrorCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.LoadingCallback
import me.hgj.jetpackmvvm.demo.app.weight.recyclerview.SpaceItemDecoration
import me.hgj.jetpackmvvm.demo.data.model.bean.SystemResponse
import me.hgj.jetpackmvvm.demo.databinding.IncludeListBinding
import me.hgj.jetpackmvvm.demo.ui.adapter.SystemAdapter
import me.hgj.jetpackmvvm.demo.viewmodel.request.RequestTreeViewModel
import me.hgj.jetpackmvvm.demo.viewmodel.state.TreeViewModel
import me.hgj.jetpackmvvm.ext.getViewModel
import me.hgj.jetpackmvvm.ext.nav

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/3
 * 描述　: 体系
 */
class SystemFragment : BaseFragment<TreeViewModel, IncludeListBinding>() {

    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>

    override fun layoutId() = R.layout.include_list

    private val systemAdapter: SystemAdapter by lazy { SystemAdapter(arrayListOf()) }

    /** 注意，在by lazy中使用getViewModel一定要使用泛型，虽然他提示不报错，但是你不写是不行的 */
    private  val requestTreeViewModel:RequestTreeViewModel by lazy { getViewModel<RequestTreeViewModel>() }

    override fun initView(savedInstanceState: Bundle?) {
        //状态页配置
        loadsir = LoadServiceInit(swipeRefresh) {
            //点击重试时触发的操作
            loadsir.showCallback(LoadingCallback::class.java)
            requestTreeViewModel.getSystemData()
        }
        //初始化recyclerView
        recyclerView.init(LinearLayoutManager(context), systemAdapter).let {
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            it.initFloatBtn(floatbtn)
        }
        //初始化 SwipeRefreshLayout
        swipeRefresh.init {
            //触发刷新监听时请求数据
            requestTreeViewModel.getSystemData()
        }
        systemAdapter.run {
            setNbOnItemClickListener { _, view, position ->
               nav().navigate(R.id.action_mainfragment_to_systemArrFragment,
                        Bundle().apply {
                            putParcelable("data", systemAdapter.data[position])
                        }
                    )
            }
            setChildClick { item: SystemResponse, view, position ->
                nav().navigate(R.id.action_mainfragment_to_systemArrFragment,
                        Bundle().apply {
                            putParcelable("data", item)
                            putInt("index", position)
                        }
                    )
            }
        }
    }

    override fun lazyLoadData() {
        requestTreeViewModel.getSystemData()
    }

    override fun createObserver() {
        requestTreeViewModel.systemDataState.observe(viewLifecycleOwner, Observer {
            swipeRefresh.isRefreshing = false
            if (it.isSuccess) {
                loadsir.showSuccess()
                systemAdapter.setNewInstance(it.listData)
            } else {
                loadsir.setErrorText(it.errMessage)
                loadsir.showCallback(ErrorCallback::class.java)
            }
        })

        shareViewModel.run {
            //监听全局的主题颜色改变
            appColor.observe(viewLifecycleOwner, Observer {
                setUiTheme(it, floatbtn, swipeRefresh, loadsir)
            })
            //监听全局的列表动画改编
            appAnimation.observe(viewLifecycleOwner, Observer {
                systemAdapter.setAdapterAnimion(it)
            })
        }

    }
}