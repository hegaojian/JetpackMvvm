package me.hgj.jetpackmvvm.ext.util

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import me.hgj.jetpackmvvm.R

/**
 * 作者　：hegaojian
 * 时间　：2025/9/29
 * 说明　：图片加载 封装的Glide
 */

/**
 * 全局配置图片默认 加载失败/占位图（框架使用者想替换的话 可在 Application 里设置成自己的图片资源）
 */
object ImageConfig {
    @DrawableRes
    var error: Int = R.drawable.ic_base_image_error

    /** 是否启用 crossFade 过渡动画（默认开启） */
    var enableCrossFade: Boolean = true

    /** crossFade 时长（毫秒），默认 300 */
    var crossFadeDuration: Int = 300
}

/**
 * 核心加载方法
 */
private fun ImageView.loadAny(
    model: Any?,
    @DrawableRes placeholder: Int? = null,
    @DrawableRes error: Int? = null,
    options: RequestOptions = RequestOptions()
) {
    var req = Glide.with(this.context)
        .load(model)
        .apply(options)
        .diskCacheStrategy(DiskCacheStrategy.ALL)

    // 占位 & 错误图
    placeholder?.let { req = req.placeholder(it) }
    error?.let { req = req.error(it) } ?: ImageConfig.error?.let { req = req.error(it) }

    // crossFade 动画（全局）
    if (ImageConfig.enableCrossFade) {
        val factory = DrawableCrossFadeFactory.Builder(ImageConfig.crossFadeDuration)
            .setCrossFadeEnabled(true)
            .build()
        req = req.transition(DrawableTransitionOptions.withCrossFade(factory))
    }

    req.into(this)
}

/**
 * 普通加载
 */
fun ImageView.load(model: Any?, @DrawableRes placeholder: Int? = null, @DrawableRes error: Int? = null) {
    loadAny(model, placeholder, error)
}

/**
 * 圆形加载
 */
fun ImageView.loadCircle(model: Any?, @DrawableRes placeholder: Int? = null, @DrawableRes error: Int? = null) {
    val options = RequestOptions.bitmapTransform(CircleCrop())
    loadAny(model, placeholder, error, options)
}

/**
 * 圆角加载
 */
fun ImageView.loadRound(model: Any?, radiusDp: Int = 12, @DrawableRes placeholder: Int? = null, @DrawableRes error: Int? = null) {
    val px = (radiusDp * context.resources.displayMetrics.density).toInt()
    val options = RequestOptions().transform(CenterCrop(), RoundedCorners(px))
    loadAny(model, placeholder, error, options)
}

/**
 * 带监听的加载
 */
fun ImageView.loadWithListener(
    model: Any?,
    @DrawableRes placeholder: Int? = null,
    @DrawableRes error: Int? = null,
    onSuccess: (() -> Unit)? = null,
    onFail: (() -> Unit)? = null
) {
    var req = Glide.with(this.context)
        .load(model)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .listener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Drawable?>?,
                isFirstResource: Boolean
            ): Boolean {
                onFail?.invoke()
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Drawable?>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                onSuccess?.invoke()
                return false
            }
        })

    // 占位 & 错误图
    placeholder?.let { req = req.placeholder(it) }
    error?.let { req = req.error(it) } ?: ImageConfig.error.let { req = req.error(it) }

    // crossFade 动画（全局）
    if (ImageConfig.enableCrossFade) {
        val factory = DrawableCrossFadeFactory.Builder(ImageConfig.crossFadeDuration)
            .setCrossFadeEnabled(true)
            .build()
        req = req.transition(DrawableTransitionOptions.withCrossFade(factory))
    }

    req.into(this)
}