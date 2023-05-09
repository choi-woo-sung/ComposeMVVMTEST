package com.woosung.compose.feature.main.ui.model

import com.woosung.compose.common.ext.addCommasToThousands
import com.woosung.domain.model.Banner
import com.woosung.domain.model.ContentType
import com.woosung.domain.model.Footer
import com.woosung.domain.model.Good
import com.woosung.domain.model.Goods
import com.woosung.domain.model.Header
import com.woosung.domain.model.Style
import java.lang.Integer.min

sealed interface GoodsUI {
    data class Style(
        val header: HeaderUi,
        val styleUi: List<StyleUi>,
        val footerUi: FooterUi,
        val extraList: List<List<StyleUi>> = listOf(),
    ) : GoodsUI {
        val isMore: Boolean = extraList.isNotEmpty()
    }

    class Banner(val bannerUi: List<BannerUi>) : GoodsUI
    data class Scroll(val header: HeaderUi, val goodUi: List<GoodUi>, val footerUi: FooterUi) : GoodsUI
    data class Grid(
        val header: HeaderUi,
        val goodUi: List<GoodUi>,
        val footerUi: FooterUi,
        val extraList: List<List<GoodUi>> = listOf(),
    ) : GoodsUI {
        val isMore: Boolean = extraList.isNotEmpty()
    }
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
    val price: String,
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
    price = price.addCommasToThousands(),
    saleRate = saleRate,
    hasCoupon = hasCoupon,

)

fun Style.toUiModel() = StyleUi(
    linkURL = linkURL,
    thumbnailURL = thumbnailURL,
)

fun Footer.toUiModel() = FooterUi(
    type = type,
    title = title,
    iconURL = iconURL,

)

fun Banner.toUiModel() = BannerUi(
    linkURL = linkURL,
    thumbnailURL = thumbnailURL,
    title = title,
    description = description,
    keyword = keyword,

)

fun List<Goods>.toUiModel() = this.map { goods ->
    when (goods.contents.type) {
        ContentType.BANNER -> GoodsUI.Banner(
            goods.contents.banners?.map { it.toUiModel() }!!,
        )

        ContentType.GRID -> {
            val item =
                goods.contents.goods!!.map { it.toUiModel() }.chunkItems(6, 3)
            GoodsUI.Grid(
                header = goods.header!!.toUiModel(),
                goodUi = item[0],
                footerUi = goods.footer!!.toUiModel(),
                extraList = item.drop(1),
            )
        }

        ContentType.SCROLL -> GoodsUI.Scroll(
            header = goods.header!!.toUiModel(),
            goodUi = goods.contents.goods!!.map { it.toUiModel() },
            footerUi = goods.footer!!.toUiModel(),
        )

        ContentType.STYLE -> {
            val item = goods.contents.styles!!.map { it.toUiModel() }.chunkItems(6, 3)
            GoodsUI.Style(
                header = goods.header!!.toUiModel(),
                styleUi = item[0],
                footerUi = goods.footer!!.toUiModel(),
                extraList = item.drop(1),
            )
        }
    }
}

/**
 * 요구사항에 맞추기위해 청크를 하는 함수
 *
 * @param items
 * @param firstChunkSize
 * @param otherChunkSize
 * @return
 */
fun <T> List<T>.chunkItems(firstChunkSize: Int, otherChunkSize: Int): List<List<T>> {
    val result = mutableListOf<List<T>>()
    var index = 0

    if (this.isNotEmpty()) {
        result.add(this.subList(index, min(index + firstChunkSize, this.size)))
        index += firstChunkSize

        while (index < this.size) {
            result.add(this.subList(index, min(index + otherChunkSize, this.size)))
            index += otherChunkSize
        }
    }

    return result
}
