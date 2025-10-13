package me.hgj.jetpackmvvm.ext.util

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.Window
import android.view.WindowManager
import me.hgj.jetpackmvvm.core.appContext
import kotlin.jvm.java


/************************************** 单位转换*********************************************** */
/**
 * 像素密度
 */
fun getDisplayMetrics() = appContext.resources.displayMetrics.density

/**
 * dp 转成为 px
 */
fun dp2px(dpValue: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dpValue,
        appContext.resources.displayMetrics
    ).toInt()
}

val Float.dp get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,this, appContext.resources.displayMetrics)

val Int.dp get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), appContext.resources.displayMetrics).toInt()

/**
 * px 转成为 dp
 */
fun px2dp(pxValue: Float) = (pxValue / getDisplayMetrics() + 0.5f).toInt()

/**
 * sp转px
 */
fun sp2px(spVal: Float): Int {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        spVal,
        appContext.resources.displayMetrics
    ).toInt()
}

/**
 * px转sp
 */
fun px2sp(pxVal: Float) = pxVal / appContext.resources.displayMetrics.scaledDensity

/************************************** 屏幕宽高*********************************************** */

/**
 * 获取屏幕宽
 */
fun getScreenWidth(): Int {
    val metric = DisplayMetrics()
    (appContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        .getMetrics(metric)
    return metric.widthPixels
}

/**
 * 获取屏幕高，包含状态栏，但不包含虚拟按键，如1920屏幕只有1794
 */
fun getScreenHeight(): Int {
    val metric = DisplayMetrics()
    (appContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        .getMetrics(metric)
    return metric.heightPixels
}

/**
 * 获取屏幕宽
 */
fun getScreenWidth2(): Int {
    val point = Point()
    (appContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        .getSize(point)
    return point.x
}

/**
 * 获取屏幕高，包含状态栏，但不包含某些手机最下面的【HOME键那一栏】，如1920屏幕只有1794
 */
fun getScreenHeight2(): Int {
    val point = Point()
    (appContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        .getSize(point)
    return point.y
}

/**
 * 获取屏幕原始尺寸高度，包括状态栏以及虚拟功能键高度
 */
fun getAllScreenHeight(): Int {
    val display =
        (appContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
    try {
        val displayMetrics = DisplayMetrics()
        val method =
            Class.forName("android.view.Display").getMethod(
                "getRealMetrics",
                DisplayMetrics::class.java
            )
        method.invoke(display, displayMetrics)
        return displayMetrics.heightPixels
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return 0
}

/*************************** 状态栏、标题栏、虚拟按键**************************************** */

/**
 * 状态栏高度，单位px，一般为25dp
 */
fun getStatusBarHeight(): Int {
    var height = 0
    val resourceId =
        appContext.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        height = appContext.resources.getDimensionPixelSize(resourceId)
    }
    return height
}

/**
 * 状态栏高度，单位px，【注意】要在onWindowFocusChanged中获取才可以
 */
fun getStatusBarHeight2(activity: Activity): Int {
    val rect = Rect()
    //DecorView是Window中的最顶层view，可以从DecorView获取到程序显示的区域，包括标题栏，但不包括状态栏。所以状态栏的高度即为显示区域的top坐标值
    activity.window.decorView.getWindowVisibleDisplayFrame(rect)
    return rect.top
}

/**
 * 标题栏的高度，【注意】要在onWindowFocusChanged中获取才可以
 */
fun getTitleBarHeight(activity: Activity): Int {
    val contentTop =
        activity.window.findViewById<View>(Window.ID_ANDROID_CONTENT)
            .top
    return contentTop - getStatusBarHeight()
}

/**
 * 获取 虚拟按键的高度
 */
fun getBottomBarHeight() = getAllScreenHeight() - getScreenHeight()