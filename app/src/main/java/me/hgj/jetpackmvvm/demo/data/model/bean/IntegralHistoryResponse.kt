package me.hgj.jetpackmvvm.demo.data.model.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 积分记录
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class IntegralHistoryResponse(
        var coinCount: Int,
        var date: Long,
        var desc: String,
        var id: Int,
        var type: Int,
        var reason: String,
        var userId: Int,
        var userName: String) : Parcelable


