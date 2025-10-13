package me.hgj.jetpackmvvm.demo.app.core.ext

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import me.hgj.jetpackmvvm.demo.app.core.util.UserManager
import me.hgj.jetpackmvvm.demo.ui.activity.LoginActivity
import me.hgj.jetpackmvvm.ext.util.currentActivity
import me.hgj.jetpackmvvm.ext.util.intent.openActivity

/**
 * 作者　: hegaojian
 * 时间　: 2020/5/2
 * 描述　:
 */
fun Fragment.nav(): NavController {
    return findNavController(this)
}

fun nav(view: View): NavController {
    return view.findNavController()
}

/**
 * 拦截登录操作，如果没有登录跳转登录，登录过了贼执行你的方法
 */
fun NavController.jumpByLogin(action: (NavController) -> Unit = {}) {
    if (UserManager.isLoggedIn) {
        action(this)
    } else {
        (currentActivity as? AppCompatActivity)?.openActivity<LoginActivity>()
    }
}
