package com.woosung.compose.core.data.mapper

import com.woosung.compose.network.model.BannerResponse
import com.woosung.compose.network.model.ContentsResponse
import com.woosung.compose.network.model.FooterResponse
import com.woosung.compose.network.model.GetGoodsResponse
import com.woosung.compose.network.model.GoodResponse
import com.woosung.compose.network.model.HeaderResponse
import com.woosung.compose.network.model.StyleResponse
import com.woosung.domain.model.Banner
import com.woosung.domain.model.Content
import com.woosung.domain.model.ContentType
import com.woosung.domain.model.Footer
import com.woosung.domain.model.Good
import com.woosung.domain.model.Goods
import com.woosung.domain.model.Header
import com.woosung.domain.model.Style

fun GetGoodsResponse.toDomainModel() = Goods(
    contents = this.contents.toDomainModel(),
    header = this.header?.toDomainModel(),
    footer = this.footer?.toDomainModel(),
)

fun ContentsResponse.toDomainModel() = Content(
    type = ContentType.valueOf(this.type),
    banners = this.banners?.map { it.toDomainModel() },
    goods = this.goods?.map { it.toDomainModel() },
    styles = this.styles?.map { it.toDomainModel() },
)

fun BannerResponse.toDomainModel() = Banner(
    linkURL = this.linkURL,
    thumbnailURL = this.thumbnailURL,
    title = this.title,
    description = this.description,
    keyword = this.keyword,
)

fun GoodResponse.toDomainModel() = Good(
    linkURL = this.linkURL,
    thumbnailURL = this.thumbnailURL,
    brandName = this.brandName,
    price = this.price,
    saleRate = this.saleRate,
    hasCoupon = this.hasCoupon,
)

fun StyleResponse.toDomainModel() = Style(
    linkURL,
    thumbnailURL,
)

fun HeaderResponse.toDomainModel() = Header(
    title,
    iconURL,
    linkURL,
)

fun FooterResponse.toDomainModel() = Footer(
    type,
    title,
    iconURL,
)
