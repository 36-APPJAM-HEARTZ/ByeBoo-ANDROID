package com.byeboo.app.data.datasourceimpl.remote.fcm

import com.byeboo.app.data.datasource.remote.fcm.FcmRemoteDataSource
import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.base.NullableBaseResponse
import com.byeboo.app.data.dto.request.notification.NotificationRequestDto
import com.byeboo.app.data.dto.response.notification.NotificationResponseDto
import com.byeboo.app.data.service.notification.NotificationService
import javax.inject.Inject

class FcmRemoteDataSourceImpl
@Inject
constructor(
    private val notificationService: NotificationService
) : FcmRemoteDataSource {
    override suspend fun saveFcmToken(request: NotificationRequestDto): NullableBaseResponse<Unit> =
        notificationService.saveFcmToken(request)

    override suspend fun updateFcmToken(request: NotificationRequestDto): NullableBaseResponse<Unit> =
        notificationService.updateFcmToken(request)

    override suspend fun deleteFcmToken(request: NotificationRequestDto): NullableBaseResponse<Unit> =
        notificationService.deleteFcmToken(request)

    override suspend fun allowQuestAlarm(): BaseResponse<NotificationResponseDto> = notificationService.allowQuestAlarm()
}
