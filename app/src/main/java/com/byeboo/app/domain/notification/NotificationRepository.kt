package com.byeboo.app.domain.notification

import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.domain.model.notification.Notification

interface NotificationRepository {
    suspend fun checkHasUnreadNotifications(): Result<Boolean>
    suspend fun getNotificationList(): Result<List<Notification>>
}