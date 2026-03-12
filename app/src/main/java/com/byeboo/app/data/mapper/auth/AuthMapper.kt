package com.byeboo.app.data.mapper.auth

import com.byeboo.app.core.model.auth.TokenEntity
import com.byeboo.app.data.dto.response.auth.KakaoLoginResponseDto
import com.byeboo.app.data.dto.response.auth.TokenReissueResponseDto
import com.byeboo.app.domain.model.JourneyStatusType
import com.byeboo.app.domain.model.auth.AuthResult
import com.byeboo.app.domain.model.auth.JourneyType

fun KakaoLoginResponseDto.toDomain(): AuthResult =
    AuthResult(
        tokens = TokenEntity(accessToken = accessToken, refreshToken = refreshToken),
        isRegistered = isRegistered,
        name = name,
        journey = journey.toJourneyType(),
        journeyStatus = journeyStatus.toJourneyStatus(),
        userId = userId,
    )

internal fun String?.toJourneyType(): JourneyType =
    when (this) {
        "FACE_EMOTION" -> JourneyType.FACE_EMOTION
        "PROCESS_EMOTION" -> JourneyType.PROCESS_EMOTION
        "PREPARE_REUNION" -> JourneyType.PREPARE_REUNION
        else -> JourneyType.UNKNOWN
    }

internal fun String?.toJourneyStatus(): JourneyStatusType =
    when (this) {
        "BEFORE_START" -> JourneyStatusType.BEFORE_START
        "IN_PROGRESS" -> JourneyStatusType.IN_PROGRESS
        "COMPLETED" -> JourneyStatusType.COMPLETED
        else -> JourneyStatusType.UNKNOWN
    }

fun TokenReissueResponseDto.toDomain(): TokenEntity =
    TokenEntity(
        accessToken = this.accessToken,
        refreshToken = this.refreshToken,
    )
