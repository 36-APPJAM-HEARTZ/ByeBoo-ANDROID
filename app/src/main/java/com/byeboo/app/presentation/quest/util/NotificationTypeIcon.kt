package com.byeboo.app.presentation.quest.util

import androidx.annotation.DrawableRes
import com.byeboo.app.R
import com.byeboo.app.presentation.notification.NotificationModel
import com.byeboo.app.presentation.notification.NotificationType

val NotificationModel.iconResId: Int
    @DrawableRes get() =
        when (this.notificationType) {
            NotificationType.QUEST_OPEN -> R.drawable.ic_notification_quest
            NotificationType.COMMENT, NotificationType.LIKE -> R.drawable.ic_notification_reaction
            NotificationType.UNKNOWN -> R.drawable.ic_notification_reaction
        }
