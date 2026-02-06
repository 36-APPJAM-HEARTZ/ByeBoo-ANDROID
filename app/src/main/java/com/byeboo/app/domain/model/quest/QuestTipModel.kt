package com.byeboo.app.domain.model.quest

data class QuestTip(
    val question: String,
    val step: String,
    val stepNumber: Long,
    val questNumber: Long,
    val tips: List<QuestTips>,
)

data class QuestTips(
    val tipStep: Int,
    val tipAnswer: String,
)
