package com.byeboo.app.domain.notification

import com.byeboo.app.domain.model.notification.NotificationModel

interface NotificationRepository {
    suspend fun checkHasUnreadNotifications(): Result<Boolean>
    suspend fun getNotificationList(): Result<List<NotificationModel>>
    suspend fun markAllNotificationsAsRead(): Result<Unit>
    suspend fun markNotificationAsRead(notificationId: Long): Result<Unit>
}