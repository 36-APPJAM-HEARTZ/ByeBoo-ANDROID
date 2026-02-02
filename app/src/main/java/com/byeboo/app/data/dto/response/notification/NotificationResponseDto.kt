package com.byeboo.app.data.dto.response.notification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationResponseDto(
    @SerialName("alarmEnabled")
    val alarmEnabled: Boolean
)
