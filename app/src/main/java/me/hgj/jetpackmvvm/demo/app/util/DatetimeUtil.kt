package me.hgj.jetpackmvvm.demo.app.util

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


/**
 * 作者　: hegaojian
 * 时间　: 2020/3/10
 * 描述　: 时间工具类
 */

object DatetimeUtil {

    val DATE_PATTERN = "yyyy-MM-dd"
    var DATE_PATTERN_SS = "yyyy-MM-dd HH:mm:ss"
    var DATE_PATTERN_MM = "yyyy-MM-dd HH:mm"

    /**
     * 获取现在时刻
     */
    val now: Date
        get() = Date(Date().time)
    /**
     * 获取现在时刻
     */
    val nows: Date
        get() = formatDate(DATE_PATTERN, now)



    /**
     * Date to Strin
     */
    @SuppressLint("SimpleDateFormat")
    fun formatDate(date: Date?, formatStyle: String): String {
        return if (date != null) {
            val sdf = SimpleDateFormat(formatStyle)
            sdf.format(date)
        } else {
            ""
        }

    }
    /**
     * Date to Strin
     */
    @SuppressLint("SimpleDateFormat")
    fun formatDate(date: Long, formatStyle: String): String {
        val sdf = SimpleDateFormat(formatStyle)
        return sdf.format(Date(date))

    }

    fun formatDate(formatStyle: String, formatStr: String): Date {
        val format = SimpleDateFormat(formatStyle, Locale.CHINA)
        return try {
            val date = Date()
            date.time = format.parse(formatStr).time
            date
        } catch (e: Exception) {
            println(e.message)
            nows
        }

    }

    /**
     * Date to Date
     */
    @SuppressLint("SimpleDateFormat")
    fun formatDate(formatStyle: String, date: Date?): Date {
        if (date != null) {
            val sdf = SimpleDateFormat(formatStyle)
            val formatDate = sdf.format(date)
            try {
                return sdf.parse(formatDate)
            } catch (e: ParseException) {
                e.printStackTrace()
                return Date()
            }

        } else {
            return Date()
        }
    }

    /**
     * 将时间戳转换为时间
     */
    fun stampToDate(s: String): Date {
        val lt = s.toLong()
        return Date(lt)
    }

    /**
     * 获得指定时间的日期
     */
    fun getCustomTime(dateStr: String):Date{
        return formatDate(DATE_PATTERN,dateStr)
    }

}
