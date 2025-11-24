package com.byeboo.app.data.datasource.remote.fcm

import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.base.NullableBaseResponse
import com.byeboo.app.data.dto.response.notification.NotificationResponseDto

interface FcmDataSource {
    suspend fun saveFcmToken(fcmToken: String): NullableBaseResponse<Unit>
    suspend fun updateFcmToken(fcmToken: String): NullableBaseResponse<Unit>
    suspend fun deleteFcmToken(fcmToken: String): NullableBaseResponse<Unit>
    suspend fun allowQuestAlarm(): BaseResponse<NotificationResponseDto>
}