package com.byeboo.app.core.network

import com.byeboo.app.domain.repository.auth.AuthRepository
import com.byeboo.app.domain.repository.auth.TokenRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator
    @Inject
    constructor(
        private val tokenRepository: TokenRepository,
        private val authRepository: AuthRepository,
    ) : Authenticator {
        private val mutex = Mutex()

        override fun authenticate(
            route: Route?,
            response: Response,
        ): Request? {
            if (responseCount(response) >= 2) return null

            return runBlocking(Dispatchers.IO) {
                try {
                    mutex.withLock {
                        val currentAccessToken =
                            tokenRepository.getAccessToken().firstOrNull().orEmpty()
                        val requestAccessToken =
                            response.request
                                .header(AUTHORIZATION)
                                ?.substringAfter(BEARER)
                                ?.trim()
                                .orEmpty()

                        if (currentAccessToken != requestAccessToken) {
                            return@runBlocking response.request
                                .newBuilder()
                                .removeHeader(AUTHORIZATION)
                                .addHeader(AUTHORIZATION, "$BEARER $currentAccessToken")
                                .build()
                        }

                        val refreshToken = tokenRepository.getRefreshToken().firstOrNull().orEmpty()

                        if (refreshToken.isEmpty()) {
                            tokenRepository.clearTokens()
                            tokenRepository.setLoginSplash(show = true, isTokenExpired = true)
                            return@runBlocking null
                        }

                        val result = authRepository.reissueAccessToken(refreshToken)
                        val newAuthenticatedToken = result.getOrNull()

                        if (newAuthenticatedToken == null) {
                            tokenRepository.clearTokens()
                            tokenRepository.setLoginSplash(show = true, isTokenExpired = true)
                            return@runBlocking null
                        }

                        tokenRepository.saveTokens(newAuthenticatedToken)

                        return@runBlocking response.request
                            .newBuilder()
                            .removeHeader(AUTHORIZATION)
                            .addHeader(
                                AUTHORIZATION,
                                "$BEARER ${newAuthenticatedToken.accessToken}",
                            ).build()
                    }
                } catch (e: Exception) {
                    tokenRepository.clearTokens()
                    tokenRepository.setLoginSplash(show = true, isTokenExpired = true)
                    return@runBlocking null
                }
            }
        }

        private fun responseCount(response: Response): Int {
            var response: Response? = response
            var count = 1

            while (response?.priorResponse != null) {
                count++
                response = response.priorResponse
            }
            return count
        }

        companion object {
            const val AUTHORIZATION = "Authorization"
            const val BEARER = "Bearer"
        }
    }
