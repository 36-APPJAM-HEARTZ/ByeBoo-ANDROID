package com.byeboo.app.domain.notification

import com.byeboo.app.domain.model.notification.NotificationModel
import kotlinx.coroutines.flow.StateFlow

interface NotificationRepository {
    val hasUnreadFlow: StateFlow<Boolean>

    suspend fun checkHasUnreadNotifications(): Result<Boolean>

    suspend fun getNotificationList(): Result<List<NotificationModel>>

    suspend fun markAllNotificationsAsRead(): Result<Unit>

    suspend fun markNotificationAsRead(notificationId: Long): Result<Unit>
}
