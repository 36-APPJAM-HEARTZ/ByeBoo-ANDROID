package com.byeboo.app.presentation.notification

import androidx.annotation.DrawableRes
import com.byeboo.app.R

sealed class NotificationType(
    @param:DrawableRes val icon: Int,
    val title: String,
    val content: String,
) {
    data class QuestOpen(
        val questId: Long,
        val openTitle: String,
        val openContent: String
    ): NotificationType(icon = R.drawable.ic_notification_quest, title = openTitle, content = openContent)

    data class QuestEmpathy(
        val questId: Long,
        val empathyTitle: String,
        val empathyContent: String
    ): NotificationType(icon = R.drawable.ic_notification_reaction, title = empathyTitle, content=empathyContent)

    data class QuestReply(
        val questId: Long,
        val replyTitle: String,
        val replyContent: String,
    ): NotificationType(icon = R.drawable.ic_notification_reaction, title = replyTitle, content = replyContent)
}

