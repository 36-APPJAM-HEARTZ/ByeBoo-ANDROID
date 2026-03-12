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
    RECORDING("이별 극복"),
    REUNION("재회 준비"),
}

fun OnboardingQuestStyle.toJourneyText(): String =
    when (this) {
        OnboardingQuestStyle.RECORDING -> "이별 극복"
        OnboardingQuestStyle.REUNION -> "재회 준비"
    }
