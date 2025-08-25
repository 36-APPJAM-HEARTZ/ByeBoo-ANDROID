package com.byeboo.app.domain.model.quest

data class QuestStateModel(
    val todayComplete: Boolean,
    val userCurrentStatus: String,
    val count: Int
)

data class QuestDialogue(
    val dialogue: String
)
