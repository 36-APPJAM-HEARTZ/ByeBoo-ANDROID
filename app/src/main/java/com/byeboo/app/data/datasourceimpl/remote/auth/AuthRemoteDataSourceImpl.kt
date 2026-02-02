package com.byeboo.app.data.datasourceimpl.remote.auth

import com.byeboo.app.data.datasource.remote.auth.AuthRemoteDataSource
import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.base.NullableBaseResponse
import com.byeboo.app.data.dto.request.auth.KakaoLoginRequestDto
import com.byeboo.app.data.dto.response.auth.KakaoLoginResponseDto
import com.byeboo.app.data.dto.response.auth.TokenReissueResponseDto
import com.byeboo.app.data.service.auth.AuthService
import javax.inject.Inject

class AuthRemoteDataSourceImpl
@Inject
constructor(
    private val authService: AuthService
) : AuthRemoteDataSource {
    override suspend fun loginWithKakao(
        token: String,
        platform: String
    ): BaseResponse<KakaoLoginResponseDto> =
        authService.loginWithKakao(
            authorization = "$BEARER $token",
            request = KakaoLoginRequestDto(platform = platform)
        )

    override suspend fun reissueAccessToken(refreshToken: String): BaseResponse<TokenReissueResponseDto> =
        authService.reissueAccessToken("$BEARER $refreshToken")

    override suspend fun logoutAccount(token: String): NullableBaseResponse<Unit> = authService.logoutAccount(
        "$BEARER $token"
    )

    override suspend fun withdrawAccount(token: String): NullableBaseResponse<Unit> = authService.withdrawAccount(
        "$BEARER $token"
    )

    companion object {
        private const val BEARER = "Bearer"
    }
}
