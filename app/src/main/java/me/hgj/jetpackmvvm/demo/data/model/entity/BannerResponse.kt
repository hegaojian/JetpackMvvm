package me.hgj.jetpackmvvm.demo.data.model.entity

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 轮播图
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class BannerResponse(
    var title: String = "",
    var url: String = "",
    var desc: String = "",
    var id: String = "",
    var imagePath: String = "",
    var isVisible: Int = 0,
    var order: Int = 0,
    var type: Int = 0,
) : Parcelable


