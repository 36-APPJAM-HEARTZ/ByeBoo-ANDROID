package com.byeboo.app.domain.model.auth

data class UserInfoModel(
    val name: String,
    val feeling: String,
    val questStyle: String,
)

data class UserJourney(
    val journey: String,
    val description: String,
)

enum class Feeling(
    val displayText: String,
) {
    EXHAUSTED("너무 힘들어요"),
    RECOVERING("극복 중이에요"),
    OVERCOMING("꽤 극복했어요"),
}

enum class QuestStyle(
    val displayText: String,
) {
    RECORDING("질문에 답하기"),
    ACTIVE("활동 인증하기"),
}

fun QuestStyle.toJourneyText(): String =
    when (this) {
        QuestStyle.RECORDING -> "감정 직면"
        QuestStyle.ACTIVE -> "감정 정리"
    }
