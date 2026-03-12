package com.byeboo.app.domain.model.auth

import com.byeboo.app.core.model.auth.TokenEntity
import com.byeboo.app.domain.model.JourneyStatusType

data class AuthResult(
    val tokens: TokenEntity,
    val isRegistered: Boolean,
    val name: String?,
    val journey: JourneyType,
    val journeyStatus: JourneyStatusType,
    val userId: Long,
)

enum class JourneyType {
    FACE_EMOTION,
    PROCESS_EMOTION,
    PREPARE_REUNION,
    UNKNOWN,
}

fun JourneyType.toJourneyText(): String =
    when (this) {
        JourneyType.FACE_EMOTION -> "이별 극복"
        JourneyType.PROCESS_EMOTION -> "감정 정리"
        JourneyType.PREPARE_REUNION -> "재회 준비"
        JourneyType.UNKNOWN -> "알 수 없음"
    }
