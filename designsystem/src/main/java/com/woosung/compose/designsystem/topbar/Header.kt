package com.woosung.compose.designsystem.topbar

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.core_designsystem.R
import com.woosung.compose.common.ext.pxToDp

@Composable
fun GoodsHeader(
    modifier: Modifier = Modifier,
    title: String,
    iconUrl: String? = null,
    linkUrl: String? = null,
) {
    val context = LocalContext

    var textSize by remember { mutableStateOf(0) }
    Surface(color = Color.White) {
        Row(
            modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 10.dp)
                .onSizeChanged {
                    textSize = it.width
                },
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.width(textSize.pxToDp() / 2),
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color.Black,
            )
            Spacer(modifier = modifier.width(5.dp))
            if (!iconUrl.isNullOrBlank()) {
                AsyncImage(
                    model = iconUrl,
                    placeholder = painterResource(id = R.drawable.ic_access_time_24),
                    contentDescription = "",
                )
            }
            Spacer(modifier = modifier.weight(1f))
            if (!linkUrl.isNullOrBlank()) {
                Text(
                    text = "전체",
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
fun TopBarPreview() {
    GoodsHeader(
        title = "테스트",
        iconUrl = "https://image.msscdn.net/icons/mobile/clock.png",
        linkUrl = "https://www.musinsa.com/brands/discoveryexpedition?category3DepthCodes=&category2DepthCodes=&category1DepthCode=018&colorCodes=&startPrice=&endPrice=&exclusiveYn=&includeSoldOut=&saleGoods=&timeSale=&includeKeywords=&sortCode=discount_rate&tags=&page=1&size=90&listViewType=small&campaignCode=&groupSale=&outletGoods=false&boutiqueGoods=",
    )
}
