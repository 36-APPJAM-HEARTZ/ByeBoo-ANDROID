package com.byeboo.app.data.datasource.remote.auth

import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.response.auth.KakaoLoginResponseDto
import com.byeboo.app.data.dto.response.auth.TokenReissueResponseDto

interface AuthRemoteDataSource {
    suspend fun loginWithKakao(token: String, platform: String): BaseResponse<KakaoLoginResponseDto>
    suspend fun reissueAccessToken(refreshToken: String): BaseResponse<TokenReissueResponseDto>
}