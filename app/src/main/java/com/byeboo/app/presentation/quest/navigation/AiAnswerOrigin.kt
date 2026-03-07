package com.byeboo.app.presentation.quest.navigation

import com.byeboo.app.core.model.quest.QuestType
import kotlinx.serialization.Serializable

@Serializable
sealed interface AiAnswerOrigin {
    @Serializable
    data object Quest : AiAnswerOrigin

    @Serializable
    data class Offboarding(
        val questType: QuestType,
    ) : AiAnswerOrigin
}
