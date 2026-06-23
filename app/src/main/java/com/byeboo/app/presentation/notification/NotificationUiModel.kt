package com.byeboo.app.presentation.notification
import androidx.compose.runtime.Immutable
import com.byeboo.app.domain.model.notification.NotificationModel
import com.byeboo.app.domain.model.notification.NotificationType

@Immutable
data class NotificationUiModel(
    val notificationId: Long,
    val notificationType: NotificationType,
    val title: String,
    val content: String,
    val isRead: Boolean,
    val createdAt: String,
    val landingUrl: String,
)

fun NotificationModel.toUiModel(): NotificationUiModel {
    return NotificationUiModel(
        notificationId = this.notificationId,
        title = this.title,
        content = this.content,
        isRead = this.isRead,
        createdAt = this.createdAt,
        landingUrl = this.landingUrl,
        notificationType = this.type,
    )
}