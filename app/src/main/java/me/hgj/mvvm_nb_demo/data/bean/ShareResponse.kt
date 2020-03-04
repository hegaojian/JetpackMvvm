package me.hgj.mvvm_nb_demo.data.bean

import me.hgj.mvvm_nb_demo.data.ApiPagerResponse
import java.io.Serializable

data class ShareResponse(var coinInfo: CoinInfo,
                         var shareArticles: ApiPagerResponse<ArrayList<AriticleResponse>>
):Serializable