package me.hgj.jetpackmvvm.ext.util

import android.os.Parcelable
import com.tencent.mmkv.MMKV
import me.hgj.jetpackmvvm.core.appContext
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


/**
 * 获取MMKV
 */
val mmkv: MMKV by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
    MMKV.mmkvWithID(appContext.packageName)
}

/**
 * 作者　：hegaojian
 * 时间　：2025/9/28
 * 说明　：非空，必须提供默认值
 * 这是一个基于 MMKV 的缓存属性委托，作用是让你在声明变量时
 * 自动完成数据的存储和读取。
 *
 * 使用方法：
 * ```
 * var isLogin by Cache(false) //boolean
 * var userName by Cache("") //String
 * var tags by Cache(setOf()) //Set<String>
 * var user by Cache(User(0, "张三"))  // Parcelable 对象
 *```
 * 特点：
 * 1. 自动使用变量名作为缓存 key，不需要手动指定。
 * 2. 支持常用类型：Boolean、Int、Long、Float、Double、String、
 *    ByteArray、Set<String>、Parcelable。
 * 3. 读取变量时自动从 MMKV 获取，赋值变量时自动写入 MMKV。
 * 4. 让缓存操作像普通变量一样简洁，无需每次调用 mmkv.encode/decode。
 */
class Cache<T : Any>(
    private val default: T,
    private val key: String? = null,
) : ReadWriteProperty<Any?, T> {

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        val actualKey = key ?: property.name
        return when (default) {
            is Boolean -> mmkv.decodeBool(actualKey, default) as T
            is Int -> mmkv.decodeInt(actualKey, default) as T
            is Long -> mmkv.decodeLong(actualKey, default) as T
            is Float -> mmkv.decodeFloat(actualKey, default) as T
            is Double -> mmkv.decodeDouble(actualKey, default) as T
            is String -> mmkv.decodeString(actualKey, default) as T
            is ByteArray -> mmkv.decodeBytes(actualKey, default) as T
            is Set<*> -> mmkv.decodeStringSet(actualKey, default as Set<String>) as T
            is Parcelable -> mmkv.decodeParcelable(
                actualKey,
                default.javaClass as Class<Parcelable>,
                default
            ) as T
            else -> throw IllegalArgumentException("Unsupported type: ${default::class.java}")
        }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        val actualKey = key ?: property.name
        when (value) {
            is Boolean -> mmkv.encode(actualKey, value)
            is Int -> mmkv.encode(actualKey, value)
            is Long -> mmkv.encode(actualKey, value)
            is Float -> mmkv.encode(actualKey, value)
            is Double -> mmkv.encode(actualKey, value)
            is String -> mmkv.encode(actualKey, value)
            is ByteArray -> mmkv.encode(actualKey, value)
            is Set<*> -> mmkv.encode(actualKey, value as Set<String>)
            is Parcelable -> mmkv.encode(actualKey, value)
            else -> throw IllegalArgumentException("Unsupported type: ${value::class.java}")
        }
    }
}

/**
 * 使用内联函数和 reified 来获取类型信息
 * ```
 * var userCache : UserInfo? by cacheNullable()
 * ```
 */
inline fun <reified T> cacheNullable(
    default: T? = null,
    key: String? = null
): ReadWriteProperty<Any?, T?> = CacheNullable(T::class.java, default, key)


/**
 *
 * 作者　：hegaojian
 * 时间　：2025/9/28
 * 说明　：可空，必须传入类名，一般用于对象
 * 这是一个基于 MMKV 的缓存属性委托，作用是让你在声明变量时
 * 自动完成数据的存储和读取。
 *
 * 使用方法：
 * ```
 * var userCache : UserInfo? by CacheNullable(UserInfo::class.java)
 *
 * 建议用封装的内联方式调用：
 * var userCache : UserInfo? by cacheNullable()
 *
 *```
 * 1. 读取变量时自动从 MMKV 获取，赋值变量时自动写入 MMKV。
 * 2. 让缓存操作像普通变量一样简洁，无需每次调用 mmkv.encode/decode。
 */
class CacheNullable<T>(
    private val clazz: Class<T>,
    private val default: T? = null,
    private val key: String? = null,
) : ReadWriteProperty<Any?, T?> {

    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        val actualKey = key ?: property.name

        // 如果有默认值，使用默认值类型
        if (default != null) {
            return decodeWithDefault(actualKey, default)
        }

        // 没有默认值，使用显式指定的类型
        return decodeByClass(actualKey, clazz)
    }

    @Suppress("UNCHECKED_CAST")
    private fun <R> decodeWithDefault(key: String, default: R): R {
        return when (default) {
            is Boolean -> mmkv.decodeBool(key, default) as R
            is Int -> mmkv.decodeInt(key, default) as R
            is Long -> mmkv.decodeLong(key, default) as R
            is Float -> mmkv.decodeFloat(key, default) as R
            is Double -> mmkv.decodeDouble(key, default) as R
            is String -> mmkv.decodeString(key, default) as R
            is ByteArray -> mmkv.decodeBytes(key, default) as R
            is Set<*> -> mmkv.decodeStringSet(key, default as Set<String>) as R
            is Parcelable -> mmkv.decodeParcelable(
                key,
                default.javaClass as Class<Parcelable>,
                default
            ) as R
            else -> throw IllegalArgumentException("Unsupported type: ${default!!::class.java}")
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun <R> decodeByClass(key: String, clazz: Class<R>): R? {
        if (!mmkv.contains(key)) {
            return null
        }

        return when {
            clazz == Boolean::class.java -> mmkv.decodeBool(key, false) as R
            clazz == Int::class.java -> mmkv.decodeInt(key, 0) as R
            clazz == Long::class.java -> mmkv.decodeLong(key, 0L) as R
            clazz == Float::class.java -> mmkv.decodeFloat(key, 0f) as R
            clazz == Double::class.java -> mmkv.decodeDouble(key, 0.0) as R
            clazz == String::class.java -> mmkv.decodeString(key, null) as R
            clazz == ByteArray::class.java -> mmkv.decodeBytes(key, null) as R
            Set::class.java.isAssignableFrom(clazz) -> mmkv.decodeStringSet(key, null) as R
            Parcelable::class.java.isAssignableFrom(clazz) -> {
                mmkv.decodeParcelable(key, clazz as Class<Parcelable>, null) as R
            }
            else -> throw IllegalArgumentException("Unsupported type: $clazz")
        }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        val actualKey = key ?: property.name

        if (value == null) {
            mmkv.removeValueForKey(actualKey)
            return
        }

        when (value) {
            is Boolean -> mmkv.encode(actualKey, value)
            is Int -> mmkv.encode(actualKey, value)
            is Long -> mmkv.encode(actualKey, value)
            is Float -> mmkv.encode(actualKey, value)
            is Double -> mmkv.encode(actualKey, value)
            is String -> mmkv.encode(actualKey, value)
            is ByteArray -> mmkv.encode(actualKey, value)
            is Set<*> -> mmkv.encode(actualKey, value as Set<String>)
            is Parcelable -> mmkv.encode(actualKey, value)
            else -> throw IllegalArgumentException("Unsupported type: ${value::class.java}")
        }
    }
}
