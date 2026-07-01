package com.byeboo.app.presentation.notification.model
import androidx.compose.runtime.Immutable
import com.byeboo.app.core.util.TimeFormatter
import com.byeboo.app.domain.model.notification.NotificationModel
import com.byeboo.app.domain.model.notification.NotificationType
import com.byeboo.app.presentation.quest.util.QuestUiModelMapper
import java.time.LocalDateTime

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

fun NotificationModel.toUiModel(mapper: QuestUiModelMapper): NotificationUiModel {
    val localDateTime =
        runCatching {
            LocalDateTime.parse(this.createdAt)
        }.getOrNull()

    return NotificationUiModel(
        notificationId = this.notificationId,
        title = this.title,
        content = this.content,
        isRead = this.isRead,
        createdAt = TimeFormatter.formatWrittenTime(localDateTime),
        landingUrl = this.landingUrl,
        notificationType = this.type,
    )
}
