package com.byeboo.app.data.service.auth

import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.request.auth.UserInfoRequestDto
import com.byeboo.app.data.dto.response.auth.UserInfoResponseDto
import com.byeboo.app.data.dto.response.auth.UserJourneyResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH

interface UserService {
    @PATCH("/api/v1/users")
    suspend fun updateUserInfo(
        @Body request: UserInfoRequestDto
    ): BaseResponse<UserInfoResponseDto>

    @GET("/api/v1/users/journey")
    suspend fun getJourney(): BaseResponse<UserJourneyResponseDto>
}
