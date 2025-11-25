package com.byeboo.app.domain.model.quest

data class QuestRecordingModel(
    val answer: String,
    val questEmotionState: String
)

data class QuestRecordingEditModel(
    val answer: String,
)
