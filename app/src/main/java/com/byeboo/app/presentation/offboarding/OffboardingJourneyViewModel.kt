package com.byeboo.app.presentation.offboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.core.model.quest.JourneyType
import com.byeboo.app.core.util.MixpanelUtil
import com.byeboo.app.domain.repository.offboarding.OffboardingJourneyRepository
import com.byeboo.app.presentation.offboarding.util.OffboardingJourneyMapper
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
class OffboardingJourneyViewModel
    @Inject
    constructor(
        private val offboardingJourneyRepository: OffboardingJourneyRepository,
        private val mapper: OffboardingJourneyMapper,
        private val mixpanelUtil: MixpanelUtil,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(OffboardingJourneyState())
        val uiState: StateFlow<OffboardingJourneyState> = _uiState.asStateFlow()

        private val _sideEffect = MutableSharedFlow<OffboardingJourneySideEffect>()
        val sideEffect: SharedFlow<OffboardingJourneySideEffect> = _sideEffect.asSharedFlow()

        init {
            getJourneyLists()
        }

        fun onBackClicked() {
            viewModelScope.launch {
                _sideEffect.emit(OffboardingJourneySideEffect.NavigateUp)
            }
        }

        fun onJourneyCompletedCardClicked(journey: JourneyType) {
            viewModelScope.launch {
                mixpanelUtil.trackEvent(
                    "journey_review_all_pageview",
                    mapOf("review_journey_type" to journey.journeyName),
                )
                _sideEffect.emit(
                    OffboardingJourneySideEffect.NavigateToOffboardingQuestCompleted(journey),
                )
            }
        }

        private fun getJourneyLists() {
            viewModelScope.launch {
                val result = offboardingJourneyRepository.getOffboardingJourney()
                result
                    .mapCatching { domain -> mapper.toUiState(domain) }
                    .onSuccess { output ->
                        _uiState.update {
                            it.copy(
                                journeyCards = output.journeyCards,
                            )
                        }
                    }.onFailure { e ->
                        _sideEffect.emit(
                            OffboardingJourneySideEffect.ShowSnackBar(
                                snackBarType = CustomSnackBarType.ALERT,
                            ),
                        )
                    }
            }
        }
    }
