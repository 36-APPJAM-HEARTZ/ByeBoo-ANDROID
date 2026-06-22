package com.byeboo.app.data.dto.response.notification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationReadStatusResponseDto (
    @SerialName("hasUnread")
    val hasUnread: Boolean
)