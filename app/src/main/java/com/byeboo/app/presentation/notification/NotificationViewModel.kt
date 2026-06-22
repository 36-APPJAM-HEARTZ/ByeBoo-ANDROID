package com.byeboo.app.presentation.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.byeboo.app.domain.notification.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
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
        viewModelScope.launch {
            notificationRepository.getNotificationList()
        }
    }


    fun onBackClicked() {
        viewModelScope.launch {
            _sideEffect.emit(NotificationSideEffect.NavigateToHome)
        }
    }
}


