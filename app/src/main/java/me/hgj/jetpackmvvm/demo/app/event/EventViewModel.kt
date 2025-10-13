package me.hgj.jetpackmvvm.demo.app.event

import com.kunminx.architecture.domain.message.MutableResult
import me.hgj.jetpackmvvm.base.vm.BaseViewModel
import me.hgj.jetpackmvvm.demo.data.model.entity.BannerResponse
import me.hgj.jetpackmvvm.demo.data.model.entity.CollectBus

/**
 * 作者　: hegaojian
 * 时间　: 2019/5/2
 * 描述　: 在这里发送全局事件总线
 */
object EventViewModel : BaseViewModel() {

    /** 全局收藏，在任意一个地方收藏或取消收藏，监听该值的界面都会收到消息 */
    val collectEvent = MutableResult<CollectBus>()

    /** 分享文章通知 */
    val shareArticleEvent = MutableResult<Boolean>()


}