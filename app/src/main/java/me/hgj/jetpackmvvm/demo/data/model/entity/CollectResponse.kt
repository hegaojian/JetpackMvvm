package me.hgj.jetpackmvvm.demo.data.model.entity

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class CollectResponse(var chapterId: Int,
                           var author: String,
                           var chapterName: String,
                           var courseId: Int,
                           var desc: String,
                           var envelopePic: String,
                           var id: String,
                           var link: String,
                           var niceDate: String,
                           var origin: String,
                           var originId: String,
                           var publishTime: Long,
                           var title: String,
                           var userId: Int,
                           var visible: Int,
                           var zan: Int):Parcelable