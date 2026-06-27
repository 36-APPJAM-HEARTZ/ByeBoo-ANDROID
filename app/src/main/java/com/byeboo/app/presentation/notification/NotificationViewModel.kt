package com.byeboo.app.presentation.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.domain.notification.NotificationRepository
import com.byeboo.app.presentation.quest.util.QuestUiModelMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
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
class NotificationViewModel
    @Inject
    constructor(
        val notificationRepository: NotificationRepository,
        val questUiModelMapper: QuestUiModelMapper,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(NotificationUiState())
        val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()

        private val _sideEffect = MutableSharedFlow<NotificationSideEffect>()
        val sideEffect: SharedFlow<NotificationSideEffect> = _sideEffect.asSharedFlow()

        fun fetchNotificationList() {
            viewModelScope.launch {
                notificationRepository
                    .getNotificationList()
                    .onSuccess { domainList ->
                        _uiState.update {
                            it.copy(
                                notificationList =
                                    domainList
                                        .map { domainList ->
                                            domainList.toUiModel(questUiModelMapper)
                                        }.toPersistentList(),
                            )
                        }
                    }
            }
        }

        fun onBackClicked() {
            viewModelScope.launch {
                _sideEffect.emit(NotificationSideEffect.NavigateToHome)
            }
        }

        fun onAllNotificationsReadClicked() {
            viewModelScope.launch {
                if (!_uiState.value.isAllNotificationRead) {
                    notificationRepository.markAllNotificationsAsRead().onSuccess {
                        _uiState.update { notificationState ->
                            val updatedNotificationList =
                                notificationState.notificationList
                                    .map { notification ->
                                        notification.copy(isRead = true)
                                    }.toPersistentList()

                            notificationState.copy(notificationList = updatedNotificationList)
                        }
                    }
                }
                _uiState.update {
                    it.copy(
                        isAllNotificationRead = true,
                    )
                }
            }
        }

        fun onNotificationClicked(notification: NotificationUiModel) {
            viewModelScope.launch {
                if (!notification.isRead) {
                    notificationRepository
                        .markNotificationAsRead(notification.notificationId)
                        .onSuccess { updateNotificationAsRead(notification.notificationId) }
                }
                if (notification.landingUrl.isNotEmpty()) {
                    _sideEffect.emit(NotificationSideEffect.NavigateToDeepLink(notification.landingUrl))
                }
            }
        }

        private fun updateNotificationAsRead(notificationId: Long) {
            _uiState.update { state ->
                val updatedNotificationList =
                    state.notificationList
                        .map {
                            if (it.notificationId == notificationId) {
                                it.copy(isRead = true)
                            } else {
                                it
                            }
                        }.toPersistentList()
                state.copy(notificationList = updatedNotificationList)
            }
        }
    }
