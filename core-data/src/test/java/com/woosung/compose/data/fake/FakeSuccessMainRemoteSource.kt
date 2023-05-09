package com.woosung.compose.data.fake

import com.woosung.compose.network.model.ContentsResponse
import com.woosung.compose.network.model.FooterResponse
import com.woosung.compose.network.model.GetGoodsResponse
import com.woosung.compose.network.model.HeaderResponse
import com.woosung.compose.network.model.StyleResponse
import com.woosung.compose.network.remote.MainRemoteSource

class FakeSuccessMainRemoteSource : MainRemoteSource {
    override suspend fun getGoods(): List<GetGoodsResponse> {
        val fakeList = listOf<GetGoodsResponse>(
            GetGoodsResponse(
                contents = ContentsResponse(
                    type = "BANNER",
                    banners = listOf(),
                    goods = listOf(),
                    styles = listOf<StyleResponse>(
                        StyleResponse(
                            linkURL = "테스트 URL",
                            thumbnailURL = "테스트 섬네일",
                        ),
                    ),
                ),
                header = HeaderResponse(
                    title = "테스트입니다.",
                ),
                footer = FooterResponse(
                    title = "Footer테스트",
                    type = "테스뚜",
                ),
            ),
        )
        return fakeList
    }
}
