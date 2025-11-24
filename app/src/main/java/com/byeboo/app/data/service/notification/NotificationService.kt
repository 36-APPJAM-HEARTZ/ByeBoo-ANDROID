package com.byeboo.app.data.service.notification

import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.base.NullableBaseResponse
import com.byeboo.app.data.dto.response.notification.NotificationResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.PATCH
import retrofit2.http.POST

interface NotificationService {
    @POST("/api/v1/notification-tokens")
    suspend fun saveFcmToken(
        @Body fcmToken: String
    ): NullableBaseResponse<Unit>

    @PATCH("/api/v1/notification-tokens")
    suspend fun updateFcmToken(
        @Body fcmToken: String
    ): NullableBaseResponse<Unit>

    @DELETE("/api/v1/notification-tokens")
    suspend fun deleteFcmToken(
        @Body fcmToken: String
    ): NullableBaseResponse<Unit>

    @PATCH("/api/v1/users/alarm")
    suspend fun allowQuestAlarm(): BaseResponse<NotificationResponseDto>
}