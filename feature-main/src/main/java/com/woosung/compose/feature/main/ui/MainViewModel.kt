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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woosung.compose.feature.main.ui.MainUiState.Loading
import com.woosung.compose.feature.main.ui.model.GoodsUI
import com.woosung.domain.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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

    val uiState = events.receiveAsFlow().runningFold(MainUiState.Loading, ::reduceState)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), Loading)

    private fun reduceState(current: MainUiState, event: MainEvent): MainUiState {
        return when (event) {
            MainEvent.Loading -> {
                Loading
            }

            is MainEvent.Loaded -> {
                MainUiState.Success(event.data)
            }

            is MainEvent.LoadMore -> {
                when (current) {
                    is MainUiState.Success -> {
                        val goodsUI = current.data[event.index]
                        when (goodsUI) {
                            is GoodsUI.Grid -> {
                                val newGoodsUIList = goodsUI.goodUi.toMutableList()
                                    .apply { addAll(goodsUI.extraList[0]) }
                                val newExtraList = goodsUI.extraList.drop(0)
                                val newGoodUI = goodsUI.copy(
                                    goodUi = newGoodsUIList,
                                    extraList = newExtraList,
                                )

                                current.copy(
                                    current.data.toMutableList()
                                        .apply { set(event.index, newGoodUI) },
                                )
                            }

                            is GoodsUI.Style -> {
                                val newStyleUIList = goodsUI.styleUi.toMutableList()
                                    .apply { addAll(goodsUI.extraList[0]) }
                                val newExtraList = goodsUI.extraList.drop(0)
                                val newStyleUI = goodsUI.copy(
                                    styleUi = newStyleUIList,
                                    extraList = newExtraList,
                                )

                                current.copy(
                                    current.data.toMutableList()
                                        .apply { set(event.index, newStyleUI) },
                                )
                            }

                            else -> current
                        }
                    }

                    else -> current
                }
            }

            is MainEvent.Recommand -> current
        }
    }

    fun fetchUser() = viewModelScope.launch {
        val users = mainRepository.getGoodsList()
        events.send(MainEvent.Loaded(users = users))
        _sideEffects.send("${users.size} user(s) loaded")
    }
}

sealed interface MainUiState {
    object Loading : MainUiState
    data class Success(val data: List<GoodsUI>) : MainUiState
    data class Error(val throwable: Throwable) : MainUiState
}

sealed interface MainEvent {
    object Loading : MainEvent
    class LoadMore(val index: Int) : MainEvent
    class Recommand(val index: Int) : MainEvent
    class Loaded(val data: List<GoodsUI>) : MainEvent
}
