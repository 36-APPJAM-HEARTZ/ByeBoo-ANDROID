package com.byeboo.app.presentation.main

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.core.util.MixpanelUtil
import com.byeboo.app.domain.model.JourneyStatusType
import com.byeboo.app.domain.repository.auth.TokenRepository
import com.byeboo.app.domain.repository.quest.QuestStateRepository
import com.byeboo.app.fcm.ByebooNotificationHandler.Companion.DESTINATION
import com.byeboo.app.fcm.ByebooNotificationHandler.Companion.QUEST_HOME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel
    @Inject
    constructor(
        private val questStateRepository: QuestStateRepository,
        private val tokenRepository: TokenRepository,
        private val mixpanelUtil: MixpanelUtil,
    ) : ViewModel() {
        val journeyStatus: StateFlow<JourneyStatusType> =
            questStateRepository
                .getUserJourneyStatus()
                .stateIn(viewModelScope, SharingStarted.Eagerly, JourneyStatusType.UNKNOWN)

        private val _questHomeNavigation = MutableStateFlow<Boolean>(false)
        val questHomeNavigation: StateFlow<Boolean> = _questHomeNavigation.asStateFlow()

        val tokenExpiredEvent: Flow<Unit> = tokenRepository.tokenExpiredEvent

        fun trackJourneyStart() {
            viewModelScope.launch {
                val journey = questStateRepository.getUserJourney() ?: "추적 실패"

                mixpanelUtil.trackEvent(
                    eventName = "journey_start_pageview",
                    properties =
                        mapOf(
                            "journey_type" to journey,
                        ),
                )
            }
        }

        fun handleIntent(intent: Intent) {
            val destination = intent.getStringExtra(DESTINATION)

            if (destination == QUEST_HOME) {
                _questHomeNavigation.value = true
            }
        }

        fun clearNavigateQuestHome() {
            _questHomeNavigation.value = false
        }
    }
