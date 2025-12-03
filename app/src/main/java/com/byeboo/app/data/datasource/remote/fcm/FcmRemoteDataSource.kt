package com.byeboo.app.data.datasource.remote.fcm

import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.base.NullableBaseResponse
import com.byeboo.app.data.dto.request.notification.NotificationRequestDto
import com.byeboo.app.data.dto.response.notification.NotificationResponseDto

interface FcmRemoteDataSource {
    suspend fun saveFcmToken(request: NotificationRequestDto): NullableBaseResponse<Unit>
    suspend fun updateFcmToken(request: NotificationRequestDto): NullableBaseResponse<Unit>
    suspend fun deleteFcmToken(request: NotificationRequestDto): NullableBaseResponse<Unit>
    suspend fun allowQuestAlarm(): BaseResponse<NotificationResponseDto>
}