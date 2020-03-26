package me.hgj.jetpackmvvm.util

import android.annotation.SuppressLint
import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

/**
 * 作者　: hegaojian
 * 时间　: 2020/3/26
 * 描述　:
 */
@SuppressLint("CheckResult")
object GlideUtil {

    /**
     * 加载普通图片
     */
    fun loadImage(
        context: Context,
        url: String,
        img: ImageView,
        withCrossFade: Boolean = true
    ) {
        val glideBuilder = Glide.with(context)
            .load(url)
        if (withCrossFade) {
            glideBuilder.transition(DrawableTransitionOptions.withCrossFade(500))
        }
        glideBuilder.into(img)
    }

    /**
     * 加载圆形图片
     */
    fun loadCicleImage(
        context: Context,
        url: String,
        img: ImageView,
        withCrossFade: Boolean = true
    ) {
        val glideBuilder = Glide.with(context)
            .load(url)
        if (withCrossFade) {
            glideBuilder.transition(DrawableTransitionOptions.withCrossFade(500))
        }
        glideBuilder.apply(RequestOptions.bitmapTransform(CircleCrop()))
            .into(img)
    }
}