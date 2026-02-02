package com.byeboo.app.data.mapper.notification

import com.byeboo.app.data.dto.request.notification.NotificationRequestDto
import com.byeboo.app.data.dto.response.notification.NotificationResponseDto
import com.byeboo.app.domain.model.notification.FcmTokenModel
import com.byeboo.app.domain.model.notification.NotificationSetting

fun FcmTokenModel.toData(): NotificationRequestDto =
    NotificationRequestDto(
        token = this.token
    )

fun NotificationResponseDto.toDomain(): NotificationSetting =
    NotificationSetting(
        alarmEnabled = this.alarmEnabled
    )
