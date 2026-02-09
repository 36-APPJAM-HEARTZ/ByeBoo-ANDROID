package com.byeboo.app.core.util

import com.byeboo.app.core.state.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

inline fun <T> MutableStateFlow<UiState<T>>.updateSuccess(
    crossinline onUpdate: (T) -> T
) {
    update { uiState ->
        if (uiState is UiState.Success) {
            uiState.copy(data = onUpdate(uiState.data))
        } else {
            uiState
        }
    }
}