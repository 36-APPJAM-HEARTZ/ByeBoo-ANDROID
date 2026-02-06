package com.byeboo.app.domain.model.quest

data class SignedUrlRequestModel(
    val contentType: String,
    val imageKey: String,
)

data class BehaviorAnswerRequestModel(
    val answer: String,
    val questEmotionState: String,
    val imageKey: String,
)

data class QuestBehaviorEditModel(
    val answer: String,
    val imageKey: String,
)
