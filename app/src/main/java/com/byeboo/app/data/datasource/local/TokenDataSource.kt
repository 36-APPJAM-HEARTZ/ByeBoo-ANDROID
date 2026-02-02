package com.byeboo.app.data.datasource.local

import kotlinx.coroutines.flow.Flow

interface TokenDataSource {
    fun getAccessToken(): Flow<String>

    fun getRefreshToken(): Flow<String>

    suspend fun updateTokens(
        accessToken: String,
        refreshToken: String,
    )

    suspend fun clearTokens()

    suspend fun setLoginSplash(show: Boolean)

    suspend fun restartSplash(): Boolean
}
