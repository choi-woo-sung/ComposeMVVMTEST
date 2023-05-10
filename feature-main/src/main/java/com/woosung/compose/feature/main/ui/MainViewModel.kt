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

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddd.pollpoll.core.result.Result
import com.ddd.pollpoll.core.result.asResult
import com.woosung.compose.feature.main.ui.MainUiState.Loading
import com.woosung.compose.feature.main.ui.model.GoodsUI
import com.woosung.compose.feature.main.ui.model.toUiModel
import com.woosung.domain.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
) : ViewModel() {
    private val events = Channel<MainEvent>()

    init {
        fetchGoods()
    }

    val uiState = events.receiveAsFlow().runningFold(MainUiState.Loading, ::reduceState)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), Loading)

    private fun reduceState(current: MainUiState, event: MainEvent): MainUiState {
        return when (event) {
            MainEvent.Loading -> {
                Loading
            }

            is MainEvent.Loaded -> MainUiState.Success(event.data)

            is MainEvent.LoadMore -> {
                when (current) {
                    is MainUiState.Success -> updateList(current, event.index)
                    else -> current
                }
            }

            is MainEvent.Recommend -> {
                when (current) {
                    is MainUiState.Success -> shuffleList(current, event.index)
                    else -> current
                }
            }

            is MainEvent.Error -> TODO()
        }
    }

    fun fetchGoods() = viewModelScope.launch {
        mainRepository.getGoodsList().asResult().collect {
            when (it) {
                is Result.Success -> events.send(MainEvent.Loaded(data = it.data.toUiModel()))
                is Result.Error -> events.send(MainEvent.Error(error = it.exception))
                Result.Loading -> events.send(MainEvent.Loading)
            }
        }
    }

    fun loadMore(index: Int) = viewModelScope.launch {
        events.send(MainEvent.LoadMore(index))
    }

    fun recommendNewItem(index: Int) = viewModelScope.launch {
        events.send(MainEvent.Recommend(index))
    }

    private fun updateList(current: MainUiState.Success, index: Int): MainUiState.Success {
        val goodsUI = current.data[index]
        val updatedGoodsUI = when (goodsUI) {
            is GoodsUI.Grid -> goodsUI.updateGridItems()
            is GoodsUI.Style -> goodsUI.updateStyleItems()
            is GoodsUI.Scroll -> goodsUI
            is GoodsUI.Banner -> goodsUI
        }
        return current.copy(
            data = current.data.toMutableList().apply { set(index, updatedGoodsUI) },
        )
    }

    private fun GoodsUI.Grid.updateGridItems(): GoodsUI.Grid {
        val newGoodsUIList = goodUi.toMutableList().apply { addAll(extraList[0]) }
        return copy(
            goodUi = newGoodsUIList.toImmutableList(),
            extraList = extraList.drop(1).toImmutableList(),
        )
    }

    private fun GoodsUI.Style.updateStyleItems(): GoodsUI.Style {
        val newStyleUIList = styleUi.toMutableList().apply { addAll(extraList[0]) }
        return copy(
            styleUi = newStyleUIList.toImmutableList(),
            extraList = extraList.drop(1).toImmutableList(),
        )
    }

    private fun shuffleList(current: MainUiState.Success, index: Int): MainUiState.Success {
        val shuffledGoodsUI = when (val goodsUI = current.data[index]) {
            is GoodsUI.Scroll -> goodsUI.shuffleItems()
            else -> goodsUI
        }

        return current.copy(
            data = current.data.toMutableList().apply { set(index, shuffledGoodsUI) },
        )
    }

    private fun GoodsUI.Scroll.shuffleItems(): GoodsUI.Scroll {
        return copy(goodUi = goodUi.shuffled().toImmutableList())
    }
}

@Stable
sealed interface MainUiState {
    object Loading : MainUiState
    data class Success(val data: List<GoodsUI>) : MainUiState
    data class Error(val throwable: Throwable) : MainUiState
}

@Stable
sealed interface MainEvent {
    object Loading : MainEvent
    class Error(val error: Throwable?) : MainEvent

    class LoadMore(val index: Int) : MainEvent
    class Recommend(val index: Int) : MainEvent
    class Loaded(val data: List<GoodsUI>) : MainEvent
}
