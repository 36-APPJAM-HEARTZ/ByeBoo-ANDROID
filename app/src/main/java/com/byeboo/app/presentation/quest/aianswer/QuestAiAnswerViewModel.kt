package com.byeboo.app.presentation.quest.aianswer

import androidx.lifecycle.ViewModel
import com.byeboo.app.core.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class QuestAiAnswerViewModel
    @Inject
    constructor() : ViewModel() {
        private val _uiState = MutableStateFlow<UiState<QuestAiAnswerState>>(UiState.Loading)
        val uiState: StateFlow<UiState<QuestAiAnswerState>> = _uiState.asStateFlow()

        init {
            loadQuestAiAnswer()
        }

        private fun loadQuestAiAnswer() {
            // TODO: 서버 연결
        }

        fun onCloseClicked() {
            // TODO: 나의 여정 퀘스트 메인 화면으로 이동
        }
    }
