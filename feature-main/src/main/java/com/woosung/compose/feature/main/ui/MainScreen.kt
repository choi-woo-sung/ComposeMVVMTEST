/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.woosung.compose.feature.main.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.woosung.compose.core.ui.MyApplicationTheme
import com.woosung.compose.designsystem.button.FooterButton
import com.woosung.compose.designsystem.grid.GoodGridContent
import com.woosung.compose.designsystem.grid.gridItems
import com.woosung.compose.designsystem.grid.styleGridItems
import com.woosung.compose.designsystem.topbar.GoodsHeader
import com.woosung.domain.model.Banner
import com.woosung.domain.model.ContentType
import com.woosung.domain.model.Footer
import com.woosung.domain.model.Good
import com.woosung.domain.model.Header

@Composable
fun MainScreen(modifier: Modifier = Modifier, viewModel: MainViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    MainScreen(uiState)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MainScreen(
    uiState: MainUiState,
) {
    val horizontalListScroll = rememberLazyListState()

    when (uiState) {
        is MainUiState.Error -> {}
        MainUiState.Loading -> {}
        is MainUiState.Success -> {
            LazyColumn() {
                val grid = uiState.data.filter { it.contents.type == ContentType.GRID }.first()
                val scroll = uiState.data.filter { it.contents.type == ContentType.SCROLL }.first()
                val banner = uiState.data.filter { it.contents.type == ContentType.BANNER }.first()
                BannerScreen(
                    header = banner.header,
                    content = banner.contents.banners,
                    footer = banner.footer,
                )
                GoodsGridScreen(
                    header = grid.header,
                    content = grid.contents.goods,
                    footer = grid.footer,
                )
                ScrollScreen(
                    header = scroll.header,
                    content = scroll.contents.goods,
                    footer = scroll.footer,
                    horizontalListScroll,
                )
                StyleScreen(
                    header = grid.header,
                    content = grid.contents.goods,
                    footer = grid.footer,
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.BannerScreen(
    header: Header?,
    content: List<Banner>?,
    footer: Footer?,
) {
    if (header != null) {
        stickyHeader {
            GoodsHeader(
                title = header.title,
                iconUrl = header.iconURL,
                linkUrl = header.linkURL,
            )
        }
    }
    if (content != null) {
        item {
            GoodParallaxHorizontalPager(banner = content)
        }
    }
    if (footer != null) {
        item {
            FooterButton(
                text = footer.title,
                iconUrl = footer.iconURL,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.ScrollScreen(
    header: Header?,
    content: List<Good>?,
    footer: Footer?,
    horizontalListScroll: LazyListState,
) {
    if (header != null) {
        stickyHeader {
            GoodsHeader(
                title = header.title,
                iconUrl = header.iconURL,
                linkUrl = header.linkURL,
            )
        }
    }
    if (content != null) {
        item {
            LazyRow(state = horizontalListScroll) {
                items(content) {
                    GoodGridContent(
                        title = it.brandName,
                        thumbnailURL = it.thumbnailURL,
                        price = it.price.toString(),
                        disCountPercent = it.saleRate.toString(),
                        hasCoupon = it.hasCoupon,
                    )
                }
            }
        }
    }
    if (footer != null) {
        item {
            FooterButton(
                text = footer.title,
                iconUrl = footer.iconURL,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.StyleScreen(
    header: Header?,
    content: List<Good>?,
    footer: Footer?,
) {
    if (header != null) {
        stickyHeader {
            GoodsHeader(
                title = header.title,
                iconUrl = header.iconURL,
                linkUrl = header.linkURL,
            )
        }
    }
    if (content != null) {
        styleGridItems(content, 3) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(1.dp),
                model = it.thumbnailURL,
                contentScale = ContentScale.Crop,
                contentDescription = "",
            )
        }
    }
    if (footer != null) {
        item {
            FooterButton(
                text = footer.title,
                iconUrl = footer.iconURL,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.GoodsGridScreen(header: Header?, content: List<Good>?, footer: Footer?) {
    if (header != null) {
        stickyHeader {
            GoodsHeader(
                title = header.title,
                iconUrl = header.iconURL,
                linkUrl = header.linkURL,
            )
        }
    }
    if (content != null) {
        gridItems(content, 3) {
            GoodGridContent(
                title = it.brandName,
                thumbnailURL = it.thumbnailURL,
                price = it.price.toString(),
                disCountPercent = it.saleRate.toString(),
                hasCoupon = it.hasCoupon,
            )
        }
    }
    if (footer != null) {
        item {
            FooterButton(
                text = footer.title,
                iconUrl = footer.iconURL,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GoodParallaxHorizontalPager(banner: List<Banner>) {
    val state = rememberPagerState(0)
    HorizontalPager(pageCount = banner.size, state = state) { page ->
        AsyncImage(
            model = banner[page].thumbnailURL,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().requiredHeightIn(300.dp),
        )
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    MyApplicationTheme {
        MainScreen()
    }
}
