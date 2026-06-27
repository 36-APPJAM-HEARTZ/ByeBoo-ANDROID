package com.byeboo.app.data.datasource.remote.notification

import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.base.NullableBaseResponse
import com.byeboo.app.data.dto.response.notification.NotificationListResponseDto
import com.byeboo.app.data.dto.response.notification.NotificationReadStatusResponseDto

interface NotificationDataSource {
    suspend fun checkHasUnreadNotifications(): BaseResponse<NotificationReadStatusResponseDto>
    suspend fun getNotificationList(): BaseResponse<NotificationListResponseDto>
    suspend fun patchAllNotificationsRead(): NullableBaseResponse<Unit>
    suspend fun patchNotificationRead(notificationId: Long): NullableBaseResponse<Unit>
}