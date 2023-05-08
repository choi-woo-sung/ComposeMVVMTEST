package com.woosung.compose.network.api

import com.woosung.compose.network.ApiAbstract
import com.woosung.compose.network.handle.executeHandle
import com.woosung.compose.network.model.HeaderResponse
import com.woosung.compose.network.retrofit.MusinsaAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class MusinsaApiTest : ApiAbstract<MusinsaAPI>() {
    private lateinit var service: MusinsaAPI

    @BeforeEach
    fun initService() {
        service = createService(MusinsaAPI::class.java)
    }

    @Nested
    @DisplayName("MusinsaAPI  Goods 데이터가 200 떨어졌을때")
    inner class LoginDataSuccess {
        @BeforeEach
        fun setup() {
            enqueueResponse("/goods/goods_response_success.json")
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        @Test
        @DisplayName("데이터 파싱과 응답코드가 정상적으로 반환")
        fun verifyGetGoods() = runTest {
            val response = service.getGoods()
            Assertions.assertTrue(response.code() == 200)
            val parsingData = response.executeHandle()

            Assertions.assertEquals(
                parsingData[1].header,
                HeaderResponse(
                    title = "클리어런스",
                ),
            )
        }
    }
}
