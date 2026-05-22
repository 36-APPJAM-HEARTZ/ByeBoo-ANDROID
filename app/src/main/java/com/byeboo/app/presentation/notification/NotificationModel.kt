package com.byeboo.app.presentation.notification
import androidx.compose.runtime.Immutable

@Immutable
data class NotificationModel(
    val notificationId: Long,
    val title: String,
    val content: String,
    val createdAt: String,
    val isRead: Boolean,
    val landingLink: String
)
