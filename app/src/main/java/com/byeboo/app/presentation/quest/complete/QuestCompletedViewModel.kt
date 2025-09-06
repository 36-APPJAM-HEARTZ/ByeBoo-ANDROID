package com.byeboo.app.presentation.quest.complete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.domain.repository.auth.UserRepository
import com.byeboo.app.domain.repository.quest.QuestCompletedRepository
import com.byeboo.app.presentation.quest.util.toUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestCompletedViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val questCompletedRepository: QuestCompletedRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(QuestCompletedState())
    val uiState: StateFlow<QuestCompletedState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<QuestCompletedSideEffect>()
    val sideEffect: SharedFlow<QuestCompletedSideEffect> = _sideEffect

    fun loadQuests(journey: String){
        viewModelScope.launch {
            val nickname = userRepository.getNickname().firstOrNull() ?: "하츠핑"
            val result = questCompletedRepository.getCompletedQuest(journey)
            result.onSuccess { detail ->
                _uiState.value = detail.toUiState(journey = journey, nickname = nickname)
            }.onFailure {

            }
        }
    }

    fun onCancelClicked(){
        viewModelScope.launch {
            _sideEffect.emit(QuestCompletedSideEffect.NavigateToOffboardingCompletedJourney)
        }
    }

    fun onQuestClick(questId: Long) {
        viewModelScope.launch {
            _sideEffect.emit(QuestCompletedSideEffect.NavigateToQuestReview(questId))
        }
    }
}
