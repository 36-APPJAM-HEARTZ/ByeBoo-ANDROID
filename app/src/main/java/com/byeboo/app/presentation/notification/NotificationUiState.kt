package com.byeboo.app.presentation.notification

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class NotificationUiState(
    val notificationList: ImmutableList<NotificationModel> = persistentListOf(),
)


sealed interface NotificationSideEffect {
    data object NavigateToHome: NotificationSideEffect
}
