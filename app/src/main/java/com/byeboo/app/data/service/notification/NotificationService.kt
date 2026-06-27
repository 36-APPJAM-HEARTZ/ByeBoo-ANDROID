package com.byeboo.app.data.service.notification

import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.base.NullableBaseResponse
import com.byeboo.app.data.dto.request.notification.NotificationRequestDto
import com.byeboo.app.data.dto.response.notification.NotificationListResponseDto
import com.byeboo.app.data.dto.response.notification.NotificationReadStatusResponseDto
import com.byeboo.app.data.dto.response.notification.NotificationResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

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

    @GET("/api/v1/notifications/unread/status")
    suspend fun getReadStatusNotification(): BaseResponse<NotificationReadStatusResponseDto>

    @GET("/api/v1/notifications")
    suspend fun getNotificationList(): BaseResponse<NotificationListResponseDto>

    @PATCH("/api/v1/notifications/read-all")
    suspend fun updateAllNotificationRead(): NullableBaseResponse<Unit>

    @PATCH("/api/v1/notifications/{notificationId}/read")
    suspend fun patchNotificationRead(
        @Path("notificationId") notificationId: Long
    ): NullableBaseResponse<Unit>

}
