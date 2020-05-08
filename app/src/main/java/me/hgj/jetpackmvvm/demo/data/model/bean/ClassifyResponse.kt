package me.hgj.jetpackmvvm.demo.data.model.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 项目分类
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class ClassifyResponse(var children: List<String> = listOf(),
                            var courseId: Int = 0,
                            var id: Int = 0,
                            var name: String = "",
                            var order: Int = 0,
                            var parentChapterId: Int = 0,
                            var userControlSetTop: Boolean = false,
                            var visible: Int = 0) : Parcelable
