package com.woosung.compose.network.remote

import com.woosung.compose.network.handle.executeHandle
import com.woosung.compose.network.model.GetGoodsResponse
import com.woosung.compose.network.retrofit.MusinsaAPI
import javax.inject.Inject

interface MainRemoteSource {
    suspend fun getGoods(): List<GetGoodsResponse>
}

class MainRemoteSourceImp @Inject constructor(
    private val musinsaAPI: MusinsaAPI
) : MainRemoteSource {
//    override suspend fun getCategories(): GetCategoriesResponse = pollAPI.getCategories().executeHandle()
    override suspend fun getGoods(): List<GetGoodsResponse> = musinsaAPI.getGoods().executeHandle()
}
