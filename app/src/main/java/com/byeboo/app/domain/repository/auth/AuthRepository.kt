package com.byeboo.app.domain.repository.auth

import com.byeboo.app.core.model.auth.TokenEntity
import com.byeboo.app.domain.model.auth.AuthResult

interface AuthRepository {
    suspend fun loginWithKakao(token: String, platform: String): Result<AuthResult>
    suspend fun reissueAccessToken(refreshToken: String): Result<TokenEntity>
    suspend fun logoutAccount(token: String): Result<Unit>
    suspend fun withdrawAccount(token: String): Result<Unit>
}
