package com.byeboo.app.data.dto.request.notification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationRequestDto(
    @SerialName("token")
    val token: String
)