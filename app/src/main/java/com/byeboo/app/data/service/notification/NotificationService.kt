package com.byeboo.app.data.service.notification

import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.base.NullableBaseResponse
import com.byeboo.app.data.dto.request.notification.NotificationRequestDto
import com.byeboo.app.data.dto.response.notification.NotificationResponseDto
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT

interface NotificationService {
    @POST("/api/v1/notification-tokens")
    suspend fun saveFcmToken(
        @Body request: NotificationRequestDto,
    ): NullableBaseResponse<Unit>

    @PATCH("/api/v1/notification-tokens")
    suspend fun updateFcmToken(
        @Body request: NotificationRequestDto,
    ): NullableBaseResponse<Unit>

    @PUT("/api/v1/notification-tokens")
    suspend fun deleteFcmToken(
        @Body request: NotificationRequestDto,
    ): NullableBaseResponse<Unit>

    @PATCH("/api/v1/users/alarm")
    suspend fun allowQuestAlarm(): BaseResponse<NotificationResponseDto>
}
