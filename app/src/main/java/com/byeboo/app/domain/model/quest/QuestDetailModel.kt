package com.byeboo.app.domain.model.quest

data class QuestDetailModel(
    val step: String,
    val stepNumber: Long,
    val questNumber: Long,
    val questStyle: QuestStyle,
    val question: String,
)

enum class QuestStyle(
    val displayText: String,
) {
    RECORDING("질문형"),
    ACTIVE("행동형"),
}

fun QuestStyle.toJourneyText(): String =
    when (this) {
        QuestStyle.RECORDING -> "질문형"
        QuestStyle.ACTIVE -> "행동형"
    }
