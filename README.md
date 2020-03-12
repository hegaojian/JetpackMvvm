# JetPackMvvm
**基于MVVM模式与谷歌官方推荐的JetPack,封装Kotlin+LiveData+ViewModel+Lifecycle+dataBinding的开发框架,帮你简化各种操作，让你快速上手使用**  

## 框架技术  

- **Lifecycle:**

  1.实现生命周期  **管理的一致性**，做到 “一处修改、处处生效”。  
  
  2.让第三方组件能够 **随时在自己内部拿到生命周期状态**，以便执行 **及时叫停 错过时机的 异步业务** 等操作。  
  
  3.让第三方组件在调试时 能够 **更方便和安全地追踪到 事故所在的生命周期源**。  
  
- **LiveData:**

  1.在 Lifecycle 的帮助下，实现生命周期管理的一致性，以及作用域的可控。  
  
  2.**职责十分克制**：仅限于让事件源注入状态、让订阅者观察状态，乃至于只能在单例的帮助下正常使用 —— 从而让新手老手 都能自然而然地遵循 **从唯一可信源取材、完成状态的正确分发** —— 这样一种 得以将不可预期的错误降到最少 的状态管理理念。  
  
  3.就算不用 DataBinding，也能使 “单向依赖” 成为可能。  
  
- **ViewModel:**  

  1.让状态管理独立于视图控制器，从而做到**重建状态的分治、状态在多页面的共享，以及跨页面通信**。
  
  2.为状态设置作用域，使状态的共享做到**作用域可控**。  
  
  3.实现**单向依赖，避免内存泄漏**。 
  
- **dataBinding:**
  
  1.通过 基于适配器模式 的 “数据驱动” 思想，规避视图的一致性问题。  
  
  2.数据驱动免去了 因为调用视图对象 而存在的大量冗余的判空处理。  
  
  3.数据驱动免去了 因为调用视图对象 而存在的大量样板代码  
  
  4.以前一直对databind很抵触（其实是思想有问题），就一直没用，最近试着去用了一下，刚开始确实很难受，一度想放弃，但是坚持了一段时间，其实蛮好用的，你可以用他做一些简单的视图数据绑定，其实会很爽，我在这个项目中我加了兼容处理，**不想用dataBinding的继承BaseVmActivity，想用dataBinding的继承BaseVmDbActivity**
  
- **Kotlin:**

  **谷歌亲儿子语言**，从java转到kotlin的你绝对会要说：**真香**
  
- **Kotlin协程:**  

  **一个线程框架**，用起来很爽，以同步方式写异步逻辑

# 1.如何使用

- 1.1 启用dataBinding 如果你需要用到dataBinding,请在主工程的build中加入，不用可以忽略这一步：
```
  dataBinding{
        enabled = true
  }
```
- 1.2 依赖
```
dependencies {
  ...
  implementation 'me.hegj:JetpackMvvm:1.0.0'
}

```
# 2.登录示例  

- 2.1 LoginFragment继承基类传入相关泛型
```
class LoginActivity:BaseFragment<LoginRegisterViewModel, FragmentLoginBinding>() {

    override fun layoutId() = R.layout.fragment_login

    override fun initView() {
        login_sub.setOnClickListener {
            mViewModel.login()
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
- 2.2 创建LoginViewModel继承BaseViewModel
```
class LoginViewModel:BaseViewModel() {
    
     //用户名
    var username = StringObservableField()
     //密码
    var password = StringObservableField()
    
    private val loginRpository:LoginRepository by lazy{ LoginRepository() }

    var loginResult = MutableLiveData<ViewState<UserInfo>>()

    fun login(){
        launchRequest({loginRpository.login(username.get(),password.get())},loginResult)
    }
}
```
## License
```
 Copyright 2019, hegaojian(何高建)       
  
   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at 
 
       http://www.apache.org/licenses/LICENSE-2.0 

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```

