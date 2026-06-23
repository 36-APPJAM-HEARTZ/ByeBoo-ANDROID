package com.byeboo.app.presentation.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.domain.notification.NotificationRepository
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
    val notificationRepository: NotificationRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()

    private val _sideEffect = MutableSharedFlow<NotificationSideEffect>()
    val sideEffect: SharedFlow<NotificationSideEffect> = _sideEffect.asSharedFlow()

    init {
        fetchNotificationList()
    }

    fun fetchNotificationList() {
        viewModelScope.launch {
            notificationRepository.getNotificationList()
                .onSuccess { domainList ->
                    _uiState.update { it.copy(notificationList = domainList.map { domainList ->
                        domainList.toUiModel() }.toPersistentList())
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
            notificationRepository.markAllNotificationsAsRead().onSuccess {
                _uiState.update { notificationState ->
                    val updatedNotificationList = notificationState.notificationList.map { notification->
                        notification.copy(isRead = true)
                    }.toPersistentList()

                    notificationState.copy(notificationList = updatedNotificationList)
                }
            }
        }
        _uiState.update { it.copy(
            isAllNotificationRead = true
        ) }
    }
}


