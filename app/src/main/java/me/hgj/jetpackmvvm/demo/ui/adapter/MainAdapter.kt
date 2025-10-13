package me.hgj.jetpackmvvm.demo.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import me.hgj.jetpackmvvm.demo.ui.fragment.home.HomeFragment
import me.hgj.jetpackmvvm.demo.ui.fragment.me.MeFragment
import me.hgj.jetpackmvvm.demo.ui.fragment.project.ProjectFragment
import me.hgj.jetpackmvvm.demo.ui.fragment.publicNumber.PublicNumberFragment
import me.hgj.jetpackmvvm.demo.ui.fragment.tree.TreeArrFragment

/**
 * 作者　: hegaojian
 * 时间　: 2023/4/3
 * 描述　:
 */
class MainAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    companion object {
        const val PAGE_HOME = 0
        const val PAGE_PROJECT = 1
        const val PAGE_SYSTEM = 2
        const val PAGE_PUBLIC = 3
        const val PAGE_USER = 4
    }

    // 缓存已创建的 Fragment
    private val fragmentCache = mutableMapOf<Int, Fragment>()

    override fun createFragment(position: Int): Fragment {
        // 如果缓存里有，直接用缓存
        fragmentCache[position]?.let { return it }
        // 没有则创建新的
        val fragment = when (position) {
            PAGE_HOME -> HomeFragment.newInstance()
            PAGE_PROJECT -> ProjectFragment.newInstance()
            PAGE_SYSTEM -> TreeArrFragment.newInstance()
            PAGE_PUBLIC -> PublicNumberFragment.newInstance()
            PAGE_USER -> MeFragment.newInstance()
            else -> throw IllegalArgumentException("Invalid position $position")
        }
        // 放进缓存
        fragmentCache[position] = fragment
        return fragment
    }

    override fun getItemCount(): Int = 5

    /**
     * 提供一个方法外部能拿到已缓存的 Fragment
     */
    fun getFragment(position: Int): Fragment? {
        return fragmentCache[position]
    }
}