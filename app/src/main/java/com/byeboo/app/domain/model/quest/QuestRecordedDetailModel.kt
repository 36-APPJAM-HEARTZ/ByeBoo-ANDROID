package com.byeboo.app.domain.model.quest

data class QuestRecordedDetailModel(
    val stepNumber: Long,
    val questNumber: Long,
    val createdAt: String,
    val question: String,
    val questAnswer: String,
    val questEmotionState: String,
    val imageKey: String? = "",
    val imageUrl: String? = "",
    val emotionDescription: String
)
