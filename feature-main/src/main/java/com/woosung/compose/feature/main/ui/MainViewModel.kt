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
import com.ddd.pollpoll.core.result.Result
import com.ddd.pollpoll.core.result.asResult
import com.woosung.compose.feature.main.ui.MainUiState.Loading
import com.woosung.domain.model.Goods
import com.woosung.domain.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository,
) : ViewModel() {

    val uiState = mainRepository.getGoodsList().asResult().map {
        when (it) {
            is Result.Success -> MainUiState.Success(it.data)

            is Result.Error -> MainUiState.Error(it.exception ?: Exception("알수없는 에러"))

            Result.Loading -> Loading
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        Loading,
    )
}

sealed interface MainUiState {
    object Loading : MainUiState
    data class Success(val data: List<Goods>) : MainUiState
    data class Error(val throwable: Throwable) : MainUiState
}
