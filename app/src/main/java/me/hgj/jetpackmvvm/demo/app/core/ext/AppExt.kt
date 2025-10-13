package me.hgj.jetpackmvvm.demo.app.core.ext

import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.drake.brv.utils.bindingAdapter
import me.hgj.jetpackmvvm.core.data.obs
import me.hgj.jetpackmvvm.demo.app.core.base.BaseFragment
import me.hgj.jetpackmvvm.demo.app.event.EventViewModel
import me.hgj.jetpackmvvm.demo.data.model.entity.ArticleResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.CollectBus
import me.hgj.jetpackmvvm.demo.data.model.entity.UserInfo
import me.hgj.jetpackmvvm.demo.data.vm.CollectViewModel
import me.hgj.jetpackmvvm.ext.util.toast
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

/**
 * 获取进程号对应的进程名
 *
 * @param pid 进程号
 * @return 进程名
 */
fun getProcessName(pid: Int): String? {
    var reader: BufferedReader? = null
    try {
        reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
        var processName = reader.readLine()
        if (!TextUtils.isEmpty(processName)) {
            processName = processName.trim { it <= ' ' }
        }
        return processName
    } catch (throwable: Throwable) {
        throwable.printStackTrace()
    } finally {
        try {
            reader?.close()
        } catch (exception: IOException) {
            exception.printStackTrace()
        }

    }
    return null
}

/**
 * 加入qq聊天群
 */
fun Fragment.joinQQGroup(key: String): Boolean {
    val intent = Intent()
    intent.data =
        Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D$key")
    // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    return try {
        startActivity(intent)
        true
    } catch (e: Exception) {
        // 未安装手Q或安装的版本不支持
        "未安装手机QQ或安装的版本不支持".toast()
        false
    }
}

/**
 * 收藏，因为在很多地方都需要收藏，而且是重复代码，这里做了封装
 */
fun CollectViewModel.toggleCollect(
    fm: BaseFragment<*,*>,
    articleId: String,
    isChecked: Boolean,
    result: (Boolean) -> Unit
) {
    val request = if (!isChecked) {
        collectArticle(articleId)
    } else {
        unCollectArticle(articleId)
    }

    request.obs(fm) {
        onSuccess {
            //请求成功那么 发送全局收藏消息事件
            EventViewModel.collectEvent.value = CollectBus(articleId, !isChecked)
            result(true)
        }
        onError {
            it.msg.toast()
            result(false)
        }
    }
}

/**
 * 公共重复代码封装，根据用户信息变化刷新对应的文章数据，主要在于改变收藏状态
 */
fun RecyclerView.refreshArticleData(userInfo: UserInfo?){
    val models = bindingAdapter.models?: arrayListOf()
    if (userInfo != null) {
        userInfo.collectIds.forEach { id ->
            for (item in models) {
                val article = item as? ArticleResponse ?: continue
                if (article.id == id) {
                    article.collect = true
                    break
                }
            }
        }
    } else {
        for (item in models) {
            if (item is ArticleResponse) {
                item.collect = false
            }
        }
    }
    bindingAdapter.models = models
}

/**
 * 公共重复代码封装，根据其他地方收藏状态，刷新当前对应的文章数据，改变收藏状态
 */
fun RecyclerView.collectRefresh(collectBus:CollectBus){
    val models = bindingAdapter.models
    models?.forEachIndexed { index, item ->
        if (item is ArticleResponse && item.id == collectBus.id) {
            item.collect = collectBus.collect
            bindingAdapter.notifyItemChanged(index)
            return@forEachIndexed
        }
    }
}