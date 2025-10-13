package me.hgj.jetpackmvvm.base.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import com.gyf.immersionbar.ImmersionBar
import me.hgj.jetpackmvvm.R
import me.hgj.jetpackmvvm.base.vm.BaseViewModel
import me.hgj.jetpackmvvm.core.net.LoadingEntity
import me.hgj.jetpackmvvm.core.net.LoadingType
import me.hgj.jetpackmvvm.ext.util.createViewModel
import me.hgj.jetpackmvvm.ext.view.dismissLoadingExt
import me.hgj.jetpackmvvm.ext.view.showLoadingExt
import me.hgj.jetpackmvvm.ext.view.visibleOrGone
import me.hgj.jetpackmvvm.widget.loadsir.callback.Callback
import me.hgj.jetpackmvvm.widget.loadsir.callback.SuccessCallback
import me.hgj.jetpackmvvm.widget.loadsir.core.LoadService
import me.hgj.jetpackmvvm.widget.loadsir.core.LoadSir

/**
 * 作者　: hegaojian
 * 时间　: 2020/11/18
 * 描述　:
 */
abstract class BaseVmActivity<VM : BaseViewModel> : AppCompatActivity(), BaseIView {

    abstract val layoutId: Int

    /**  是否要显示头部标题栏 */
    open val showTitle: Boolean = true

    /** 是否显示暗色状态栏文字颜色 默认白色 */
    open val statusDark = false

    private var dataBindView: View? = null

    /** 界面状态管理者 */
    lateinit var uiStatusManger: LoadService<*>

    /** 当前Activity绑定的一个ViewModel */
    lateinit var mViewModel: VM

    /** titleBarView 这个可替换成自己定制的标题栏，重写getTitleBarView方法 */
    private var mTitleBarView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        //生成泛型中的ViewModel
        mViewModel = createViewModel().also {
            // 绑定监听到当前Ui层
            addLoadingUiChange(it)
        }
        //初始化 status View
        initStatusView()
        initView(savedInstanceState)
        createObserver()
        //初始化绑定点击方法
        onBindViewClick()
    }

    private fun initStatusView() {
        mTitleBarView = getTitleBarView()
        dataBindView = initViewDataBind()
        mTitleBarView?.let {
            if(showTitle){
                //如果显示标题栏，那么添加到页面的第一个
                findViewById<LinearLayout>(R.id.baseRootView).addView(it, 0)
            }
        }
        initImmersionBar()
        findViewById<FrameLayout>(R.id.baseContentView).addView(if (dataBindView == null) LayoutInflater.from(this).inflate(layoutId, null) else dataBindView)
        uiStatusManger = createLoadService()
    }

    override fun createObserver() {
        //一些监听放在这里面存放编写 方便管理
    }
    /**
     * 创建 LoadService
     */
    private fun createLoadService(): LoadService<*> {
        val loadView = getLoadingView() ?: findViewById(R.id.baseContentView)
        val target = loadView
        val status =  if (hasCustomStateLayout()) {
            // 如果有自定义状态页，构建新的 LoadSir
            LoadSir.beginBuilder()
                .setEmptyCallBack(getEmptyStateLayout() ?: LoadSir.getDefault().emptyCallBack)
                .setLoadingCallBack(getLoadingStateLayout() ?: LoadSir.getDefault().loadingCallBack)
                .setErrorCallBack(getErrorStateLayout() ?: LoadSir.getDefault().errorCallBack)
                .setDefaultCallback(SuccessCallback::class.java)
                .build()
                .register(target) { onLoadRetry() }
        } else {
            // 否则使用全局默认配置
            LoadSir.getDefault().register(target) { onLoadRetry() }
        }
        return status
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
     * 初始化view成功后执行
     */
    abstract fun initView(savedInstanceState: Bundle?)


    /**
     * 初始化沉浸式
     * Init immersion bar.
     */
    protected open fun initImmersionBar() {
        //设置共同沉浸式样式
        mTitleBarView?.let {
            if (showTitle) {
                ImmersionBar.with(this).titleBar(it).statusBarDarkFont(statusDark).init()
            }else{
                ImmersionBar.with(this).statusBarDarkFont(statusDark).init()
            }
        }
    }

    /**
     * 点击事件方法 配合setOnclick()拓展函数调用
     */
    open fun onBindViewClick() {}

    /**
     * 注册 UI 事件 监听请求时的回调UI的操作
     */
    fun addLoadingUiChange(viewModel: BaseViewModel) {
        viewModel.loadingChange.run {
            loading.observe(this@BaseVmActivity) {
                when (it.loadingType) {
                    LoadingType.LOADING_DIALOG -> {
                        if (it.isShow) {
                            showLoading(it)
                        } else {
                            dismissLoading(it)
                        }
                    }

                    LoadingType.LOADING_XML -> {
                        if (it.isShow) {
                            showLoadingUi(it.loadingMessage)
                        }
                    }

                    LoadingType.LOADING_NULL -> { }
                }
            }
            showError.observe(this@BaseVmActivity) {
                //如果请求错误 并且loadingType类型为 xml 那么控制界面显示为错误布局
                if (it.loadingType == LoadingType.LOADING_XML) {
                    showErrorUi(it.msg)
                }
            }
            showSuccess.observe(this@BaseVmActivity) {
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
            (getErrorStateLayout()?.javaClass ?: LoadSir.getDefault().errorCallBack::class.java).apply {
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
            (getEmptyStateLayout()?.javaClass ?: LoadSir.getDefault().emptyCallBack::class.java).apply {
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
            (getLoadingStateLayout()?.javaClass ?: LoadSir.getDefault().loadingCallBack::class.java).apply {
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

    /**
     * 显示加载
     * @param setting 设置
     */
    override fun showLoading(setting: LoadingEntity) {
        showLoadingExt(setting.loadingMessage, setting.coroutineScope)
    }

    /**
     * 隐藏
     * @param setting 设置
     */
    override fun dismissLoading(setting: LoadingEntity) {
        dismissLoadingExt()
    }

    /**
     * 供子类BaseVbActivity 初始化 DataBinding ViewBinding操作
     */
    open fun initViewDataBind(): View? {
        return null
    }

    override fun getEmptyStateLayout(): Callback? = null
    override fun getErrorStateLayout(): Callback? = null
    override fun getLoadingStateLayout(): Callback? = null

}