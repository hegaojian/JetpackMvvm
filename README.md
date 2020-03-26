[![Platform][1]][2] [![GitHub license][3]][4]  [![GitHub license][5]][6] 

[1]:https://img.shields.io/badge/platform-Android-blue.svg  
[2]:https://github.com/hegaojian/JetpackMvvm
[3]:https://img.shields.io/github/release/hegaojian/JetpackMvvm.svg
[4]:https://github.com/hegaojian/JetpackMvvm/releases/latest
[5]:https://img.shields.io/badge/license-Apache%202-blue.svg
[6]:https://github.com/hegaojian/JetpackMvvm/blob/master/LICENSE

# JetPackMvvm
- **基于MVVM模式集成谷歌官方推荐的JetPack组件库：LiveData、ViewModel、Lifecycle组件**
- **使用kotlin语言，添加大量拓展函数，简化代码**
- **加入Retorfit网络请求,协程，帮你简化各种操作，让你快速请求网络**  

## 演示Demo
 已用该库重构了我之前的玩安卓项目，利用Navigation组件以单Activity+Fragment架构编写，优化了很多代码，对比之前的mvp项目，开发效率与舒适度要提高了不少，想看之前MVP的项目可以去 [https://github.com/hegaojian/WanAndroid](https://github.com/hegaojian/WanAndroid) 
 
#### 效果图展示 
![项目效果图](https://upload-images.jianshu.io/upload_images/9305757-818106225dd01e65.gif?imageMogr2/auto-orient/strip)
 
#### APK下载：

- [Github下载](https://github.com/hegaojian/JetpackMvvm/releases/download/1.0.1/app-release.apk)

- [firm下载(推荐)](http://d.6short.com/v9q7)

- 扫码下载(推荐)

![](https://upload-images.jianshu.io/upload_images/9305757-4999111e26d5e93a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
  
  
## 1.如何使用

- 1.1 启用dataBinding 如果你需要用到dataBinding,请在主工程的build中加入，不用可以忽略这一步：
**不想用dataBinding的继承BaseVmActivity，想用dataBinding的继承BaseVmDbActivity**
```
  dataBinding{
        enabled = true
  }
```
- 1.2 依赖
```
dependencies {
  ...
  implementation 'me.hegj:JetpackMvvm:1.0.2'
}

```
## 2.登录示例（可以看demo，预先创建你的基类，并实现相关的方法）

- 2.1 LoginFragment继承基类传入相关泛型
```
class LoginFragment:BaseFragment<LoginRegisterViewModel, FragmentLoginBinding>() {

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
                //登录成功 退出，，
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
- 3.3 创建数据仓库，不管数据是请求网络还是本地数据库拉取都在这里实现

```
class LoginRepository {

    //登录 自带协程
    suspend fun login(username: String, password: String): ApiResponse<UserInfo> {
        return NetworkApi().service.login(username, password)
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

