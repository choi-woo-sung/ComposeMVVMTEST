package com.woosung.compose.network.model

data class GetGoodsResponse(
    val contents: ContentsResponse,
    val header: HeaderResponse? = null,
    val footer: FooterResponse? = null,
)

data class ContentsResponse(
    val type: String,
    val banners: List<BannerResponse>? = null,
    val goods: List<GoodResponse>? = null,
    val styles: List<StyleResponse>? = null,
)

data class BannerResponse(
    val linkURL: String,
    val thumbnailURL: String,
    val title: String,
    val description: String,
    val keyword: String,
)

data class GoodResponse(
    val linkURL: String,
    val thumbnailURL: String,
    val brandName: String,
    val price: Long,
    val saleRate: Long,
    val hasCoupon: Boolean,
)

// enum class BrandName(val value: String) {
//    @SerialName("디스커버리 익스페디션")
//    디스커버리익스페디션("디스커버리 익스페디션"),
//
//    @SerialName("아스트랄 프로젝션")
//    아스트랄프로젝션("아스트랄 프로젝션"),
//
//    @SerialName("텐블레이드")
//    텐블레이드("텐블레이드"),
// }

data class StyleResponse(
    val linkURL: String,
    val thumbnailURL: String,
)

data class FooterResponse(
    val type: String,
    val title: String,
    val iconURL: String? = null,
)

data class HeaderResponse(
    val title: String,
    val iconURL: String? = null,
    val linkURL: String? = null,
)
