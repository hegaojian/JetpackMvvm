package me.hgj.jetpackmvvm.demo.data.model.entity

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * 文章
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class ArticleResponse(
    var apkLink: String,
    var author: String,//作者
    var chapterId: Int,
    var chapterName: String,
    var collect: Boolean,//是否收藏
    var courseId: Int,
    var desc: String,
    var envelopePic: String,
    var fresh: Boolean,
    var id: String,
    var link: String,
    var niceDate: String,
    var origin: String,
    var prefix: String,
    var projectLink: String,
    var publishTime: Long,
    var superChapterId: Int,
    var superChapterName: String,
    var shareUser: String,
    var tags: List<TagsResponse>,
    var title: String,
    var type: Int,
    var userId: String,
    var visible: Int,
    var zan: Int) : Parcelable
