package com.byeboo.app.presentation.quest.util

import androidx.annotation.DrawableRes
import com.byeboo.app.R
import com.byeboo.app.presentation.notification.NotificationModel

val NotificationModel.iconResId: Int
    @DrawableRes get() = when (this.notificationType) {
        "QUEST_OPEN" -> R.drawable.ic_notification_quest
        "COMMENT", "LIKE" -> R.drawable.ic_notification_reaction
        else -> R.drawable.ic_notification_reaction
    }