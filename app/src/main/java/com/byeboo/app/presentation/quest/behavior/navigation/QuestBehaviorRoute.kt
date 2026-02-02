package com.byeboo.app.presentation.quest.behavior.navigation

import com.byeboo.app.core.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
sealed class QuestBehavior : Route {
    @Serializable
    data class QuestBehaviorWriting(
        val questId: Long,
        val isEditMode: Boolean,
        val fromOffboarding: Boolean,
        val imageKey: String?
    ) : QuestBehavior()

    @Serializable
    data class QuestBehaviorComplete(
        val questId: Long
    ) : QuestBehavior()
}
