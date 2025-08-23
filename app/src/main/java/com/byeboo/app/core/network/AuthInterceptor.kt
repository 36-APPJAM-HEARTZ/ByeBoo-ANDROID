package com.byeboo.app.core.network

import com.byeboo.app.domain.repository.auth.TokenRepository
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor @Inject constructor(
    private val tokenRepository: TokenRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val masterToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3NTU4MDM3NjEsImV4cCI6MTc1NzAxMzM2MX0.xbZVCx2_3gyGxRAQzbNnGN4Pp2Rwwv-0j4whiGBMOlY"

        val newRequest = request.newBuilder()
            .addHeader(AUTH_HEADER_KEY, "Bearer $masterToken")
            .build()
        return chain.proceed(newRequest)
    }

    companion object {
        const val AUTH_HEADER_KEY = "Authorization"
    }
}
