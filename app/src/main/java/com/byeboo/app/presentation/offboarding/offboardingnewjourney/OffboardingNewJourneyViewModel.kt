package com.byeboo.app.presentation.offboarding.offboardingnewjourney

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.domain.model.JourneyStatusType
import com.byeboo.app.domain.repository.quest.QuestStateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class OffboardingNewJourneyViewModel @Inject constructor(
    private val questStateRepository: QuestStateRepository
) : ViewModel() {
    private val _sideEffect = MutableSharedFlow<OffboardingNewJourneySideEffect>()
    val sideEffect: SharedFlow<OffboardingNewJourneySideEffect> = _sideEffect.asSharedFlow()

    fun onBackClicked() {
        viewModelScope.launch {
            _sideEffect.emit(OffboardingNewJourneySideEffect.NavigateUp)
        }
    }

    fun postNewJourney(journey: QuestType) {
        val journeyName = journey.journeyName

        viewModelScope.launch {
            questStateRepository.updateUserJourney(journeyName)
            questStateRepository.updateUserJourneyStatus(JourneyStatusType.IN_PROGRESS)
            _sideEffect.emit(OffboardingNewJourneySideEffect.NavigateToQuestStart(journey))
        }
    }
}
