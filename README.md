# MVVM-Nb
轻松实现Kotlin+MVVM+LiveData+协程，让你的开发效率飞起来，让你的项目稳的一匹

# 1.如何使用

- 1.1 启用dataBindingg 如果你需要用到dataBinding,请在主工程的build中加入，不用可以忽略这一步：
```
  dataBinding{
        enabled = true
  }
```
- 1.2 依赖
```
dependencies {
  ...
  implementation 'me.hegj:mvvmnb:1.0.1'
}

```
# 2.快速开始
- 2.1 编写自己的基类继承BaseVmActivity(不使用databind)或BaseVmDbActivity(使用databind),同时实现继承类的方法
```
abstract class BaseActivity<VM : BaseViewModel> : BaseVmActivity<VM>() {

    /**
     * 打开等待框
     */
    override fun showLoading(message: String) {
       
    }
    /**
     * 关闭等待框
     */
    override fun dismissLoading() {
     
    }

    /**
     * 显示消息弹窗
     */
    override fun showMessage(message: String) {
       
    }

    /**
     * 吐司
     */
    override fun showToast(message: String) {
       
    }

}
```
- 2.2 LoginActivity继承基类
```
class LoginActivity:BaseActivity<LoginViewModel>() {

    override fun layoutId() = R.layout.activity_login

    override fun initView() {
        login_sub.setOnClickListener {
            mViewModel.login(login_username.toTextString(),login_password.toTextString())
        }
    }

    override fun createObserver() {
        //监听请求结果
        mViewModel.loginResult.observe(this, Observer { viewState ->
            parseState(viewState,{
                //登录成功
                finish()
            },{
                //登录失败
                showMessage(it.errorMsg)
            })
        })
    }
}
```
- 2.3 创建LoginViewModel继承BaseViewModel
```
class LoginViewModel:BaseViewModel() {

    private val loginRpository:LoginRepository by lazy{ LoginRepository() }

    var loginResult = MutableLiveData<ViewState<UserInfo>>()

    fun login(username:String,password:String){
        launchRequest({loginRpository.login(username,password)},loginResult)
    }
}
```


