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

import app.cash.turbine.test
import com.woosung.compose.core.testing.CoroutinesTestExtension
import com.woosung.compose.feature.main.ui.model.GoodsUI
import com.woosung.compose.feature.main.ui.model.toUiModel
import com.woosung.domain.model.Content
import com.woosung.domain.model.ContentType
import com.woosung.domain.model.Footer
import com.woosung.domain.model.Good
import com.woosung.domain.model.Goods
import com.woosung.domain.model.Header
import com.woosung.domain.model.Style
import com.woosung.domain.repository.MainRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.extension.ExtendWith

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@ExtendWith(CoroutinesTestExtension::class)
class MainViewModelTest {

    private lateinit var mainViewModel: MainViewModel
    val fakeRepository = FakeMainRepository()

    @Nested
    @DisplayName("상품 리스트를 정상적으로 호출되었을때")
    inner class MainSuccess {
        @BeforeEach
        fun initService() {
            mainViewModel = MainViewModel(fakeRepository)
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @org.junit.jupiter.api.Test
        @DisplayName("상품 값을 정상적으로 가져온다.")
        fun verifyGetGoodsList() = runTest {
            Assertions.assertEquals(mainViewModel.uiState.value, MainUiState.Loading)
            mainViewModel.fetchGoods()
            mainViewModel.uiState.test {
                val item = this.awaitItem()
                Assertions.assertTrue(item is MainUiState.Success)
                Assertions.assertEquals(item, MainUiState.Success(fakeGoodsList.toUiModel()))

                if (item is MainUiState.Success) {
                    Assertions.assertTrue(item.data[0] is GoodsUI.Style)
                    Assertions.assertTrue(item.data[1] is GoodsUI.Grid)
                }
            }
        }
    }
}

val fakeGoodsList = listOf<Goods>(
    Goods(
        contents = Content(
            type = ContentType.STYLE,
            banners = listOf(),
            goods = listOf(),
            styles = listOf(
                Style(
                    "테스트 URL",
                    thumbnailURL = "썸네일 URL",
                ),
            ),
        ),
        header = Header("테스트"),
        footer = Footer("test", "test"),
    ),

    Goods(
        contents = Content(
            type = ContentType.GRID,
            banners = listOf(),
            goods = listOf(
                Good(
                    linkURL = "테스트",
                    thumbnailURL = "테스트",
                    brandName = "호호",
                    price = 1000,
                    saleRate = 30,
                    hasCoupon = false,
                ),
            ),
            styles = listOf(),
        ),
        header = Header("테스트"),
        footer = Footer("test", "test"),
    ),
)

class FakeMainRepository : MainRepository {
    override fun getGoodsList(): Flow<List<Goods>> = flow {
        emit(fakeGoodsList)
    }
}
