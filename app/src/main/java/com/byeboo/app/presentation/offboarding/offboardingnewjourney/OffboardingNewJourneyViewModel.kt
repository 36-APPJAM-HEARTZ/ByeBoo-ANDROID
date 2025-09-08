package com.byeboo.app.presentation.offboarding.offboardingnewjourney

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.domain.model.JourneyStatusType
import com.byeboo.app.domain.repository.offboarding.OffboardingNewJourneyRepository
import com.byeboo.app.domain.repository.quest.QuestStateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OffboardingNewJourneyViewModel @Inject constructor(
    private val questStateRepository: QuestStateRepository,
    private val offboardingNewJourneyRepository: OffboardingNewJourneyRepository
) : ViewModel() {
    private val _sideEffect = MutableSharedFlow<OffboardingNewJourneySideEffect>()
    val sideEffect: SharedFlow<OffboardingNewJourneySideEffect> = _sideEffect.asSharedFlow()

    fun onBackClicked() {
        viewModelScope.launch {
            _sideEffect.emit(OffboardingNewJourneySideEffect.NavigateUp)
        }
    }

    fun postNewJourney(journey: QuestType) {
        val journeyType = journey.journeyType
        val journeyName = journey.journeyName

        viewModelScope.launch {
            offboardingNewJourneyRepository.postOffboardingNewJourney(journeyType)
                .onSuccess {
                    questStateRepository.updateUserJourney(journeyName)
                    questStateRepository.updateUserJourneyStatus(JourneyStatusType.IN_PROGRESS)
                    _sideEffect.emit(OffboardingNewJourneySideEffect.NavigateToQuestStart)
                }
                .onFailure { e ->

                }
        }
    }
}
