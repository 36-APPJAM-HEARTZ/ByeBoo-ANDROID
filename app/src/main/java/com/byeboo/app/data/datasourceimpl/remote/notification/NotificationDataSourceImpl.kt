package com.byeboo.app.data.datasourceimpl.remote.notification

import com.byeboo.app.data.datasource.remote.notification.NotificationDataSource
import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.base.NullableBaseResponse
import com.byeboo.app.data.dto.response.notification.NotificationListResponseDto
import com.byeboo.app.data.dto.response.notification.NotificationReadStatusResponseDto
import com.byeboo.app.data.service.notification.NotificationService
import javax.inject.Inject

class NotificationDataSourceImpl @Inject constructor(
    private val notificationService: NotificationService
): NotificationDataSource {
    override suspend fun checkHasUnreadNotifications(): BaseResponse<NotificationReadStatusResponseDto> =
        notificationService.getReadStatusNotification()

    override suspend fun getNotificationList(): BaseResponse<NotificationListResponseDto> =
        notificationService.getNotificationList()

    override suspend fun patchAllNotificationsRead(): NullableBaseResponse<Unit> =
        notificationService.updateAllNotificationRead()

    override suspend fun patchNotificationRead(notificationId: Long): NullableBaseResponse<Unit> =
        notificationService.patchNotificationRead(notificationId = notificationId)

}