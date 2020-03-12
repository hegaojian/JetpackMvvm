package me.hgj.jetpackmvvm.demo.ui.todo

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.bottomsheets.setPeekHeight
import com.afollestad.materialdialogs.lifecycle.lifecycleOwner
import com.afollestad.materialdialogs.list.listItems
import com.blankj.utilcode.util.ConvertUtils
import com.kingja.loadsir.core.LoadService
import com.yanzhenjie.recyclerview.SwipeRecyclerView
import kotlinx.android.synthetic.main.include_list.*
import kotlinx.android.synthetic.main.include_recyclerview.*
import kotlinx.android.synthetic.main.include_toolbar.*
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.adapter.TodoAdapter
import me.hgj.jetpackmvvm.demo.app.base.BaseFragment
import me.hgj.jetpackmvvm.demo.app.ext.*
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.EmptyCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.ErrorCallback
import me.hgj.jetpackmvvm.demo.app.weight.loadCallBack.LoadingCallback
import me.hgj.jetpackmvvm.demo.app.weight.recyclerview.DefineLoadMoreView
import me.hgj.jetpackmvvm.demo.app.weight.recyclerview.SpaceItemDecoration
import me.hgj.jetpackmvvm.demo.databinding.FragmentListBinding

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/11
 * 描述　:
 */
class TodoListFragment : BaseFragment<TodoViewModel, FragmentListBinding>() {

    //适配器
    private val articleAdapter: TodoAdapter by lazy { TodoAdapter(arrayListOf()) }
    //界面状态管理者
    private lateinit var loadsir: LoadService<Any>
    //recyclerview的底部加载view 因为要动态改变他的颜色，所以加了他这个属性
    private lateinit var footView: DefineLoadMoreView

    override fun layoutId() = R.layout.fragment_list

    override fun initView() {
        toolbar.run {
            initClose("TODO") {
                Navigation.findNavController(it).navigateUp()
            }
            inflateMenu(R.menu.todo_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.todo_add -> {
                        Navigation.findNavController(this)
                            .navigate(R.id.action_todoListFragment_to_addTodoFragment)
                    }
                }
                true
            }
        }
        //状态页配置
        loadsir = LoadServiceInit(swipeRefresh) {
            //点击重试时触发的操作
            loadsir.showCallback(LoadingCallback::class.java)
            mViewModel.getTodoData(true)
        }

        //初始化recyclerView
        recyclerView.init(LinearLayoutManager(context), articleAdapter).let {
            it as SwipeRecyclerView
            it.addItemDecoration(SpaceItemDecoration(0, ConvertUtils.dp2px(8f)))
            footView = it.initFooter(SwipeRecyclerView.LoadMoreListener {
                //触发加载更多时请求数据
                mViewModel.getTodoData(false)
            })
            //初始化FloatingActionButton
            it.initFloatBtn(floatbtn)
        }
        //初始化 SwipeRefreshLayout
        swipeRefresh.init {
            //触发刷新监听时请求数据
            mViewModel.getTodoData(true)
        }
        articleAdapter.run {
            setOnItemClickListener { adapter, view, position ->
                Navigation.findNavController(view).navigate(R.id.action_todoListFragment_to_addTodoFragment,
                    Bundle().apply{
                        putSerializable("todo",articleAdapter.data[position])
                    })
            }
            addChildClickViewIds(R.id.item_todo_setting)
            setOnItemChildClickListener { adapter, view, position ->
                when (view.id) {
                    R.id.item_todo_setting -> {
                        val items =  if(articleAdapter.data[position].isDone()){
                            listOf("删除", "编辑")
                        }else{
                            listOf("删除", "编辑","完成")
                        }
                        activity?.let { activity ->
                            MaterialDialog(activity, BottomSheet())
                                .lifecycleOwner(viewLifecycleOwner).show {
                                    cornerRadius(8f)
                                    setPeekHeight(ConvertUtils.dp2px((items.size*50+36).toFloat()))
                                    listItems(items = items) { _, index, item ->
                                        when(index){
                                            0 ->{
                                                //删除
                                                mViewModel.delTodo(articleAdapter.data[position].id,position)
                                            }
                                            1 ->{
                                                //编辑
                                                Navigation.findNavController(view).navigate(R.id.action_todoListFragment_to_addTodoFragment,
                                                    Bundle().apply{
                                                     putSerializable("todo",articleAdapter.data[position])
                                                 })
                                            }
                                            2 ->{
                                                //完成
                                                mViewModel.doneTodo(articleAdapter.data[position].id,position)
                                            }
                                        }
                                    }
                                }
                        }

                    }
                }
            }
        }
    }

    override fun lazyLoadData() {
        mViewModel.getTodoData(true)
    }

    override fun createObserver() {
        mViewModel.todoDataState.observe(viewLifecycleOwner, Observer {
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
        mViewModel.delDataState.observe(viewLifecycleOwner, Observer {
            if(it.isSuccess){
                if(articleAdapter.data.size==1){
                    loadsir.showCallback(EmptyCallback::class.java)
                }
                articleAdapter.remove(it.data)
            }else{
                showMessage(it.errorMsg)
            }
        })
        mViewModel.doneDataState.observe(viewLifecycleOwner, Observer {
            if(it.isSuccess){
                swipeRefresh.isRefreshing = true
                mViewModel.getTodoData(true)
            }else{
                showMessage(it.errorMsg)
            }
        })
        getActivityMessageViewModel().addTodo.observe(viewLifecycleOwner, Observer {
            if(articleAdapter.data.size==0){
                //界面没有数据时，变为加载中 增强一丢丢体验
                loadsir.showCallback(LoadingCallback::class.java)
            }else{
                //有数据时，swipeRefresh 显示刷新状态
                swipeRefresh.isRefreshing = true
            }
            mViewModel.getTodoData(true)
        })
    }

}