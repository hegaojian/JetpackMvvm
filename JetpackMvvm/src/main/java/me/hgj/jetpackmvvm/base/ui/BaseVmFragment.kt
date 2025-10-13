package me.hgj.jetpackmvvm.base.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import me.hgj.jetpackmvvm.R
import me.hgj.jetpackmvvm.base.vm.BaseViewModel
import me.hgj.jetpackmvvm.ext.util.createViewModel
import me.hgj.jetpackmvvm.ext.util.logD
import me.hgj.jetpackmvvm.ext.view.dismissLoadingExt
import me.hgj.jetpackmvvm.ext.view.showLoadingExt
import me.hgj.jetpackmvvm.ext.view.visibleOrGone
import me.hgj.jetpackmvvm.widget.loadsir.callback.Callback
import me.hgj.jetpackmvvm.widget.loadsir.callback.SuccessCallback
import me.hgj.jetpackmvvm.widget.loadsir.core.LoadService
import me.hgj.jetpackmvvm.widget.loadsir.core.LoadSir
import me.hgj.jetpackmvvm.core.net.LoadStatusEntity
import me.hgj.jetpackmvvm.core.net.LoadingEntity
import me.hgj.jetpackmvvm.core.net.LoadingType
import kotlin.apply
import kotlin.jvm.java
import kotlin.jvm.javaClass
import kotlin.let
import kotlin.run
import kotlin.text.isNotEmpty

/**
 * 作者　: hegaojian
 * 时间　: 2020/11/18
 * 描述　:
 */
abstract class BaseVmFragment<VM : BaseViewModel> : Fragment(), BaseIView {

    abstract val layoutId: Int

    private var dataBindView: View? = null

    //界面状态管理者
    lateinit var uiStatusManger: LoadService<*>

    //是否第一次加载
    private var isFirst: Boolean = true

    //当前Fragment绑定的泛型类ViewModel
    lateinit var mViewModel: VM

    //父类activity
    lateinit var mActivity: AppCompatActivity

    override fun onCreateView(inflater: LayoutInflater,container: ViewGroup?,savedInstanceState: Bundle?): View? {
        isFirst = true
        javaClass.simpleName.logD()
        dataBindView = initViewDataBind(inflater, container)
        val rootView = if (dataBindView == null) {
            inflater.inflate(layoutId, container, false)
        } else {
            dataBindView
        }
        return if (getLoadingView() == null) {
            //没有传递指定的view,那么将整个Fragment布局包裹
            createLoadService(rootView)
            container?.removeView(uiStatusManger.loadLayout)
            uiStatusManger.loadLayout
        } else {
            rootView
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = createViewModel().also {
            // 绑定监听到当前Ui层
            addLoadingUiChange(it)
        }
        initStatusView(view, savedInstanceState)
        initView(savedInstanceState)
        createObserver()
        onBindViewClick()
    }

    override fun createObserver() {}

    private fun initStatusView(view: View, savedInstanceState: Bundle?) {
        //如果传入了自定义包裹view 将该view注册 做 空 错误 loading 布局处理
        getLoadingView()?.let { createLoadService(it) }
    }
    /**
     * 创建 LoadService
     */
    private fun createLoadService(view: View?){
         uiStatusManger = if (hasCustomStateLayout()) {
            // 如果有自定义状态页，构建新的 LoadSir
            LoadSir.beginBuilder()
                .setEmptyCallBack(getEmptyStateLayout() ?: LoadSir.getDefault().emptyCallBack)
                .setLoadingCallBack(getLoadingStateLayout() ?: LoadSir.getDefault().loadingCallBack)
                .setErrorCallBack(getErrorStateLayout() ?: LoadSir.getDefault().errorCallBack)
                .setDefaultCallback(SuccessCallback::class.java)
                .build()
                .register(view) { onLoadRetry() }
        } else {
            // 否则使用全局默认配置
            LoadSir.getDefault().register(view) { onLoadRetry() }
        }
    }

    /**
     * 判断是否有自定义状态布局
     */
    private fun hasCustomStateLayout(): Boolean {
        return getEmptyStateLayout() != null ||
                getLoadingStateLayout() != null ||
                getErrorStateLayout() != null
    }

    /**
     * 初始化view操作
     */
    abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 懒加载 当前Fragment首次视图可见时才触发一次
     */
    open fun lazyLoadData() {}

    override fun onResume() {
        super.onResume()
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirst) {
            lazyLoadData()
            isFirst = false
        }
    }

    /**
     * 子类可传入需要被包裹的View，做状态显示-空、错误、加载
     * 如果子类不覆盖该方法 那么会将整个当前Fragment界面都当做View包裹
     */
    override fun getLoadingView(): View? {
        return null
    }

    /**
     * 点击事件方法存放地 配合setOnclick()拓展函数调用
     */
    open fun onBindViewClick() {}

    /**
     * 注册 UI 事件 监听请求时的回调UI的操作
     */
    fun addLoadingUiChange(viewModel: BaseViewModel) {
        viewModel.loadingChange.run {
            loading.observe(viewLifecycleOwner) {
                when (it.loadingType) {
                    //通用弹窗Dialog
                    LoadingType.LOADING_DIALOG -> {
                        if (it.isShow) {
                            showLoading(it)
                        } else {
                            dismissLoading(it)
                        }
                    }
                    //请求时 xml显示 loading
                    LoadingType.LOADING_XML -> {
                        if (it.isShow) {
                            showLoadingUi(it.loadingMessage)
                        }
                    }
                    else ->{}
                }
            }
            //当请求失败时
            showError.observe(viewLifecycleOwner) {
                if (it.loadingType == LoadingType.LOADING_XML) {
                    showErrorUi(it.msg)
                }
            }
            //如果是 LoadingType.LOADING_XML，当请求成功时 会显示正常的成功布局
            showSuccess.observe(viewLifecycleOwner) {
                //只有 当前 状态为 加载中时， 才切换成 成功页面
                if (getLoadingStateLayout() != null && uiStatusManger.currentCallback == getLoadingStateLayout()!!::class.java
                    || uiStatusManger.currentCallback == LoadSir.getDefault().loadingCallBack::class.java
                ) {
                    showSuccessUi()
                }
            }
        }
    }

    /**
     * 空界面，错误界面 点击重试时触发的方法，如果有使用 状态布局的话，一般子类都要实现
     */
    override fun onLoadRetry() {}

    /**
     * 显示 成功状态界面
     */
    override fun showSuccessUi() {
        uiStatusManger.showSuccess()
    }

    /**
     * 显示 错误 状态界面
     * @param message String
     */
    override fun showErrorUi(message: String) {
        uiStatusManger.showCallback(
            (getErrorStateLayout()?.javaClass
                ?: LoadSir.getDefault().errorCallBack::class.java).apply {
                uiStatusManger.setCallBack(this) { _, view ->
                    val messageView = view.findViewById<AppCompatTextView>(R.id.state_error_tip)
                    messageView?.let {
                        it.text = message
                        it.visibleOrGone(message.isNotEmpty())
                    }
                }
            }
        )
    }

    /**
     * 显示 空数据 状态界面
     */
    override fun showEmptyUi(message: String) {
        uiStatusManger.showCallback(
            (getEmptyStateLayout()?.javaClass
                ?: LoadSir.getDefault().emptyCallBack::class.java).apply {
                uiStatusManger.setCallBack(this) { _, view ->
                    val messageView = view.findViewById<AppCompatTextView>(R.id.state_empty_tip)
                    messageView?.let {
                        it.text = message
                        it.visibleOrGone(message.isNotEmpty())
                    }
                }
            }
        )
    }

    /**
     * 显示 loading 状态界面
     */
    override fun showLoadingUi(message: String) {
        uiStatusManger.showCallback(
            (getLoadingStateLayout()?.javaClass
                ?: LoadSir.getDefault().loadingCallBack::class.java).apply {
                uiStatusManger.setCallBack(this) { _, view ->
                    val messageView = view.findViewById<AppCompatTextView>(R.id.state_loading_tip)
                    messageView?.let {
                        it.text = message
                        it.visibleOrGone(message.isNotEmpty())
                    }
                }
            }
        )
    }

    override fun showLoading(setting: LoadingEntity) {
        showLoadingExt(setting.loadingMessage)
    }

    override fun dismissLoading(setting: LoadingEntity) {
        dismissLoadingExt()
    }

    /**
     * 供子类BaseVmDbActivity 初始化 DataBinding ViewBinding操作
     */
    open fun initViewDataBind(inflater: LayoutInflater, container: ViewGroup?): View? {
        return null
    }

    override fun getEmptyStateLayout(): Callback? = null
    override fun getErrorStateLayout(): Callback? = null
    override fun getLoadingStateLayout(): Callback? = null

}