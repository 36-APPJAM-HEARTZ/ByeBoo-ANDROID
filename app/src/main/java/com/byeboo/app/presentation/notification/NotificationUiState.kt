package com.byeboo.app.presentation.notification

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class NotificationUiState(
    val hasNotification: Boolean = false,
    val notificationList: ImmutableList<NotificationModel> = persistentListOf(),
)