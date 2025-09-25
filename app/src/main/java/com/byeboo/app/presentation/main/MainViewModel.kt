package com.byeboo.app.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.core.util.MixpanelUtil
import com.byeboo.app.domain.model.JourneyStatusType
import com.byeboo.app.domain.repository.quest.QuestStateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class MainViewModel @Inject constructor(
    private val questStateRepository: QuestStateRepository,
    private val mixpanelUtil: MixpanelUtil
) : ViewModel() {
    val journeyStatus: StateFlow<JourneyStatusType> = questStateRepository.getUserJourneyStatus()
        .stateIn(viewModelScope, SharingStarted.Eagerly, JourneyStatusType.UNKNOWN)

    fun trackJourneyStart() {
        viewModelScope.launch {
            val journey = questStateRepository.getUserJourney() ?: "추적 실패"

            mixpanelUtil.trackEvent(
                eventName = "journey_start_pageview",
                properties = mapOf(
                    "journey_type" to journey
                )
            )
        }
    }
}
