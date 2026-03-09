package com.byeboo.app.domain.model.auth

data class UserInfoModel(
    val name: String,
    val questStyle: String,
)

data class UserJourney(
    val journey: String,
    val description: String,
)

enum class OnboardingQuestStyle(
    val displayText: String,
    ) {
    REUNION("재회 준비"),
    RECORDING("이별 극복")
}

fun OnboardingQuestStyle.toJourneyText(): String =
    when (this) {
        OnboardingQuestStyle.REUNION -> "재회 준비"
        OnboardingQuestStyle.RECORDING -> "이별 극복"
    }