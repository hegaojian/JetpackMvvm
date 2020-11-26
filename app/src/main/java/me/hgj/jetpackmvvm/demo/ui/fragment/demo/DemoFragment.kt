package me.hgj.jetpackmvvm.demo.ui.fragment.demo

import android.os.Bundle
import kotlinx.android.synthetic.main.include_toolbar.*
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.base.BaseFragment
import me.hgj.jetpackmvvm.demo.app.ext.initClose
import me.hgj.jetpackmvvm.demo.databinding.FragmentDemoBinding
import me.hgj.jetpackmvvm.demo.viewmodel.state.DemoViewModel
import me.hgj.jetpackmvvm.ext.nav
import me.hgj.jetpackmvvm.ext.navigateAction

/**放一些示例，目前只有 文件下载示例 后面想到什么加什么，作者那个比很懒，佛性添加
 * @author : hgj
 * @date   : 2020/7/13
 */
class DemoFragment : BaseFragment<DemoViewModel, FragmentDemoBinding>() {

    override fun layoutId() = R.layout.fragment_demo

    override fun initView(savedInstanceState: Bundle?) {
        mDatabind.click = ProxyClick()

        toolbar.initClose("示例") {
            nav().navigateUp()
        }
    }


    inner class ProxyClick {
        fun download() {
            //测试一下 普通的下载
            nav().navigateAction(R.id.action_demoFragment_to_downLoadFragment)
        }

        fun downloadLibrary() {
            //测试一下利用三方库下载
            nav().navigateAction(R.id.action_demoFragment_to_downLoadLibraryFragment)
        }
    }


}