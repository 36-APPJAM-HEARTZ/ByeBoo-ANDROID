package com.byeboo.app.domain.model.auth

import com.byeboo.app.core.model.auth.TokenEntity

data class AuthResult(
    val tokens: TokenEntity,
    val isRegistered: Boolean,
    val name: String?,
    val journey: JourneyType,
    val journeyStatus: JourneyStatusType
)

enum class JourneyType(val journeyType: String){
    FACE_EMOTION("감정 직면"),
    PROCESS_EMOTION("감정 정리"),
    UNKNOWN("알 수 없음");
}
enum class JourneyStatusType(val journeyStatusType: String){
    BEFORE_START("진행 직전"),
    IN_PROGRESS("진행 중"),
    COMPLETED("진행 완료"),
    UNKNOWN("알 수 없음");
}

fun JourneyType.toJourneyText(): String {
    return when(this) {
        JourneyType.FACE_EMOTION -> "감정 직면"
        JourneyType.PROCESS_EMOTION -> "감정 정리"
        JourneyType.UNKNOWN -> "알 수 없음"
    }
}

fun JourneyStatusType.toJourneyStatusText(): String {
    return when(this) {
        JourneyStatusType.BEFORE_START -> "진행 직전"
        JourneyStatusType.IN_PROGRESS -> "진행 중"
        JourneyStatusType.COMPLETED -> "진행 완료"
        JourneyStatusType.UNKNOWN -> "알 수 없음"
    }
}

