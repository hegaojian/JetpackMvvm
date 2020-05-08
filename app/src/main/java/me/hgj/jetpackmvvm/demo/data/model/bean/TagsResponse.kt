package me.hgj.jetpackmvvm.demo.data.model.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
/**
 * 文章的标签
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class TagsResponse(var name:String, var url:String): Parcelable
