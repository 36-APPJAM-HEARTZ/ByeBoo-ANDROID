package com.byeboo.app.domain.notification

import com.byeboo.app.domain.model.notification.Notification

interface NotificationRepository {
    suspend fun getNotificationList(): Result<List<Notification>>
}