package me.hgj.jetpackmvvm.demo.data.model.entity

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 积分
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class IntegralResponse(
        var coinCount: String,//当前积分
        var rank: String,
        var userId: String,
        var username: String) : Parcelable


