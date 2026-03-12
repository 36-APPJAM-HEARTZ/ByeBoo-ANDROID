package com.byeboo.app.domain.model.auth

import com.byeboo.app.core.model.auth.TokenEntity
import com.byeboo.app.core.model.quest.JourneyType
import com.byeboo.app.domain.model.JourneyStatusType

data class AuthResult(
    val tokens: TokenEntity,
    val isRegistered: Boolean,
    val name: String?,
    val journey: JourneyType,
    val journeyStatus: JourneyStatusType,
    val userId: Long,
)
