package com.byeboo.app.presentation.quest.navigation

import com.byeboo.app.core.model.quest.JourneyType
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.core.navigation.MainTabRoute
import com.byeboo.app.core.navigation.Route
import kotlinx.serialization.Serializable

@Serializable
data object Quest : MainTabRoute {
    const val ROUTE = "quest"
}

@Serializable
data class QuestStart(
    val journeyType: JourneyType? = null,
) : Route

@Serializable
data class QuestTip(
    val questId: Long,
    val questType: QuestType,
) : Route

@Serializable
data class QuestReview(
    val questId: Long,
) : Route

@Serializable
data class QuestCommonAnswer(
    val answerId: Long,
) : Route

@Serializable
data object QuestMyAnswers : Route

@Serializable
data class QuestAiAnswer(
    val questId: Long,
    val isExistedAiAnswer: Boolean,
    val aiAnswerOrigin: AiAnswerOrigin,
    val questType: QuestType? = null,
) : Route
