package com.byeboo.app.domain.model.quest

data class QuestData(
    val inProgressQuest: QuestInProgressModel,
    val journeyTitle: String,
    val userNickname: String,
    val questCompletedCount: Long
)
