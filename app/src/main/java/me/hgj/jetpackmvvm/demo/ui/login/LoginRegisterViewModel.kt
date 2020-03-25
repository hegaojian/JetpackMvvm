package me.hgj.jetpackmvvm.demo.ui.login

import androidx.lifecycle.MutableLiveData
import me.hgj.jetpackmvvm.BaseViewModel
import me.hgj.jetpackmvvm.databind.BooleanObservableField
import me.hgj.jetpackmvvm.databind.StringObservableField
import me.hgj.jetpackmvvm.demo.data.bean.UserInfo
import me.hgj.jetpackmvvm.demo.data.repository.LoginRepository
import me.hgj.jetpackmvvm.ext.launchRequest
import me.hgj.jetpackmvvm.state.ViewState

/**
 * 作者　: hegaojian
 * 时间　: 2019/12/23
 * 描述　:登录注册的Viewmodel
 */
class LoginRegisterViewModel : BaseViewModel() {

    //用户名
    var username = StringObservableField()

    //密码(登录注册界面)
    var password = StringObservableField()
    var password2 = StringObservableField()

    //是否显示明文密码（登录注册界面）
    var isShowPwd = BooleanObservableField()
    var isShowPwd2 = BooleanObservableField()

    private val loginRpository: LoginRepository by lazy { LoginRepository() }
    //过滤处理请求结果
    var loginResult = MutableLiveData<ViewState<UserInfo>>()
    //不过滤处理请求结果
//  var loginResult = MutableLiveData<ViewState<ApiResponse<UserInfo>>>()

    fun loginReq() {
        //1.这种是回调在 Activity或Fragment中的
        launchRequest(
            { loginRpository.login(username.get(), password.get()) }//请求体
            , loginResult,//请求的返回结果，请求成功与否都会改变该值，在Activity或fragment中监听回调结果，具体可看loginActivity中的回调
            true,//是否显示等待框，，默认false不显示 可以不填
            "正在登录中..."//等待框内容，默认：请求网络中...  可以不填
        )

        /* //如果想自己拿到请求code执行不同的业务逻辑，不用框架帮你处理请求结果是否成功，可以这样：
         launchRequestNoCheck(
             { loginRpository.login(username.get(), password.get()) }//请求体
             , loginResult,//请求的返回结果，请求成功与否都会改变该值，在Activity或fragment中监听回调结果
             true,//是否显示等待框，，默认false不显示 可以不填
             "正在登录中..."//等待框内容，默认：请求网络中...  可以不填
         )

         //2.这种回调在当前Viewmodel自己做处理结果
         launchResultVM({loginRpository.login(username.get(),password.get())},{
             //请求成功
         },{
             //请求失败
         },isShowDialog = true,loadingMessage = "正在登录中...")
*/
        //如果想自己拿到请求code执行不同的业务逻辑，不用框架帮你处理请求结果是否成功，可以这样：
       /* launchResultVMNoCheck({ loginRpository.login1(username.get(), password.get()) }, {
            //请求成功
        }, {
            //请求失败
        }, isShowDialog = true, loadingMessage = "正在登录中...")*/

    }

    fun registerAndlogin() {
        launchRequest(
            { loginRpository.register(username.get(), password.get()) }
            , loginResult,
            true,
            "正在注册中..."
        )
    }
}