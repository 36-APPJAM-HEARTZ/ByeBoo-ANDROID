package com.byeboo.app.presentation.main

import android.content.Intent
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.core.util.MixpanelUtil
import com.byeboo.app.domain.model.JourneyStatusType
import com.byeboo.app.domain.repository.auth.UserRepository
import com.byeboo.app.domain.repository.fcm.FcmTokenRepository
import com.byeboo.app.domain.repository.quest.QuestStateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val questStateRepository: QuestStateRepository,
    private val fcmTokenRepository: FcmTokenRepository,
    private val userRepository: UserRepository,
    private val mixpanelUtil: MixpanelUtil
) : ViewModel() {
    val journeyStatus: StateFlow<JourneyStatusType> = questStateRepository.getUserJourneyStatus()
        .stateIn(viewModelScope, SharingStarted.Eagerly, JourneyStatusType.UNKNOWN)

    private val _notificationQuestId = MutableStateFlow<String?>(null)
    val notificationQuestId: StateFlow<String?> = _notificationQuestId.asStateFlow()

    val isLoginCompleted: StateFlow<Boolean> = userRepository.getLoggedIn()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

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

    fun handleIntent(intent: Intent) {
        val questId = intent.getStringExtra("questId")
        if (questId != null) {
            updateNotificationQuestId(questId)
        }
    }
    
    fun updateNotificationQuestId(questId: String?) {
        _notificationQuestId.value = questId
    }

    fun clearNotificationQuestId() {
        _notificationQuestId.value = null
    }

    fun hasNotificationPermission() {
        viewModelScope.launch {
            if (isLoginCompleted.value) {
                fcmTokenRepository.saveAlarmEnabled(true)
                fcmTokenRepository.allowQuestAlarm()
            }
        }
    }
}
