package com.byeboo.app.presentation.offboarding.offboardingcompletedguide

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.core.util.MixpanelUtil
import com.byeboo.app.domain.repository.auth.UserRepository
import com.byeboo.app.domain.repository.quest.QuestStateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OffboardingCompletedGuideViewModel
    @Inject
    constructor(
        private val userRepository: UserRepository,
        private val questStateRepository: QuestStateRepository,
        private val savedStateHandle: SavedStateHandle,
        private val mixpanelUtil: MixpanelUtil,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(OffboardingCompletedGuideState())
        val uiState: StateFlow<OffboardingCompletedGuideState> = _uiState.asStateFlow()

        private val _sideEffect = MutableSharedFlow<OffboardingCompletedGuideSideEffect>()
        val sideEffect: SharedFlow<OffboardingCompletedGuideSideEffect> = _sideEffect.asSharedFlow()

        companion object {
            private const val ANIMATION_PLAYED = "animation_played"
        }

        val isInitialAnimation: StateFlow<Boolean> =
            savedStateHandle.getStateFlow(
                ANIMATION_PLAYED,
                false,
            )

        init {
            loadInitialData()
        }

        private fun loadInitialData() {
            viewModelScope.launch {
                userRepository
                    .getNickname()
                    .catch { e ->
                        _sideEffect.emit(
                            OffboardingCompletedGuideSideEffect.ShowSnackBar(
                                "서버에 연결할 수 없습니다. 잠시 후 시도해 주세요.",
                            ),
                        )
                    }.collect { name ->
                        _uiState.update { it.copy(nickname = name) }
                    }
            }
            viewModelScope.launch {
                runCatching {
                    val journey = questStateRepository.getUserJourney() ?: "감정 직면"
                    _uiState.update { it.copy(journeyName = journey) }
                }.onFailure { e ->
                    _sideEffect.emit(
                        OffboardingCompletedGuideSideEffect.ShowSnackBar(
                            "서버에 연결할 수 없습니다. 잠시 후 시도해 주세요.",
                        ),
                    )
                }
            }
        }

        fun onCloseClicked() {
            viewModelScope.launch {
                _sideEffect.emit(
                    OffboardingCompletedGuideSideEffect.NavigateToHome,
                )
            }
        }

        fun onNewJourneyClicked() {
            viewModelScope.launch {
                savedStateHandle[ANIMATION_PLAYED] = true
                mixpanelUtil.trackEvent("journey_new_pageview")
                _sideEffect.emit(
                    OffboardingCompletedGuideSideEffect.NavigateToOffboardingNewJourney,
                )
            }
        }

        fun onCompletedJourneyClicked() {
            viewModelScope.launch {
                savedStateHandle[ANIMATION_PLAYED] = true
                mixpanelUtil.trackEvent("journey_review_pageview")
                _sideEffect.emit(
                    OffboardingCompletedGuideSideEffect.NavigateToOffboardingCompletedJourney,
                )
            }
        }
    }
