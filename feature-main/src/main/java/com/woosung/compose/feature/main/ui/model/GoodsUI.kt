package com.woosung.compose.feature.main.ui.model

import com.woosung.domain.model.Banner
import com.woosung.domain.model.ContentType
import com.woosung.domain.model.Footer
import com.woosung.domain.model.Good
import com.woosung.domain.model.Goods
import com.woosung.domain.model.Header
import com.woosung.domain.model.Style

sealed interface GoodsUI {
    class Style(val header: HeaderUi, val styleUi: List<StyleUi>, val footerUi: FooterUi) : GoodsUI
    class Banner(val bannerUi: List<BannerUi>) : GoodsUI
    class Scroll(val header: HeaderUi, val goodUi: List<GoodUi>, val footerUi: FooterUi) : GoodsUI
    class Grid(val header: HeaderUi, val goodUi: List<GoodUi>, val footerUi: FooterUi) : GoodsUI
}

data class HeaderUi(
    val title: String,
    val iconURL: String? = null,
    val linkURL: String? = null,
)

data class BannerUi(
    val linkURL: String,
    val thumbnailURL: String,
    val title: String,
    val description: String,
    val keyword: String,
)

data class GoodUi(
    val linkURL: String,
    val thumbnailURL: String,
    val brandName: String,
    val price: Long,
    val saleRate: Long,
    val hasCoupon: Boolean,
)

data class FooterUi(
    val type: String,
    val title: String,
    val iconURL: String? = null,
)

data class StyleUi(
    val linkURL: String,
    val thumbnailURL: String,
)

fun Header.toUiModel() = HeaderUi(
    title = title,
    iconURL = iconURL,
    linkURL = linkURL,
)

fun Good.toUiModel() = GoodUi(
    linkURL = linkURL,
    thumbnailURL = thumbnailURL,
    brandName = brandName,
    price = price,
    saleRate = saleRate,
    hasCoupon = hasCoupon,

)

fun Style.toUiModel() = StyleUi(
    linkURL = linkURL,
    thumbnailURL = thumbnailURL,
)

fun Footer.toUiModel() = FooterUi(
    
)

fun Banner.toUiModel() = BannerUi(
    linkURL = linkURL,
    thumbnailURL = thumbnailURL,
    title = title,
    description = description,
    keyword = keyword,

)

fun List<Goods>.toUiModel() {
    this.map { goods ->
        when (goods.contents.type) {
            ContentType.BANNER -> GoodsUI.Banner(
                goods.contents.banners?.map { it.toUiModel() }!!,
            )

            ContentType.GRID -> GoodsUI.Grid(
                header = goods.header.toUiModel(),


            )

            ContentType.SCROLL -> TODO()
            ContentType.STYLE -> TODO()
        }
    }
}
