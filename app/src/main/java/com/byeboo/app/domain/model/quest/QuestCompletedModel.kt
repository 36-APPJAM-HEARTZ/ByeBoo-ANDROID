package com.byeboo.app.domain.model.quest

data class QuestCompletedModel(
    val progressPeriod: String,
    val currentStep: Int,
    val steps: List<QuestCompletedStepModel>
)

data class QuestCompletedStepModel(
    val stepNumber: Long,
    val step: String,
    val quests: List<QuestCompletedQuestModel>
)

data class QuestCompletedQuestModel(
    val questId: Long,
    val question: String,
    val questStyle: String,
    val questNumber: Long
)
