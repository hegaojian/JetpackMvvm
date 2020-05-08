package me.hgj.jetpackmvvm.demo.data.model.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class ShareResponse(
    var coinInfoResponse: CoinInfoResponse,
    var shareArticles: ApiPagerResponse<ArrayList<AriticleResponse>>
) : Parcelable