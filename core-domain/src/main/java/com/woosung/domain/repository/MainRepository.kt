package com.woosung.domain.repository

import com.woosung.domain.model.Goods
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    fun getGoodsList(): Flow<List<Goods>>
}
