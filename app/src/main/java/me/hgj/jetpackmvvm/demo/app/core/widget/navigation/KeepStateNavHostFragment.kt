package me.hgj.jetpackmvvm.demo.app.core.widget.navigation

import androidx.navigation.NavHostController
import androidx.navigation.fragment.NavHostFragment

class KeepStateNavHostFragment : NavHostFragment() {
    override fun onCreateNavHostController(navHostController: NavHostController) {
        super.onCreateNavHostController(navHostController)
        // 注册自定义 Navigator
        val navigator = KeepStateNavigator(
            requireContext(),
            childFragmentManager,
            id
        )
        navHostController.navigatorProvider.addNavigator(navigator)
    }
}
