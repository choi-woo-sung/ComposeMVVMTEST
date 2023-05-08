package com.woosung.domain.model

data class Goods(
    val contents: Content,
    val header: Header? = null,
    val footer: Footer? = null,
)

data class Content(
    val type: ContentType,
    val banners: List<Banner>? = null,
    val goods: List<Good>? = null,
    val styles: List<Style>? = null,
)

data class Banner(
    val linkURL: String,
    val thumbnailURL: String,
    val title: String,
    val description: String,
    val keyword: String,
)

data class Good(
    val linkURL: String,
    val thumbnailURL: String,
    val brandName: String,
    val price: Long,
    val saleRate: Long,
    val hasCoupon: Boolean,
)

data class Style(
    val linkURL: String,
    val thumbnailURL: String,
)

data class Footer(
    val type: String,
    val title: String,
    val iconURL: String? = null,
)

data class Header(
    val title: String,
    val iconURL: String? = null,
    val linkURL: String? = null,
)
