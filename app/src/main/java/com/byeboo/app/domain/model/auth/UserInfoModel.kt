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

enum class QuestStyle(
    val displayText: String,
) {
    RECORDING("재회 준비"),
    ACTIVE("이별 극복"),
}

fun QuestStyle.toJourneyText(): String =
    when (this) {
        QuestStyle.RECORDING -> "감정 직면"
        QuestStyle.ACTIVE -> "감정 정리"
    }
