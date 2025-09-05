package com.byeboo.app.presentation.quest.complete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestCompletedViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(QuestCompletedState())
    val uiState: StateFlow<QuestCompletedState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<QuestCompletedSideEffect>()
    val sideEffect: SharedFlow<QuestCompletedSideEffect> = _sideEffect

    fun onQuestClick(questId: Long) {
        viewModelScope.launch {
            _sideEffect.emit(QuestCompletedSideEffect.NavigateToQuestReview(questId))
        }
    }
}
