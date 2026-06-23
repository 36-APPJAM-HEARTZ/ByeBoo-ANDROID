package com.byeboo.app.domain.notification

import com.byeboo.app.domain.model.notification.NotificationModel

interface NotificationRepository {
    suspend fun checkHasUnreadNotifications(): Result<Boolean>
    suspend fun getNotificationList(): Result<List<NotificationModel>>
}