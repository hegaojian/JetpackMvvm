@file:Suppress("UNCHECKED_CAST","unused","NON_PUBLIC_CALL_FROM_PUBLIC_INLINE","SpellCheckingInspection")

package me.hgj.jetpackmvvm.ext.util.intent
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.BaseBundle
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import java.io.Serializable
import java.lang.reflect.Field
import java.lang.reflect.Method
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

object ActivityMessenger {
    private var sRequestCode = 0
        set(value) {
            field = if (value >= Integer.MAX_VALUE) 1 else value
        }

    /**
     * 作用同 [Activity.startActivity]。
     *
     * 示例：
     * ```kotlin
     * // 不携带参数
     * openActivity<TestActivity>(this)
     *
     * // 携带参数
     * openActivity<TestActivity>(this, "Key" to "Value")
     * ```
     */
    inline fun <reified TARGET : Activity> openActivity(
        starter: FragmentActivity,
        vararg params: Pair<String, Any>
    ) = starter.startActivity(Intent(starter, TARGET::class.java).putExtras(*params))

    inline fun <reified TARGET : Activity> openActivity(
        starter: Fragment,
        vararg params: Pair<String, Any>
    ) = starter.startActivity(Intent(starter.context, TARGET::class.java).putExtras(*params))

    inline fun <reified TARGET : Activity> openActivity(
        starter: Context,
        vararg params: Pair<String, Any>
    ) = starter.startActivity(Intent(starter, TARGET::class.java).putExtras(*params))

    fun openActivity(
        starter: FragmentActivity,
        target: KClass<out Activity>,
        vararg params: Pair<String, Any>
    ) = starter.startActivity(Intent(starter, target.java).putExtras(*params))

    fun openActivity(
        starter: Fragment,
        target: KClass<out Activity>,
        vararg params: Pair<String, Any>
    ) = starter.startActivity(Intent(starter.context, target.java).putExtras(*params))

    fun openActivity(
        starter: Context,
        target: KClass<out Activity>,
        vararg params: Pair<String, Any>
    ) = starter.startActivity(Intent(starter, target.java).putExtras(*params))

    /**
     * 作用同 [Activity.startActivityForResult]。
     *
     * 示例：
     * ```kotlin
     * openActivityForResult<TestActivity>(this) { result ->
     *     if (result == null) {
     *         // ResultCode != RESULT_OK
     *     } else {
     *         // 成功返回
     *     }
     * }
     * ```
     */
    inline fun <reified TARGET : Activity> openActivityForResult(
        starter: FragmentActivity,
        vararg params: Pair<String, Any>,
        crossinline callback: ((result: Intent?) -> Unit)
    ) = openActivityForResult(starter, TARGET::class, *params, callback = callback)

    inline fun <reified TARGET : Activity> openActivityForResult(
        starter: Fragment,
        vararg params: Pair<String, Any>,
        crossinline callback: ((result: Intent?) -> Unit)
    ) = openActivityForResult(starter.activity, TARGET::class, *params, callback = callback)

    inline fun openActivityForResult(
        starter: FragmentActivity?,
        target: KClass<out Activity>,
        vararg params: Pair<String, Any>,
        crossinline callback: ((result: Intent?) -> Unit)
    ) {
        starter ?: return
        openActivityForResult(starter, Intent(starter, target.java).putExtras(*params), callback)
    }

    inline fun openActivityForResult(
        starter: FragmentActivity?,
        intent: Intent, crossinline callback: ((result: Intent?) -> Unit)
    ) {
        starter ?: return
        val fm = starter.supportFragmentManager
        val fragment = GhostFragment()
        fragment.init(++sRequestCode, intent) { result ->
            callback(result)
            fm.beginTransaction().remove(fragment).commitAllowingStateLoss()
        }
        fm.beginTransaction().add(fragment, GhostFragment::class.java.simpleName)
            .commitAllowingStateLoss()
    }

    /**
     * 结束当前页面并携带返回数据。
     *
     * 示例：
     * ```kotlin
     * finish(this, "Key" to "Value")
     * ```
     */
    fun finish(src: Activity, vararg params: Pair<String, Any>) = with(src) {
        setResult(Activity.RESULT_OK, Intent().putExtras(*params))
        finish()
    }

    fun finish(src: Fragment, vararg params: Pair<String, Any>) =
        src.activity?.run { finish(this, *params) }
}

/**
 * Intent 扩展方法，用于批量 put Extras。
 */
fun Intent.putExtras(vararg params: Pair<String, Any>): Intent {
    if (params.isEmpty()) return this
    params.forEach { (key, value) ->
        when (value) {
            is Int -> putExtra(key, value)
            is Byte -> putExtra(key, value)
            is Char -> putExtra(key, value)
            is Long -> putExtra(key, value)
            is Float -> putExtra(key, value)
            is Short -> putExtra(key, value)
            is Double -> putExtra(key, value)
            is Boolean -> putExtra(key, value)
            is Bundle -> putExtra(key, value)
            is String -> putExtra(key, value)
            is IntArray -> putExtra(key, value)
            is ByteArray -> putExtra(key, value)
            is CharArray -> putExtra(key, value)
            is LongArray -> putExtra(key, value)
            is FloatArray -> putExtra(key, value)
            is Parcelable -> putExtra(key, value)
            is ShortArray -> putExtra(key, value)
            is DoubleArray -> putExtra(key, value)
            is BooleanArray -> putExtra(key, value)
            is CharSequence -> putExtra(key, value)
            is Array<*> -> {
                when {
                    value.isArrayOf<String>() ->
                        putExtra(key, value as Array<String?>)
                    value.isArrayOf<Parcelable>() ->
                        putExtra(key, value as Array<Parcelable?>)
                    value.isArrayOf<CharSequence>() ->
                        putExtra(key, value as Array<CharSequence?>)
                    else -> putExtra(key, value)
                }
            }
            is Serializable -> putExtra(key, value)
        }
    }
    return this
}

/**
 * Intent 扩展方法，直接通过 key 获取值。
 */
fun <O> Intent.get(key: String, defaultValue: O? = null): O? {
    try {
        val extras = IntentFieldMethod.mExtras.get(this) as? Bundle ?: return defaultValue
        IntentFieldMethod.unparcel.invoke(extras)
        val map = IntentFieldMethod.mMap.get(extras) as? Map<String, Any> ?: return defaultValue
        return map[key] as? O ?: return defaultValue
    } catch (e: Exception) {
        // Ignore
    }
    return defaultValue
}

fun <O> Bundle.get(key: String, defaultValue: O? = null): O? {
    try {
        IntentFieldMethod.unparcel.invoke(this)
        val map = IntentFieldMethod.mMap.get(this) as? Map<String, Any> ?: return defaultValue
        return map[key] as? O ?: return defaultValue
    } catch (e: Exception) {
        // Ignore
    }
    return defaultValue
}

/**
 * 内部 Fragment，用于 startActivityForResult 回调。
 */
class GhostFragment : Fragment() {
    private var requestCode = -1
    private var intent: Intent? = null
    private var callback: ((result: Intent?) -> Unit)? = null

    fun init(requestCode: Int, intent: Intent, callback: ((result: Intent?) -> Unit)) {
        this.requestCode = requestCode
        this.intent = intent
        this.callback = callback
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        intent?.let { startActivityForResult(it, requestCode) }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == this.requestCode) {
            val result = if (resultCode == Activity.RESULT_OK && data != null) data else null
            callback?.invoke(result)
        }
    }

    override fun onDetach() {
        super.onDetach()
        intent = null
        callback = null
    }
}

/**
 * 内部工具，反射获取 Intent / Bundle 私有字段。
 */
internal object IntentFieldMethod {
    lateinit var mExtras: Field
    lateinit var mMap: Field
    lateinit var unparcel: Method

    init {
        try {
            mExtras = Intent::class.java.getDeclaredField("mExtras")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mMap = BaseBundle::class.java.getDeclaredField("mMap")
                unparcel = BaseBundle::class.java.getDeclaredMethod("unparcel")
            } else {
                mMap = Bundle::class.java.getDeclaredField("mMap")
                unparcel = Bundle::class.java.getDeclaredMethod("unparcel")
            }
            mExtras.isAccessible = true
            mMap.isAccessible = true
            unparcel.isAccessible = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

/**
 * Activity Extras 委托
 */
class ActivityExtras<T>(private val extraName: String, private val defaultValue: T) :
    ReadWriteProperty<Activity, T> {
    private var extra: T? = null

    override fun getValue(thisRef: Activity, property: KProperty<*>): T =
        extra ?: thisRef.intent?.get<T>(extraName)?.also { extra = it } ?: defaultValue.also { extra = it }

    override fun setValue(thisRef: Activity, property: KProperty<*>, value: T) {
        extra = value
    }
}

/**
 * Fragment Extras 委托
 */
class FragmentExtras<T>(private val extraName: String, private val defaultValue: T) :
    ReadWriteProperty<Fragment, T> {
    private var extra: T? = null

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T =
        extra ?: thisRef.arguments?.get<T>(extraName)?.also { extra = it } ?: defaultValue.also { extra = it }

    override fun setValue(thisRef: Fragment, property: KProperty<*>, value: T) {
        extra = value
    }
}

fun <T> extraFrag(extraName: String): FragmentExtras<T?> = FragmentExtras(extraName, null)
fun <T> extraFrag(extraName: String, defaultValue: T): FragmentExtras<T> = FragmentExtras(extraName, defaultValue)
fun <T> extraAct(extraName: String): ActivityExtras<T?> = ActivityExtras(extraName, null)
fun <T> extraAct(extraName: String, defaultValue: T): ActivityExtras<T> = ActivityExtras(extraName, defaultValue)

/**
 * 以下是 Activity / Fragment 扩展方法，统一使用 openActivity / openActivityForResult
 */
inline fun <reified TARGET : Activity> FragmentActivity.openActivity(
    vararg params: Pair<String, Any>
) = startActivity(Intent(this, TARGET::class.java).putExtras(*params))

inline fun <reified TARGET : Activity> Fragment.openActivity(
    vararg params: Pair<String, Any>
) = activity?.run {
    startActivity(Intent(this, TARGET::class.java).putExtras(*params))
}

fun FragmentActivity.openActivity(
    target: KClass<out Activity>, vararg params: Pair<String, Any>
) = startActivity(Intent(this, target.java).putExtras(*params))

fun Fragment.openActivity(
    target: KClass<out Activity>, vararg params: Pair<String, Any>
) = activity?.run {
    startActivity(Intent(this, target.java).putExtras(*params))
}

inline fun <reified TARGET : Activity> FragmentActivity.openActivityForResult(
    vararg params: Pair<String, Any>, crossinline callback: ((result: Intent?) -> Unit)
) = ActivityMessenger.openActivityForResult(this, TARGET::class, *params, callback = callback)

inline fun <reified TARGET : Activity> Fragment.openActivityForResult(
    vararg params: Pair<String, Any>, crossinline callback: ((result: Intent?) -> Unit)
) = activity?.run { ActivityMessenger.openActivityForResult(this, TARGET::class, *params, callback = callback) }

inline fun FragmentActivity.openActivityForResult(
    target: KClass<out Activity>, vararg params: Pair<String, Any>,
    crossinline callback: ((result: Intent?) -> Unit)
) = ActivityMessenger.openActivityForResult(this, target, *params, callback = callback)

inline fun Fragment.openActivityForResult(
    target: KClass<out Activity>, vararg params: Pair<String, Any>,
    crossinline callback: ((result: Intent?) -> Unit)
) = activity?.run { ActivityMessenger.openActivityForResult(this, target, *params, callback = callback) }

fun Activity.finish(vararg params: Pair<String, Any>) = run {
    setResult(Activity.RESULT_OK, Intent().putExtras(*params))
    finish()
}

fun Activity.finish(intent: Intent) = run {
    setResult(Activity.RESULT_OK, intent)
    finish()
}

fun String.toIntent(flags: Int = 0): Intent = Intent(this).setFlags(flags)

inline fun FragmentActivity?.openActivityForResult(
    intent: Intent, crossinline callback: ((result: Intent?) -> Unit)
) = this?.run { ActivityMessenger.openActivityForResult(this, intent, callback) }

inline fun Fragment.openActivityForResult(
    intent: Intent, crossinline callback: ((result: Intent?) -> Unit)
) = activity?.run { ActivityMessenger.openActivityForResult(this, intent, callback) }
