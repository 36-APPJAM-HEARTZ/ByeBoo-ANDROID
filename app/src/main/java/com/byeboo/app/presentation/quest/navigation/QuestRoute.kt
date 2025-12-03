package com.byeboo.app.presentation.quest.navigation

import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.core.navigation.MainTabRoute
import com.byeboo.app.core.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
data object Quest : MainTabRoute {
    const val ROUTE = "quest"
}

@Serializable
data class QuestStart(val questType: QuestType? = null) : Route

@Serializable
data class QuestTip(val questId: Long, val questType: QuestType) : Route

@Serializable
data class QuestReview(val questId: Long) : Route
