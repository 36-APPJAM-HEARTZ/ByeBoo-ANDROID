package com.byeboo.app.core.network

import com.byeboo.app.domain.repository.auth.TokenRepository
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor @Inject constructor(
    private val tokenRepository: TokenRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val accessToken = runBlocking {
            tokenRepository.getCachedAccessToken()
        }

        val authenticatedRequest = originalRequest.newBuilder().apply {
            if (accessToken.isNotBlank()) {
                header(AUTHORIZATION, "$BEARER $accessToken")
            }
        }.build()

        return chain.proceed(authenticatedRequest)
    }

    companion object {
        const val AUTHORIZATION = "Authorization"
        const val BEARER = "Bearer"
    }
}
