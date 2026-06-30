package com.byeboo.app.data.mapper.notification

import com.byeboo.app.data.dto.request.notification.NotificationRequestDto
import com.byeboo.app.data.dto.response.notification.NotificationResponseDto
import com.byeboo.app.data.dto.response.notification.NotificationsDto
import com.byeboo.app.domain.model.notification.FcmTokenModel
import com.byeboo.app.domain.model.notification.NotificationModel
import com.byeboo.app.domain.model.notification.NotificationSetting
import com.byeboo.app.domain.model.notification.NotificationType

fun FcmTokenModel.toData(): NotificationRequestDto =
    NotificationRequestDto(
        token = this.token,
    )

fun NotificationResponseDto.toDomain(): NotificationSetting =
    NotificationSetting(
        alarmEnabled = this.alarmEnabled,
    )

fun NotificationsDto.toDomain(): NotificationModel =
    NotificationModel(
        notificationId = this.notificationId,
        content = this.content,
        title = this.title,
        isRead = this.isRead,
        createdAt = this.createdAt,
        landingUrl = this.landingUrl,
        type = this.notificationType.toNotificationType(),
    )

private fun String.toNotificationType(): NotificationType =
    try {
        NotificationType.valueOf(this)
    } catch (_: IllegalArgumentException) {
        NotificationType.UNKNOWN
    }
