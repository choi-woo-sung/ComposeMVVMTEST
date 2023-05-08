package com.woosung.compose.network.retrofit

import com.woosung.compose.network.handle.ApiResponse
import com.woosung.compose.network.model.GetGoodsResponse
import retrofit2.Response
import retrofit2.http.*

interface MusinsaAPI {

    @GET("/interview/list.json")
    suspend fun getGoods(): Response<ApiResponse<List<GetGoodsResponse>>>
}
