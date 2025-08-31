package com.byeboo.app.data.mapper.auth

import com.byeboo.app.core.model.auth.TokenEntity
import com.byeboo.app.data.dto.response.auth.KakaoLoginResponseDto
import com.byeboo.app.data.dto.response.auth.TokenReissueResponseDto
import com.byeboo.app.domain.model.auth.AuthResult

fun KakaoLoginResponseDto.toDomain(): AuthResult = AuthResult(
    tokens = TokenEntity(accessToken = accessToken, refreshToken = refreshToken),
    isRegistered = isRegistered
)

fun TokenReissueResponseDto.toDomain(): TokenEntity = TokenEntity(
    accessToken = this.accessToken,
    refreshToken = this.refreshToken
)
