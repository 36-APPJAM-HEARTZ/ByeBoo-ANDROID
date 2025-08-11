package com.byeboo.app.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.domain.repository.auth.UserRepository
import com.byeboo.app.domain.repository.quest.QuestStateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val questStateRepository: QuestStateRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    private val _sideEffect = MutableSharedFlow<HomeSideEffect>()
    val sideEffect = _sideEffect.asSharedFlow()

    init {
        viewModelScope.launch {

            val nickname = userRepository.getNickname().firstOrNull()

            val isStarted = questStateRepository.isQuestStarted()
            val journey = questStateRepository.getUserJourney() ?: "감정 직면"

            val seenAboutHelp = userRepository.hasSeenAboutHelp()

            var currentStep: Int? = null
            if (isStarted) {
                questStateRepository.getQuestCount()
                    .onSuccess { model ->
                        currentStep = model.count
                    }
            }

            _uiState.update {
                it.copy(
                    nickname = nickname,
                    isQuestStarted = isStarted,
                    journey = journey,
                    currentStep = currentStep ?: 0,
                    totalSteps = 30,
                    hasSeenAboutHelp = seenAboutHelp
                )
            }
        }
    }

    fun onClickQuest() {
        viewModelScope.launch {
            _sideEffect.emit(HomeSideEffect.NavigateToQuest)
        }
    }

    fun onClickQuestStart() {
        viewModelScope.launch {
            _sideEffect.emit(HomeSideEffect.NavigateToQuestStart)
        }
    }

    fun onHelpIconClicked() {
        viewModelScope.launch {
            userRepository.setHasSeenAboutHelp(true)
            _uiState.update { it.copy(hasSeenAboutHelp = true) }
        }
    }
}