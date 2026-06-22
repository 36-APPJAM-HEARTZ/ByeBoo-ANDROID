package com.byeboo.app.domain.model.notification

data class Notification(
    val notificationId: Long,
    val content: String,
    val title: String,
    val isRead: Boolean,
    val createdAt: String,
    val landingUrl: String,
    val type: NotificationType
)

enum class NotificationType {
    QUEST_OPEN,
    COMMENT,
    LIKE,
    UNKNOWN
}