package com.byeboo.app.presentation.quest.common.navigation

import com.byeboo.app.core.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
sealed class QuestCommonRoute : Route {
    @Serializable
    data class QuestCommonWriting(
        val questId: Long,
        val answerId: Long,
        val isEditMode: Boolean,
    ) : QuestCommonRoute()

    @Serializable
    data class QuestCommonComplete(
        val questId: Long,
    ) : QuestCommonRoute()
}
