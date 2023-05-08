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

package com.woosung.compose.data

import com.woosung.compose.core.data.MainRepositoryImp
import com.woosung.compose.core.testing.CoroutinesTestExtension
import com.woosung.compose.data.fake.FakeSuccessMainRemoteSource
import com.woosung.domain.repository.MainRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(CoroutinesTestExtension::class)
class MainRepositoryTest {

    private lateinit var mainRepository: MainRepository

    @Nested
    @DisplayName("상품 리스트를 정삭적으로 호출되었을때")
    inner class MainSuccess {
        @BeforeEach
        fun initService() {
            mainRepository = MainRepositoryImp(FakeSuccessMainRemoteSource())
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @org.junit.jupiter.api.Test
        @DisplayName("fakegoodsList를 domain 모델로 매핑하여 정상적으로 반환")
        fun verifyGetGoodsList() = runTest {
            var currentGoodsList = mainRepository.getGoodsList().first()
            Assertions.assertEquals(currentGoodsList[0].header?.title, "테스트입니다.")
            Assertions.assertEquals(
                currentGoodsList[0].contents?.styles?.get(0)?.thumbnailURL,
                "테스트 섬네일",
            )
            Assertions.assertEquals(
                currentGoodsList[0].contents?.styles?.get(0)?.linkURL,
                "테스트 URL",
            )
        }
    }
}
