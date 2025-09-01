package com.byeboo.app.domain.model.auth

import com.byeboo.app.core.model.auth.TokenEntity

data class AuthResult(
    val tokens: TokenEntity,
    val isRegistered: Boolean,
    val name: String?,
    val journey: JourneyType,
    val journeyStatus: JourneyStatusType
)

enum class JourneyType {
    FACE_EMOTION,
    PROCESS_EMOTION,
    UNKNOWN;
}
enum class JourneyStatusType {
    BEFORE_START,
    IN_PROGRESS,
    COMPLETED,
    UNKNOWN;
}

fun JourneyType.toJourneyText(): String {
    return when(this) {
        JourneyType.FACE_EMOTION -> "감정 직면"
        JourneyType.PROCESS_EMOTION -> "감정 정리"
        JourneyType.UNKNOWN -> "알 수 없음"
    }
}

