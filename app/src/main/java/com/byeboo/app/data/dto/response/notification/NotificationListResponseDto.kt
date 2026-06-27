package com.byeboo.app.data.dto.response.notification

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationListResponseDto(
    @SerialName("notifications")
    val notifications: List<NotificationsDto>,
)

@Serializable
data class NotificationsDto(
    @SerialName("notificationId")
    val notificationId: Long,
    @SerialName("content")
    val content: String,
    @SerialName("title")
    val title: String,
    @SerialName("isRead")
    val isRead: Boolean,
    @SerialName("createdAt")
    val createdAt: String,
    @SerialName("landingUrl")
    val landingUrl: String,
    @SerialName("notificationType")
    val notificationType: String,
)
