package me.hgj.jetpackmvvm.ext.view

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.view.View
import android.widget.ImageView

/**
 * 作者　: hegaojian
 * 时间　: 2020/11/18
 * 描述　:
 */

/**
 * View显示
 */
fun View?.visible() {
    this?.visibility = View.VISIBLE
}

/**
 * View隐藏
 */
fun View?.gone() {
    this?.visibility = View.GONE
}

/**
 * View占位隐藏
 */
fun View?.inVisible() {
    this?.visibility = View.INVISIBLE
}

/**
 * View是否显示
 */
fun View?.isVisible(): Boolean {
    return this?.visibility == View.VISIBLE
}

/**
 * View是否隐藏
 */
fun View?.isGone(): Boolean {
    return this?.visibility == View.GONE
}

/**
 * View是否占位隐藏
 */
fun View?.isInVisible(): Boolean {
    return this?.visibility == View.INVISIBLE
}

/**
 * @param visible 如果为true 该View显示 否则隐藏
 */
fun View?.visibleOrGone(visible: Boolean) {
    if (visible) {
        this.visible()
    } else {
        this.gone()
    }
}

/**
 * @param visible 如果为true 该View显示 否则占位隐藏
 */
fun View?.visibleOrInvisible(visible: Boolean) {
    if (visible) {
        this.visible()
    } else {
        this.inVisible()
    }
}

/**
 * 显示传入的view集合
 */
fun visibleViews(vararg views: View?) {
    views.forEach {
        it?.visible()
    }
}

/**
 * 隐藏传入的view集合
 */
fun goneViews(vararg views: View?) {
    views.forEach {
        it?.gone()
    }
}

/**
 * 将view转为bitmap
 */
@Deprecated("use View.drawToBitmap()")
fun View.toBitmap(scale: Float = 1f, config: Bitmap.Config = Bitmap.Config.ARGB_8888): Bitmap? {
    if (this is ImageView) {
        if (drawable is BitmapDrawable) return (drawable as BitmapDrawable).bitmap
    }
    this.clearFocus()
    val bitmap = createBitmapSafely(
        (width * scale).toInt(),
        (height * scale).toInt(),
        config,
        1
    )
    if (bitmap != null) {
        Canvas().run {
            setBitmap(bitmap)
            save()
            drawColor(Color.WHITE)
            scale(scale, scale)
            this@toBitmap.draw(this)
            restore()
            setBitmap(null)
        }
    }
    return bitmap
}

private fun createBitmapSafely(width: Int, height: Int, config: Bitmap.Config, retryCount: Int): Bitmap? {
    try {
        return Bitmap.createBitmap(width, height, config)
    } catch (e: OutOfMemoryError) {
        e.printStackTrace()
        if (retryCount > 0) {
            System.gc()
            return createBitmapSafely(width, height, config, retryCount - 1)
        }
        return null
    }
}



