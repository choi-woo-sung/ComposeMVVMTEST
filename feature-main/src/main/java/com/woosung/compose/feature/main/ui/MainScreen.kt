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

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.woosung.compose.core.ui.MyApplicationTheme
import com.woosung.compose.designsystem.button.FooterButton
import com.woosung.compose.designsystem.grid.GoodGridContent
import com.woosung.compose.designsystem.grid.gridItems
import com.woosung.compose.designsystem.grid.styleGridItems
import com.woosung.compose.designsystem.topbar.GoodsHeader
import com.woosung.compose.feature.main.ui.model.BannerUi
import com.woosung.compose.feature.main.ui.model.FooterUi
import com.woosung.compose.feature.main.ui.model.GoodUi
import com.woosung.compose.feature.main.ui.model.GoodsUI
import com.woosung.compose.feature.main.ui.model.HeaderUi
import com.woosung.compose.feature.main.ui.model.StyleUi
import kotlinx.coroutines.delay

@Composable
fun MainScreen(modifier: Modifier = Modifier, viewModel: MainViewModel = hiltViewModel()) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle().value
    MainScreen(
        uiState,
        onLoadMoreClicked = viewModel::loadMore,
        onRecommendClicked = viewModel::recommendNewItem,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MainScreen(
    uiState: MainUiState,
    onLoadMoreClicked: (Int) -> Unit = {},
    onRecommendClicked: (Int) -> Unit = {},
) {
    val horizontalListScroll = rememberLazyListState()
    LazyColumn() {
        when (uiState) {
            is MainUiState.Error -> {}
            MainUiState.Loading -> {
                item {
                    Box(modifier = Modifier.fillParentMaxSize()) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }
                }
            }

            is MainUiState.Success -> {
                uiState.data.forEachIndexed { idx, goodUi ->
                    when (goodUi) {
                        is GoodsUI.Banner -> {
                            BannerScreen(
                                content = goodUi.bannerUi,
                            )
                        }

                        is GoodsUI.Grid -> {
                            GoodsGridScreen(
                                header = goodUi.header,
                                content = goodUi.goodUi,
                                footer = goodUi.footerUi,
                                onClick = { onLoadMoreClicked(idx) },
                                isMore = goodUi.isMore,

                            )
                        }

                        is GoodsUI.Scroll -> {
                            ScrollScreen(
                                header = goodUi.header,
                                content = goodUi.goodUi,
                                footer = goodUi.footerUi,
                                horizontalListScroll,
                                onClick = { onRecommendClicked(idx) },
                            )
                        }

                        is GoodsUI.Style -> {
                            StyleScreen(
                                header = goodUi.header,
                                content = goodUi.styleUi,
                                footer = goodUi.footerUi,
                                onClick = { onLoadMoreClicked(idx) },
                                isMore = goodUi.isMore,
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.BannerScreen(
    content: List<BannerUi>,
) {
    item {
        GoodParallaxHorizontalPager(banner = content)
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.ScrollScreen(
    header: HeaderUi,
    content: List<GoodUi>,
    footer: FooterUi,
    horizontalListScroll: LazyListState,
    onClick: () -> Unit,
) {
    stickyHeader {
        GoodsHeader(
            title = header.title,
            iconUrl = header.iconURL,
            linkUrl = header.linkURL,
        )
    }
    item {
        LazyRow(state = horizontalListScroll) {
            items(content) {
                GoodGridContent(
                    modifier = Modifier.width(150.dp),
                    title = it.brandName,
                    thumbnailURL = it.thumbnailURL,
                    price = it.price,
                    disCountPercent = it.saleRate.toString(),
                    hasCoupon = it.hasCoupon,
                )
            }
        }
    }
    item {
        FooterButton(
            onClick = onClick,
            text = footer.title,
            iconUrl = footer.iconURL,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.StyleScreen(
    header: HeaderUi,
    content: List<StyleUi>,
    footer: FooterUi,
    onClick: () -> Unit,
    isMore: Boolean = true,
) {
    stickyHeader {
        GoodsHeader(
            title = header.title,
            iconUrl = header.iconURL,
            linkUrl = header.linkURL,
        )
    }
    styleGridItems(content, 3) {
        AsyncImage(
            modifier = Modifier
                .padding(1.dp)
                .fillMaxSize(),
            model = it.thumbnailURL,
            contentScale = ContentScale.Crop,
            contentDescription = "",
        )
    }
    if (isMore) {
        item {
            FooterButton(
                onClick = onClick,
                text = footer.title,
                iconUrl = footer.iconURL,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
fun LazyListScope.GoodsGridScreen(
    header: HeaderUi,
    content: List<GoodUi>,
    footer: FooterUi,
    onClick: () -> Unit,
    isMore: Boolean = true,
) {
    stickyHeader {
        GoodsHeader(
            title = header.title,
            iconUrl = header.iconURL,
            linkUrl = header.linkURL,
        )
    }
    gridItems(content, 3) {
        GoodGridContent(
            modifier = Modifier.fillMaxSize(),
            title = it.brandName,
            thumbnailURL = it.thumbnailURL,
            price = it.price,
            disCountPercent = it.saleRate.toString(),
            hasCoupon = it.hasCoupon,
        )
    }
    if (isMore) {
        item {
            FooterButton(
                onClick = onClick,
                text = footer.title,
                iconUrl = footer.iconURL,
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GoodParallaxHorizontalPager(banner: List<BannerUi>) {
    val pagerState = rememberPagerState(0)
    LaunchedEffect(true) {
        while (pagerState.currentPage != banner.size - 1) {
            delay(3000)
            pagerState.animateScrollToPage(
                page = pagerState.settledPage + 1,
                animationSpec = tween(
                    durationMillis = 1000,
                ),
            )
        }
    }
    Box() {
        HorizontalPager(
            pageCount = banner.size,
            state = pagerState,
            beyondBoundsPageCount = 0,
        ) { page ->
            val bannerResult = banner[page]
            val pageOffset = pagerState.calculateCurrentOffsetForPage(page)
            Box() {
                val image = rememberAsyncImagePainter(model = bannerResult.thumbnailURL)
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeightIn(300.dp)
                        .graphicsLayer {
                            // todo pager에서는 Z Index를 제공하지 않기 떄문에 현재론 이방법으로 뒤로가기 적용불가
                            // alpha값을 줘서 자연스럽게 보이게 만듬.
                            translationX =
                                if (pagerState.settledPage == page) pageOffset * (size.width) else 0f
//                            alpha = if (pagerState.settledPage == page) 1 - pageOffset.absoluteValue else 1f
                        },
                    painter = image,
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                )
                Column(
                    Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        modifier = Modifier
                            .graphicsLayer {
                                translationX = pageOffset * (size.width * -2)
                            },
                        text = bannerResult.title,
                    )
                    Spacer(modifier = Modifier.height(50.dp))

                    Text(
                        modifier = Modifier
                            .graphicsLayer {
                                translationX = pageOffset / 3f * (size.width * -2)
                            },
                        text = bannerResult.description,
                    )
                }
            }
        }

        Surface(Modifier.align(Alignment.BottomEnd), color = Color.Black.copy(alpha = 0.5f)) {
            Text(
                modifier = Modifier.padding(10.dp),
                text = "${pagerState.currentPage + 1}/${banner.size}",
            )
        }
    }
}

// extension method for current page offset
@OptIn(ExperimentalFoundationApi::class)
fun PagerState.calculateCurrentOffsetForPage(page: Int): Float {
    return (currentPage - page) + currentPageOffsetFraction
}

@Preview(showBackground = true)
@Composable
private fun MainScreenLoadingPreview() {
    MyApplicationTheme {
        MainScreen(uiState = MainUiState.Loading)
    }
}
