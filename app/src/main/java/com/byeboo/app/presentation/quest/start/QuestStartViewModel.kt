package com.byeboo.app.presentation.quest.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.domain.model.JourneyStatusType
import com.byeboo.app.domain.repository.NewJourneyRepository
import com.byeboo.app.domain.repository.auth.UserRepository
import com.byeboo.app.domain.repository.quest.QuestStateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class QuestStartViewModel @Inject constructor(
    private val questStateRepository: QuestStateRepository,
    private val userRepository: UserRepository,
    private val newJourneyRepository: NewJourneyRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(QuestStartState())
    val uiState: StateFlow<QuestStartState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<QuestStartSideEffect>()
    val sideEffect: SharedFlow<QuestStartSideEffect> = _sideEffect

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            userRepository.getNickname().collect { name ->
                _uiState.update { it.copy(nickname = name) }
            }
        }
        viewModelScope.launch {
            val journey = questStateRepository.getUserJourney() ?: "감정 직면"
            _uiState.update { it.copy(journeyName = journey) }
        }
    }

    fun onStartClicked(journey: QuestType?) {
        if (journey == null) {
            viewModelScope.launch {
                val result = questStateRepository.updateQuestStartState()
                if (result.isSuccess) {
                    questStateRepository.setQuestStarted(true)
                    questStateRepository.updateUserJourneyStatus(JourneyStatusType.IN_PROGRESS)
                    _sideEffect.emit(QuestStartSideEffect.NavigateToQuest)
                } else {
                    _sideEffect.emit(
                        QuestStartSideEffect.ShowSnackBar("서버에 연결할 수 없습니다. 잠시 후 시도해 주세요.")
                    )
                }
            }
        } else {
            postNewJourney(journey)
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _sideEffect.emit(QuestStartSideEffect.NavigateToHome)
        }
    }

    fun postNewJourney(journey: QuestType) {
        val journeyType = journey.journeyType
        val journeyName = journey.journeyName

        viewModelScope.launch {
            newJourneyRepository.postNewJourney(journeyType)
                .onSuccess {
                    questStateRepository.setQuestStarted(true)
                    questStateRepository.updateUserJourney(journeyName)
                    questStateRepository.updateUserJourneyStatus(JourneyStatusType.IN_PROGRESS)
                    _sideEffect.emit(QuestStartSideEffect.NavigateToQuest)
                }
                .onFailure { e ->
                    _sideEffect.emit(
                        QuestStartSideEffect.ShowSnackBar("서버에 연결할 수 없습니다. 잠시 후 시도해 주세요.")
                    )
                }
        }
    }
}
