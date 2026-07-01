package com.byeboo.app.presentation.notification

import androidx.annotation.DrawableRes
import com.byeboo.app.R
import com.byeboo.app.domain.model.notification.NotificationType

val NotificationType.iconResId: Int
    @DrawableRes get() =
        when (this) {
            NotificationType.QUEST_OPEN -> R.drawable.ic_notification_quest
            NotificationType.COMMENT, NotificationType.LIKE -> R.drawable.ic_notification_reaction
            NotificationType.UNKNOWN -> R.drawable.ic_notification_reaction
        }
