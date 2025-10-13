package me.hgj.jetpackmvvm.demo.ui.fragment.lookinfo

import android.os.Bundle
import androidx.lifecycle.Observer
import com.drake.brv.utils.bindingAdapter
import me.hgj.jetpackmvvm.core.data.obs
import me.hgj.jetpackmvvm.demo.app.core.base.BaseFragment
import me.hgj.jetpackmvvm.demo.app.core.ext.collectRefresh
import me.hgj.jetpackmvvm.demo.app.core.ext.refreshArticleData
import me.hgj.jetpackmvvm.demo.app.core.ext.toggleCollect
import me.hgj.jetpackmvvm.demo.app.core.util.UserManager
import me.hgj.jetpackmvvm.demo.app.event.EventViewModel
import me.hgj.jetpackmvvm.demo.data.vm.CollectViewModel
import me.hgj.jetpackmvvm.demo.data.vm.SearchViewModel
import me.hgj.jetpackmvvm.demo.databinding.FragmentLookinfoBinding
import me.hgj.jetpackmvvm.demo.ui.adapter.articleAdapter
import me.hgj.jetpackmvvm.ext.lifecycle.getViewModel
import me.hgj.jetpackmvvm.ext.util.intent.bundle
import me.hgj.jetpackmvvm.ext.util.loadListError
import me.hgj.jetpackmvvm.ext.util.loadListSuccess
import me.hgj.jetpackmvvm.ext.util.loadMore
import me.hgj.jetpackmvvm.ext.util.refresh
import me.hgj.jetpackmvvm.ext.view.divider
import me.hgj.jetpackmvvm.ext.view.vertical
import me.hgj.jetpackmvvm.util.decoration.DividerOrientation

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/4
 * 描述　: 查看他人信息
 */
class LookInfoFragment : BaseFragment<SearchViewModel, FragmentLookinfoBinding>() {

    override val showTitle = true
    override val title = "他的信息"

    //对方的Id
    private var shareId by bundle("")
    private var shareName by bundle("")

    val collectVm: CollectViewModel by getViewModel()

    override fun initView(savedInstanceState: Bundle?) {
        mBind.shareName.text = shareName
        mBind.shareInfo.text = "id：${shareId}  排名：xxx"
        mBind.includeLayout.refresh.refresh {
            getList(refresh = true)
        }.loadMore {
            getList(refresh = false)
        }
        mBind.includeLayout.rv.vertical().divider {
            orientation = DividerOrientation.VERTICAL
            includeVisible = true
            setDivider(8, true)
        }.articleAdapter(canJumpLookInfo = false, onCollectClick = { article, isChecked, result ->
            collectVm.toggleCollect(this, article.id, isChecked, result)
        })
    }

    override fun lazyLoadData() {
        onLoadRetry()
    }

    override fun onLoadRetry() {
        super.onLoadRetry()
        getList(refresh = true, loadingXml = true)
    }

    fun getList(refresh: Boolean = true, loadingXml: Boolean = false) {
        mViewModel.getShareUserData(shareId, refresh = refresh, loadingXml = loadingXml).obs(viewLifecycleOwner)  {
            onSuccess {
                mBind.shareInfo.text = "id：${shareId} 排名：${it.coinInfo.rank}"
                loadListSuccess(
                    it.shareArticles,
                    mBind.includeLayout.rv.bindingAdapter,
                    mBind.includeLayout.refresh,
                    this@LookInfoFragment
                )
            }
            onError {
                loadListError(it, mBind.includeLayout.refresh)
            }
        }
    }

    override fun createObserver() {
        super.createObserver()
        // 收藏变化监听
        EventViewModel.collectEvent.observe(viewLifecycleOwner, Observer {
            mBind.includeLayout.rv.collectRefresh(it)
        })
        // 用户信息变化监听
        UserManager.observeUser().observe(viewLifecycleOwner) {
            mBind.includeLayout.rv.refreshArticleData(it)
        }
    }

    override fun getLoadingView() = mBind.includeLayout.refresh

}