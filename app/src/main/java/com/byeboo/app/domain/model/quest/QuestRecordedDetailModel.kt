package com.byeboo.app.domain.model.quest

data class QuestRecordedDetailModel(
    val question: String,
    val questAnswer: String,
    val imageUrl: String? = "",
    val stepNumber: Long,
    val questNumber: Long,
    val createdAt: String,
    val questEmotionState: String,
    val emotionDescription: String
)
