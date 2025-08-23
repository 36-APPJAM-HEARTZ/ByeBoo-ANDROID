package com.byeboo.app.core.network

import com.byeboo.app.BuildConfig.MASTER_KEY
import com.byeboo.app.domain.repository.auth.TokenRepository
import javax.inject.Inject
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor @Inject constructor(
    private val tokenRepository: TokenRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val masterToken = MASTER_KEY

        val newRequest = request.newBuilder()
            .addHeader(AUTH_HEADER_KEY, "Bearer $masterToken")
            .build()
        return chain.proceed(newRequest)
    }

    companion object {
        const val AUTH_HEADER_KEY = "Authorization"
    }
}
