[![Platform][1]][2] [![GitHub license][3]][4]  [![GitHub license][5]][6] 

[1]:https://img.shields.io/badge/platform-Android-blue.svg  
[2]:https://github.com/hegaojian/JetpackMvvm
[3]:https://img.shields.io/github/release/hegaojian/JetpackMvvm.svg
[4]:https://github.com/hegaojian/JetpackMvvm/releases/latest
[5]:https://img.shields.io/badge/license-Apache%202-blue.svg
[6]:https://github.com/hegaojian/JetpackMvvm/blob/master/LICENSE

# :chicken::chicken::chicken:JetPackMvvm
- **基于MVVM模式集成谷歌官方推荐的JetPack组件库：LiveData、ViewModel、Lifecycle、Navigation组件**
- **使用kotlin语言，添加大量拓展函数，简化代码**
- **加入Retrofit网络请求,协程，帮你简化各种操作，让你快速请求网络**  

## 演示Demo
 已用该库重构了我之前的玩安卓项目，利用Navigation组件以单Activity+Fragment架构编写，优化了很多代码，对比之前的mvp项目，开发效率与舒适度要提高了不少，想看之前MVP的项目可以去 [https://github.com/hegaojian/WanAndroid](https://github.com/hegaojian/WanAndroid) 
 
**打个小广告** 新建了个项目框架，封装了大部分需求，去除了navigation，网络请求使用了 RxHttp，使用更方便，开发速度更高，有兴趣的可以star一下看看 [地址](https://github.com/hegaojian/ProjectTemplate)
 
#### 效果图展示 
![项目效果图](https://upload-images.jianshu.io/upload_images/9305757-818106225dd01e65.gif?imageMogr2/auto-orient/strip)
 
#### APK下载：

- [Github下载](https://github.com/hegaojian/JetpackMvvm/releases/download/1.1.8/app-release.apk)

- [firm下载(推荐)](http://d.6short.com/v9q7)

- 扫码下载(推荐)

![](https://upload-images.jianshu.io/upload_images/9305757-4999111e26d5e93a.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
  
  
## 1.如何集成

- **1.1 在root's build.gradle中加入Jitpack仓库**

```
buildscript {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

- **1.2 在app's build.gradle中添加依赖**

```
dependencies {
  ...
  implementation 'com.github.hegaojian:JetpackMvvm:1.2.2'
}
```

- **1.3 在app's build.gradle中，android 模块下开启DataBinding(如果你不想用DataBinding,请忽略这一步)**

```
AndroidStudio 4.0 以下版本------>
android {
    ...
    dataBinding {
        enabled = true 
    }
}

AndroidStudio 4.0及以上版本 ------>
android {
    ...
   buildFeatures {
        dataBinding = true
    }
}
 
```

## 2.继承基类
一般我们项目中都会有一套自己定义的符合业务需求的基类 ***BaseActivity/BaseFragment***，所以我们的基类需要**继承本框架的Base类**

- 不想用Databinding的-------可以继承 BaseVmActivity/BaseVmFragment
- 用Databinding的-----------可以继承BaseVmDbActivity/BaseVmDbFragment**

**Activity：**

```
abstract class BaseActivity<VM : BaseViewModel, DB : ViewDataBinding> : BaseVmDbActivity<VM, DB>() {
     /**
     * 当前Activity绑定的视图布局Id abstract修饰供子类实现
     */
    abstract override fun layoutId(): Int
    /**
     * 当前Activityc创建后调用的方法 abstract修饰供子类实现
     */
    abstract override fun initView(savedInstanceState: Bundle?)

    /**
     * 创建liveData数据观察
     */
    override override fun createObserver()


    /**
     * 打开等待框 在这里实现你的等待框展示
     */
    override fun showLoading(message: String) {
       ...
    }

    /**
     * 关闭等待框 在这里实现你的等待框关闭
     */
    override fun dismissLoading() {
       ...
    }
}
```
**Fragment：**
```
abstract class BaseFragment<VM : BaseViewModel,DB:ViewDataBinding> : BaseVmDbFragment<VM,DB>() {
    /**
     * 当前Fragment绑定的视图布局Id abstract修饰供子类实现
     */
    abstract override fun layoutId(): Int
   
    abstract override fun initView(savedInstanceState: Bundle?)

    /**
     * 懒加载 只有当前fragment视图显示时才会触发该方法 abstract修饰供子类实现
     */
    abstract override fun lazyLoadData()

    /**
     * 创建liveData数据观察 懒加载之后才会触发
     */
    override override fun createObserver()
  
    /**
     * Fragment执行onViewCreated后触发的方法 
     */
    override fun initData() {

    }
    
   /**
     * 打开等待框 在这里实现你的等待框展示
     */
    override fun showLoading(message: String) {
       ...
    }

    /**
     * 关闭等待框 在这里实现你的等待框关闭
     */
    override fun dismissLoading() {
       ...
    }
}
```

## 3.编写一个登录功能

- **3.1 编写fragment_login.xml界面后转换成 databind 布局（鼠标停在根布局，Alt+Enter 点击提示 Convert to data binding layout即可）**
```
<layout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:bind="http://schemas.android.com/tools">
    <data>
       
    </data>
    <LinearLayout>
       ....
    </LinearLayout>
 </layout>   
```
- **3.2 创建LoginViewModel类继承BaseViewModel**

```
class LoginViewModel : BaseViewModel() {
  
}
```

- **3.3 创建LoginFragment 继承基类传入相关泛型,第一个泛型为你创建的LoginViewModel,第二个泛型为ViewDataBind，保存fragment_login.xml后databinding会生成一个FragmentLoginBinding类。（如果没有生成，试着点击Build->Clean Project）**
```
class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>() {
    
    /**
     *  当前fragment绑定的布局
     */
    override fun layoutId() = R.layout.fragment_login
    
    /**
     *  初始化操作
     */
    override fun initView(savedInstanceState: Bundle?) {
        ...
    }
    
    /**
     *  fragment 懒加载
     */
    override fun lazyLoadData() { 
        ...
    }
}
```

## 4.网络请求（Retrofit+协程）

- **4.1 新建请求配置类继承 BaseNetworkApi 示例：**
```
class NetworkApi : BaseNetworkApi() {

   companion object {
         
        val instance: NetworkApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { NetworkApi() }

        //双重校验锁式-单例 封装NetApiService 方便直接快速调用
        val service: ApiService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            instance.getApi(ApiService::class.java, ApiService.SERVER_URL)
        }
    }
   
    /**
     * 实现重写父类的setHttpClientBuilder方法，
     * 在这里可以添加拦截器，可以对 OkHttpClient.Builder 做任意你想要做的骚操作
     */
    override fun setHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        builder.apply {
            //示例：添加公共heads，可以存放token，公共参数等， 注意要设置在日志拦截器之前，不然Log中会不显示head信息
            addInterceptor(MyHeadInterceptor())
            // 日志拦截器
            addInterceptor(LogInterceptor())
            //超时时间 连接、读、写
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(5, TimeUnit.SECONDS)
            writeTimeout(5, TimeUnit.SECONDS)
        }
        return builder
    }

    /**
     * 实现重写父类的setRetrofitBuilder方法，
     * 在这里可以对Retrofit.Builder做任意骚操作，比如添加GSON解析器，protobuf等
     */
    override fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder {
        return builder.apply {
            addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            addCallAdapterFactory(CoroutineCallAdapterFactory())
        }
    }
}
```


- **4.2如果你请求服务器返回的数据有基类（没有可忽略这一步）例如:**
```
{
    "data": ...,
    "errorCode": 0,
    "errorMsg": ""
}
```
该示例格式是 [玩Android Api](https://www.wanandroid.com/blog/show/2)返回的数据格式，如果errorCode等于0 请求成功，否则请求失败
作为开发者的角度来说，我们主要是想得到脱壳数据-data，且不想每次都判断errorCode==0请求是否成功或失败
这时我们可以在服务器返回数据基类中继承BaseResponse，实现相关方法：

```
data class ApiResponse<T>(var errorCode: Int, var errorMsg: String, var data: T) : BaseResponse<T>() {

    // 这里是示例，wanandroid 网站返回的 错误码为 0 就代表请求成功，请你根据自己的业务需求来编写
    override fun isSucces() = errorCode == 0

    override fun getResponseCode() = errorCode

    override fun getResponseData() = data

    override fun getResponseMsg() = errorMsg

}
```
- **4.3 在ViewModel中发起请求，所有请求都是在viewModelScope中启动，请求会发生在IO线程，最终回调在主线程上，当页面销毁的时候，请求会统一取消，不用担心内存泄露的风险，框架做了2种请求使用方式**  

**1、将请求数据包装给ResultState，在Activity/Fragment中去监听ResultState拿到数据做处理**

```
class RequestLoginViewModel: BaseViewModel {

  //自动脱壳过滤处理请求结果，自动判断结果是否成功
    var loginResult = MutableLiveData<ResultState<UserInfo>>()
    
  //不用框架帮脱壳
    var loginResult2 = MutableLiveData<ResultState<ApiResponse<UserInfo>>>()
    
  fun login(username: String, password: String){
   //1.在 Activity/Fragment的监听回调中拿到已脱壳的数据（项目有基类的可以用）
        request(
            { HttpRequestCoroutine.login(username, password) }, //请求体
            loginResult,//请求的结果接收者，请求成功与否都会改变该值，在Activity或fragment中监听回调结果，具体可看loginActivity中的回调
            true,//是否显示等待框，，默认false不显示 可以默认不传
            "正在登录中..."//等待框内容，可以默认不填请求网络中...
        )
        
   //2.在Activity/Fragment中的监听拿到未脱壳的数据，你可以自己根据code做业务需求操作（项目没有基类的可以用）
        requestNoCheck(
          {HttpRequestCoroutine.login(username,password)},
          loginResult2,
          true,
          "正在登录中...") 
}


class LoginFragment : BaseFragment<LoginViewModel, FragmentLoginBinding>() {
    
    private val requestLoginRegisterViewModel: RequestLoginRegisterViewModel by viewModels()
    
    /**
     *  当前fragment绑定的布局
     */
    override fun layoutId() = R.layout.fragment_login
    
    /**
     *  初始化操作
     */
    override fun initView(savedInstanceState: Bundle?) {
        ...
    }
    
    /**
     *  fragment 懒加载
     */
    override fun lazyLoadData() { 
        ...
    }
    
    override fun createObserver(){
      //脱壳
       requestLoginRegisterViewModel.loginResult.observe(viewLifecycleOwner,
            Observer { resultState ->
                parseState(resultState, {
                    //登录成功 打印用户
                    it.username.logd()
                }, {
                    //登录失败(网络连接问题，服务器的结果码不正确...异常都会走在这里)
                    showMessage(it.errorMsg)
                })
            })
    
       //不脱壳
       requestLoginRegisterViewModel.loginResult2.observe(viewLifecycleOwner, Observer {resultState ->
               parseState(resultState,{
                   if(it.errorCode==0){
                       //登录成功 打印用户名
                       it.data.username.logd()
                   }else{
                       //登录失败
                       showMessage(it.errorMsg)
                   }
               },{
                   //请求发生了异常
                   showMessage(it.errorMsg)
               })
           })
   } 
}
```

**2、 直接在当前ViewModel中拿到请求结果**

```
class RequestLoginViewModel : BaseViewModel() {
    
  fun login(username: String, password: String){
   //1.拿到已脱壳的数据（项目有基类的可以用）
     request({HttpRequestCoroutine.login(username,password)},{
             //请求成功 已自动处理了 请求结果是否正常
             it.username.logd()
         },{
             //请求失败 网络异常，或者请求结果码错误都会回调在这里
             it.errorMsg.logd()
         },true,"正在登录中...")
        
   //2.拿到未脱壳的数据，你可以自己根据code做业务需求操作（项目没有基类或者不想框架帮忙脱壳的可以用）
       requestNoCheck({HttpRequestCoroutine.login(username,password)},{
            //请求成功 自己拿到数据做业务需求操作
            if(it.errorCode==0){
                //结果正确
                it.data.username.logd()
            }else{
                //结果错误
                it.errorMsg.logd()
            }
        },{
            //请求失败 网络异常回调在这里
            it.errorMsg.logd()
        },true,"正在登录中...")
}
 
```
### 注意：使用该请求方式时需要注意，如果该ViewModel并不是跟Activity/Fragment绑定的泛型ViewModel，而是
val mainViewModel:MainViewModel by viewModels()
或者
val mainViewModel：MainViewModel by activityViewModels()
获取的
如果请求时要弹出loading，你需要在Activity | Fragment中添加以下代码：
### addLoadingObserve(viewModel)

## 4.4 开启打印日志开关
设置全局jetpackMvvmLog变量 是否打开请求日志，默认false不打印，如需要打印日志功能，请设值为 true

## 5.获取ViewModel
- **5.1我们的activity/fragment会有多个ViewModel，按传统的写法感觉有点累**
```
 val mainViewModel = ViewModelProvider(this,
            ViewModelProvider.AndroidViewModelFactory(application)).get(MainViewModel::class.java)
```
**现在官方Ktx有拓展函数可以轻松调用
```
//在activity中获取当前Activity级别作用域的ViewModel
 private val mainViewModel:MainViewModel by viewModels()
 
//在activity中获取Application级别作用域的ViewModel（注，这个是本框架提供的，Application类继承框架的BaseApp才有用）
 private val mainViewModel by lazy { getAppViewModel<MainViewModel>()}

//在fragment中获取当前Fragment级别作用域的ViewModel
 private val mainViewModel:MainViewModel by viewModels()

//在fragment中获取父类Activity级别作用域的ViewModel
private val mainViewModel：MainViewModel by activityViewModels()

//在fragment中获取Application级别作用域的ViewModel（注，这个是本框架提供的，Application类继承框架的BaseApp才有用）
private val mainViewModel by lazy { getAppViewModel<MainViewModel>()}
```
## 6.写了一些常用的拓展函数
```
 算了不写了，这个不重要，想具体看的话可以在
 me.hgj.jetpackmvvm.ext.util
 me.hgj.jetpackmvvm.ext.view
 的包中看，反正你也可以自己写，按照自己的喜好与需求来
```

## 7.混淆

```
-keep class me.hgj.jetpackmvvm.**{*;}
-keep class com.google.android.material.** {*;}
-keep class androidx.** {*;}
-keep public class * extends androidx.**
-keep interface androidx.** {*;}
-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**
-dontwarn androidx.**
```


## 感谢
- [Jetpack-MVVM-Best-Practice](https://github.com/KunMinX/Jetpack-MVVM-Best-Practice)
- [重学安卓](https://xiaozhuanlan.com/kunminx?rel=8184827882)

## 联系
- QQ交流群：419581249

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

