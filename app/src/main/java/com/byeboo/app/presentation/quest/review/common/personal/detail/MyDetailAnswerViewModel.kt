package com.byeboo.app.presentation.quest.review.common.personal.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.presentation.quest.component.type.MyPostOption
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class MyDetailAnswerViewModel
    @Inject
    constructor() : ViewModel() {
        private val _uiState = MutableStateFlow(MyDetailAnswerState())
        val uiState: StateFlow<MyDetailAnswerState> = _uiState.asStateFlow()

        fun onBackClicked() {
            // TODO: 이동로직 구현
        }

        fun onClickMoreOptions() {
            _uiState.update { it.copy(showBottomSheet = true) }
        }

        fun onDismissBottomSheet() {
            _uiState.update { it.copy(showBottomSheet = false) }
        }

        fun onOptionClicked(option: MyPostOption) {
            onDismissBottomSheet()

            viewModelScope.launch {
                when (option) {
                    MyPostOption.EDIT -> { // TODO 수정 화면 이동
                    }

                    MyPostOption.DELETE -> { // TODO 삭제 모달 띄우기
                    }
                }
            }
        }
    }
