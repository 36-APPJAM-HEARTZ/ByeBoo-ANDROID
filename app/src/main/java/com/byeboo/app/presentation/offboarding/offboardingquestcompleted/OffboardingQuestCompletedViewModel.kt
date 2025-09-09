package com.byeboo.app.presentation.offboarding.offboardingquestcompleted

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.domain.repository.auth.UserRepository
import com.byeboo.app.domain.repository.offboarding.OffboardingQuestCompletedRepository
import com.byeboo.app.presentation.offboarding.util.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OffboardingQuestCompletedViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val offboardingQuestCompletedRepository: OffboardingQuestCompletedRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(QuestCompletedState())
    val uiState: StateFlow<QuestCompletedState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<QuestCompletedSideEffect>()
    val sideEffect: SharedFlow<QuestCompletedSideEffect> = _sideEffect

    init {
        viewModelScope.launch {
            userRepository.getNickname().collect { nickname ->
                _uiState.update {
                    it.copy(userName = nickname)
                }
            }
        }
    }

    fun setJourney(journey: QuestType) {
        _uiState.update { it.copy(journeyType = journey) }
        loadQuests(journey)
    }

    private fun loadQuests(journey: QuestType) {
        viewModelScope.launch {
            val userName = uiState.value.userName
            val result = offboardingQuestCompletedRepository.getCompletedQuest(journey)

            result.onSuccess { detail ->
                _uiState.update { detail.toUiState(journey = journey, nickname = userName) }
            }.onFailure {
                viewModelScope.launch {
                    _sideEffect.emit(QuestCompletedSideEffect.ShowSnackBar("서버에 연결할 수 없습니다. 잠시 후 시도해 주세요."))
                }
            }
        }
    }

    fun onCancelClicked() {
        viewModelScope.launch {
            _sideEffect.emit(QuestCompletedSideEffect.NavigateUp)
        }
    }

    fun onQuestClicked(questId: Long) {
        viewModelScope.launch {
            _sideEffect.emit(QuestCompletedSideEffect.NavigateToQuestReview(questId))
        }
    }
}