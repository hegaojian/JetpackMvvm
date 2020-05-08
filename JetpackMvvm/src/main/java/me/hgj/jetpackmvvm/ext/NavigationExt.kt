package me.hgj.jetpackmvvm.ext

import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment

/**
 * 作者　: hegaojian
 * 时间　: 2020/5/2
 * 描述　:
 */
fun Fragment.nav(): NavController {
    return NavHostFragment.findNavController(this)
}

fun nav(view:View): NavController {
    return Navigation.findNavController(view)
}

