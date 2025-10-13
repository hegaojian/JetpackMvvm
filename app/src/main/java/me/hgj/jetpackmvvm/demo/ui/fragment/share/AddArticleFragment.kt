package me.hgj.jetpackmvvm.demo.ui.fragment.share

import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.afollestad.materialdialogs.customview.customView
import me.hgj.jetpackmvvm.core.data.obs
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.core.base.BaseFragment
import me.hgj.jetpackmvvm.demo.app.event.EventViewModel
import me.hgj.jetpackmvvm.demo.app.core.ext.nav
import me.hgj.jetpackmvvm.demo.app.core.util.UserManager
import me.hgj.jetpackmvvm.demo.databinding.FragmentShareAriticleBinding
import me.hgj.jetpackmvvm.demo.data.vm.ShareViewModel
import me.hgj.jetpackmvvm.ext.util.clickNoRepeat
import me.hgj.jetpackmvvm.ext.view.showDialogMessage
import me.hgj.jetpackmvvm.ext.view.textString


/**
 * 作者　: hegaojian
 * 时间　: 2020/3/10
 * 描述　:
 */
class AddArticleFragment : BaseFragment<ShareViewModel, FragmentShareAriticleBinding>() {

    override val showTitle = true

    override val title = "分享文章"

    override fun initView(savedInstanceState: Bundle?) {
        UserManager.user?.let { mBind.shareUsername.hint = it.nickname.ifEmpty { it.username } }
        baseBinding.includeToolbar.toolbar.run {
            inflateMenu(R.menu.share_menu)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.share_rules -> {
                        //分享规则
                        MaterialDialog(mActivity, BottomSheet())
                            .show {
                                title(text = "温馨提示")
                                customView(R.layout.layout_share_tips,scrollable = true,horizontalPadding = true)
                                positiveButton(text = "知道了")
                                cornerRadius(16f)
                            }

                    }
                }
                true
            }
        }
    }

    override fun onBindViewClick() {
        mBind.shareSubmit.clickNoRepeat {
            val shareTitle = mBind.shareTitle.textString()
            val shareUrl = mBind.shareUrl.textString()
            when {
                shareTitle.isEmpty() -> showDialogMessage("请填写文章标题")
                shareUrl.isEmpty() -> showDialogMessage("请填写文章链接")
                else -> {
                    showDialogMessage(
                        "确定分享吗？", positiveButtonText = "分享", negativeButtonText = "取消",
                        positiveAction = {
                            mViewModel.addShareArticle(shareTitle,shareUrl).obs(viewLifecycleOwner) {
                                onSuccess {
                                    EventViewModel.shareArticleEvent.value = true
                                    nav().navigateUp()
                                }
                                onError {
                                    showDialogMessage(it.msg)
                                }
                            }
                        })
                }
            }
        }
    }

}