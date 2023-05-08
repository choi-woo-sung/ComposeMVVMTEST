package com.woosung.compose.core.data

import com.woosung.compose.core.data.mapper.toDomainModel
import com.woosung.compose.network.remote.MainRemoteSource
import com.woosung.domain.model.Goods
import com.woosung.domain.repository.MainRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MainRepositoryImp @Inject constructor(
    private val mainRemoteSource: MainRemoteSource,
) : MainRepository {

    override fun getGoodsList(): Flow<List<Goods>> = flow {
        val result = mainRemoteSource.getGoods().map { it.toDomainModel() }
        emit(result)
    }
}
