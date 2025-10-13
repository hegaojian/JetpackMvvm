package me.hgj.jetpackmvvm.demo.data.model.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class ShareResponse(
    var coinInfo: CoinInfoResponse,
    var shareArticles: @RawValue ApiPagerResponse<ArticleResponse>
) : Parcelable