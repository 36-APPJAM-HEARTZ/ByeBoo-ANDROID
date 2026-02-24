package com.byeboo.app.presentation.quest.review.common

import androidx.lifecycle.ViewModel
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.presentation.quest.model.CommonAnswerModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CommonAnswerViewModel @Inject constructor(

) : ViewModel() {
    private val _uiState = MutableStateFlow(CommonAnswerState())
    val uiState: StateFlow<CommonAnswerState> = _uiState.asStateFlow()

    init {
        loadCommonAnswer(1L)
    }

    // Todo : 더미데이터 변경
    private fun loadCommonAnswer(answerId: Long) {
        val dummyAnswer = CommonAnswerModel(
            answerId = answerId,
            writer = "장원영",
            profileIconRes = R.drawable.ic_profile_sadness,
            displayTime = "2026. 01. 30.",
            content = "헤어진 첫날 밤이었어요. 혼자 집에 있는데 갑자기 모든 게 현실로 다가왔고, 이제 정말 끝났다는 걸 깨달았을 때... 그때가 제일 힘들었던 것 같아요."
        )

        _uiState.update { it.copy(answer = dummyAnswer) }
    }
}