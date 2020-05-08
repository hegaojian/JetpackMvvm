package me.hgj.jetpackmvvm.demo.data.model.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 收藏的网址类
 * @Author:         hegaojian
 * @CreateDate:     2019/8/31 10:36
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class CollectUrlResponse(var icon: String,
                              var id: Int,
                              var link: String,
                              var name: String,
                              var order: Int,
                              var userId: Int,
                              var visible: Int) : Parcelable

