package com.byeboo.app.data.repositoryimpl.auth

import com.byeboo.app.core.model.auth.TokenEntity
import com.byeboo.app.data.datasource.local.TokenDataSource
import com.byeboo.app.domain.repository.auth.TokenRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

class TokenRepositoryImpl
    @Inject
    constructor(
        private val tokenDataSource: TokenDataSource,
    ) : TokenRepository {
        @Volatile
        private var cachedAccessToken: String = ""

        private val _tokenExpiredEvent = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

        override val tokenExpiredEvent: Flow<Unit> = _tokenExpiredEvent.asSharedFlow()

        override fun getAccessToken(): Flow<String> = tokenDataSource.getAccessToken()

        override fun getRefreshToken(): Flow<String> = tokenDataSource.getRefreshToken()

        override suspend fun saveTokens(tokens: TokenEntity) {
            tokenDataSource.updateTokens(tokens.accessToken, tokens.refreshToken)
            updateCachedAccessToken(tokens.accessToken)
        }

        override suspend fun clearTokens() {
            tokenDataSource.clearTokens()
            updateCachedAccessToken("")
        }

        override suspend fun initCachedAccessToken() {
            cachedAccessToken = tokenDataSource.getAccessToken().firstOrNull().orEmpty()
        }

        override fun getCachedAccessToken(): String = cachedAccessToken

        override fun updateCachedAccessToken(token: String) {
            cachedAccessToken = token
        }

        override suspend fun setLoginSplash(
            show: Boolean,
            isTokenExpired: Boolean,
        ) {
            tokenDataSource.setLoginSplash(show)
            if (isTokenExpired) {
                _tokenExpiredEvent.tryEmit(Unit)
            }
        }

        override suspend fun restartSplash(): Boolean = tokenDataSource.restartSplash()
    }
