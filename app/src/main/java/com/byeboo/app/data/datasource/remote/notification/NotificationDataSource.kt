package com.byeboo.app.data.datasource.remote.notification

import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.response.notification.NotificationListResponseDto

interface NotificationDataSource {
    suspend fun getNotificationList(): BaseResponse<NotificationListResponseDto>
}