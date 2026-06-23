package com.byeboo.app.presentation.notification

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class NotificationUiState(
    val notificationList: ImmutableList<NotificationUiModel> = persistentListOf(),
)

sealed interface NotificationSideEffect {
    data object NavigateToHome: NotificationSideEffect
}
