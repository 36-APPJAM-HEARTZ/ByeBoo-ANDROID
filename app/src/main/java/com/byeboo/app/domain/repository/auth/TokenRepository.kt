package com.byeboo.app.domain.repository.auth

import com.byeboo.app.core.model.auth.TokenEntity
import kotlinx.coroutines.flow.Flow

interface TokenRepository {
    suspend fun getAccessToken(): Flow<String>
    suspend fun getRefreshToken(): Flow<String>
    suspend fun saveTokens(tokens: TokenEntity)
    suspend fun clearTokens()
    suspend fun initCachedAccessToken()
    fun updateCachedAccessToken(token: String)
    fun getCachedAccessToken(): String
}
