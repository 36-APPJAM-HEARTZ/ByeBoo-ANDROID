package com.byeboo.app.data.service.auth

import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.request.auth.KakaoLoginRequestDto
import com.byeboo.app.data.dto.response.auth.KakaoLoginResponseDto
import com.byeboo.app.data.dto.response.auth.TokenReissueResponseDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {
    @POST("/api/v1/auth/login")
    suspend fun loginWithKakao(
        @Header("Authorization") authorization: String,
        @Body request: KakaoLoginRequestDto
    ): BaseResponse<KakaoLoginResponseDto>

    @POST("/api/v1/auth/reissue")
    suspend fun reissueAccessToken(
        @Header("Authorization") authorization: String
    ): BaseResponse<TokenReissueResponseDto>

    @DELETE("/api/v1/auth/logout")
    suspend fun logoutAccount(): BaseResponse<Unit>

    @DELETE("/api/v1/auth/withdraw")
    suspend fun withdrawAccount(): BaseResponse<Unit>
}