package com.byeboo.app.presentation.notification

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class NotificationUiState(
    val notificationList: ImmutableList<NotificationUiModel> = persistentListOf(),
    val isAllNotificationRead: Boolean = false,
)

sealed interface NotificationSideEffect {
    data object NavigateToHome : NotificationSideEffect

    data class NavigateToDeepLink(
        val landingUrl: String,
    ) : NotificationSideEffect
}
