package me.hgj.jetpackmvvm.demo.data.bean

import me.hgj.jetpackmvvm.demo.data.ApiPagerResponse
import java.io.Serializable

data class ShareResponse(var coinInfo: CoinInfo,
                         var shareArticles: ApiPagerResponse<ArrayList<AriticleResponse>>
):Serializable