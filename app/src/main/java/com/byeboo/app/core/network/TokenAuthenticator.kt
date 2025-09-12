package com.byeboo.app.core.network

import com.byeboo.app.domain.repository.auth.AuthRepository
import com.byeboo.app.domain.repository.auth.TokenRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TokenAuthenticator @Inject constructor(
    private val tokenRepository: TokenRepository,
    private val authRepository: AuthRepository
) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request? = runBlocking {
        if (responseCount(response) >= 2) return@runBlocking null

        val refreshToken = tokenRepository.getRefreshToken().firstOrNull() ?: return@runBlocking null

        val result = authRepository.reissueAccessToken(refreshToken)

        val newAuthenticatedToken = result.getOrNull()

        if (newAuthenticatedToken == null) {
            tokenRepository.clearTokens()
            return@runBlocking null
        }

        tokenRepository.saveTokens(newAuthenticatedToken)

        response.request.newBuilder()
            .removeHeader(AUTHORIZATION)
            .addHeader(AUTHORIZATION, "$BEARER ${newAuthenticatedToken.accessToken}")
            .build()
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
