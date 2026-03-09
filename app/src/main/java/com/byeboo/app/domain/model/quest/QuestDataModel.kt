package com.byeboo.app.domain.model.quest

data class QuestDataModel(
    val inProgressQuest: QuestInProgressModel,
    val journeyTitle: String,
    val questCompletedCount: Long,
)
