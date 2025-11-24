package com.byeboo.app.data.datasourceimpl.remote.fcm

import com.byeboo.app.data.datasource.remote.fcm.FcmDataSource
import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.base.NullableBaseResponse
import com.byeboo.app.data.dto.response.notification.NotificationResponseDto
import com.byeboo.app.data.service.notification.NotificationService
import javax.inject.Inject

class FcmDataSourceImpl @Inject constructor(
    private val notificationService: NotificationService
): FcmDataSource {
    override suspend fun saveFcmToken(fcmToken: String): NullableBaseResponse<Unit> {
        return  notificationService.saveFcmToken(fcmToken)
    }

    override suspend fun updateFcmToken(fcmToken: String): NullableBaseResponse<Unit> {
        return notificationService.updateFcmToken(fcmToken)
    }

    override suspend fun deleteFcmToken(fcmToken: String): NullableBaseResponse<Unit> {
        return notificationService.deleteFcmToken(fcmToken)
    }

    override suspend fun allowQuestAlarm(): BaseResponse<NotificationResponseDto> {
        return notificationService.allowQuestAlarm()
    }
}