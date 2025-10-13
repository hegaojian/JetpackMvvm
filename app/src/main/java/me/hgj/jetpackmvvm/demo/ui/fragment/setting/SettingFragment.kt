package me.hgj.jetpackmvvm.demo.ui.fragment.setting

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.TwoStatePreference
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.customview.customView
import com.drake.brv.utils.setup
import me.hgj.jetpackmvvm.demo.R
import me.hgj.jetpackmvvm.demo.app.core.util.CacheDataManager
import me.hgj.jetpackmvvm.demo.app.core.util.LocalDataUtil
import me.hgj.jetpackmvvm.demo.app.core.util.UserManager
import me.hgj.jetpackmvvm.demo.data.model.CacheConfig
import me.hgj.jetpackmvvm.demo.data.model.entity.BannerResponse
import me.hgj.jetpackmvvm.demo.databinding.LayoutOpenSourceProjectBinding
import me.hgj.jetpackmvvm.demo.databinding.LayoutOpenSourceProjectItemBinding
import me.hgj.jetpackmvvm.demo.ui.activity.WebActivity
import me.hgj.jetpackmvvm.ext.util.getAppVersion
import me.hgj.jetpackmvvm.ext.util.intent.openActivity
import me.hgj.jetpackmvvm.ext.util.toast
import me.hgj.jetpackmvvm.ext.view.showDialogMessage
import me.hgj.jetpackmvvm.ext.view.vertical

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/9
 * 描述　: 系统设置
 */
class SettingFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.root_preferences)
        initData()
        findPreference<Preference>("exit")?.isVisible = UserManager.isLoggedIn//未登录时，退出登录需要隐藏
        findPreference<Preference>("open")?.setOnPreferenceClickListener { preference ->
            false
        }
        findPreference<Preference>("exit")?.setOnPreferenceClickListener { preference ->
            showDialogMessage(
                "确定退出登录吗",
                positiveButtonText = "退出",
                negativeButtonText = "取消",
                positiveAction = {
                    //清空cookie
                    UserManager.clearUser()
                    activity?.finish()
                })
            false
        }

        findPreference<Preference>("clearCache")?.setOnPreferenceClickListener {
            showDialogMessage(
                "确定清理缓存吗",
                positiveButtonText = "清理",
                negativeButtonText = "取消",
                positiveAction = {
                    activity?.let { CacheDataManager.clearAllCache(it as? AppCompatActivity) }
                    initData()
                })
            false
        }
        findPreference<Preference>("version")?.setOnPreferenceClickListener {
            "暂无更新".toast()
            false
        }
        findPreference<Preference>("copyRight")?.setOnPreferenceClickListener {
            activity?.let {
                showDialogMessage(it.getString(R.string.copyright_tip))
            }
            false
        }
        findPreference<Preference>("author")?.setOnPreferenceClickListener {
            showDialogMessage(
                title = "联系作者",
                message = "扣　扣：824868922\n\n微　信：hgj840\n\n邮　箱：824868922@qq.com"
            )
            false
        }
        findPreference<Preference>("project")?.setOnPreferenceClickListener {
            val data = BannerResponse(
                title = "一位练习时长两年半的菜虚鲲制作的玩安卓App",
                url = findPreference<Preference>("project")?.summary.toString()
            )
            openActivity<WebActivity>("banner" to data)
            false
        }
        findPreference<Preference>("open")?.setOnPreferenceClickListener {
            MaterialDialog(requireActivity())
                .show {
                    title(text = "使用到的开源库")
                    val dialogView = LayoutOpenSourceProjectBinding.inflate(LayoutInflater.from(requireActivity()))
                    cornerRadius(4f)
                    customView(view = dialogView.root, horizontalPadding = true, scrollable = true)
                    cancelOnTouchOutside(false)
                    dialogView.projectRv.vertical().setup {
                        addType<BannerResponse>(R.layout.layout_open_source_project_item,)
                        onBind {
                            val model = getModel<BannerResponse>()
                            val binding = getBinding<LayoutOpenSourceProjectItemBinding>()
                            binding.projectName.text = model.title
                        }
                        R.id.projectName.onClick {
                            val model = getModel<BannerResponse>()
                            openActivity<WebActivity>("banner" to model)
                        }
                    }.models = LocalDataUtil.openSourceProjects()
                    positiveButton(text = "关闭")
                }
            false
        }
    }

    /**
     * 初始化设值
     */
    private fun initData() {
        activity?.let {
            findPreference<TwoStatePreference>("top")?.isChecked = CacheConfig.showTop
            findPreference<Preference>("clearCache")?.summary = CacheDataManager.getTotalCacheSize(it)
            findPreference<Preference>("version")?.summary = "当前版本 " + it.getAppVersion()
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceManager.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceManager.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroyView() {
        preferenceManager.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
        super.onDestroyView()
    }

    override fun onSharedPreferenceChanged(p0: SharedPreferences?,key: String?) {
        if (key == "top") {
            CacheConfig.showTop = p0?.getBoolean("top", true)?:true
        }
    }
}

