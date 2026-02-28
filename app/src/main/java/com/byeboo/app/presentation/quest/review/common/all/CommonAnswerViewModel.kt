package com.byeboo.app.presentation.quest.review.common.all

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.presentation.quest.component.type.OtherPostOption
import com.byeboo.app.presentation.quest.model.CommonAnswerModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CommonAnswerViewModel
@Inject
constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(CommonAnswerState())
    val uiState: StateFlow<CommonAnswerState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<CommonAnswerSideEffect>()
    val sideEffect: SharedFlow<CommonAnswerSideEffect> = _sideEffect.asSharedFlow()

    init {
        loadCommonAnswer(1L)
    }

    // Todo : 더미데이터 변경
    private fun loadCommonAnswer(answerId: Long) {
        val dummyAnswer =
            CommonAnswerModel(
                answerId = answerId,
                writer = "장원영",
                profileIconRes = R.drawable.ic_profile_sadness,
                displayTime = "2026. 01. 30.",
                content = "헤어진 첫날 밤이었어요. 혼자 집에 있는데 갑자기 모든 게 현실로 다가왔고, 이제 정말 끝났다는 걸 깨달았을 때... 그때가 제일 힘들었던 것 같아요.",
            )

        _uiState.update { it.copy(answer = dummyAnswer) }
    }

    fun onClickMoreOptions() {
        _uiState.update { it.copy(showBottomSheet = true) }
    }

    fun onDismissBottomSheet() {
        _uiState.update { it.copy(showBottomSheet = false) }
    }

    fun onOptionClick(option: OtherPostOption) {
        onDismissBottomSheet()

        viewModelScope.launch {
            when (option) {
                OtherPostOption.BLOCK -> {
                    _sideEffect.emit(CommonAnswerSideEffect.NavigateToQuest)
                    _sideEffect.emit(
                        CommonAnswerSideEffect.ShowSnackBar(
                            snackBarType = CustomSnackBarType.SUCCESS("차단이 완료되었어요. 이에 해당 사용자의 글이 노출되지 않아요.")
                        ),
                    )
                }

                OtherPostOption.REPORT -> {
                    _sideEffect.emit(
                        CommonAnswerSideEffect.ShowSnackBar(
                            snackBarType = CustomSnackBarType.SUCCESS("신고가 접수되었어요. 처리 결과는 알림을 통해 알려드려요.")
                        ),
                    )
                }
            }
        }
    }
}
