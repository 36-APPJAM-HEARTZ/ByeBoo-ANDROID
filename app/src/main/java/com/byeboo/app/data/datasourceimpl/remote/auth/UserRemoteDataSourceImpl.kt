package com.byeboo.app.data.datasourceimpl.remote.auth

import com.byeboo.app.data.datasource.remote.auth.UserRemoteDataSource
import com.byeboo.app.data.dto.base.BaseResponse
import com.byeboo.app.data.dto.request.auth.UserInfoRequestDto
import com.byeboo.app.data.dto.request.auth.UserNicknameRequestDto
import com.byeboo.app.data.dto.response.auth.UserInfoResponseDto
import com.byeboo.app.data.dto.response.auth.UserJourneyResponseDto
import com.byeboo.app.data.dto.response.auth.UserNicknameChangeResponseDto
import com.byeboo.app.data.service.auth.UserService
import javax.inject.Inject

class UserRemoteDataSourceImpl @Inject constructor(
    private val userService: UserService
) : UserRemoteDataSource {
    override suspend fun updateUserInfo(request: UserInfoRequestDto): BaseResponse<UserInfoResponseDto> {
        return userService.updateUserInfo(request)
    }

    override suspend fun getUserJourney(): BaseResponse<UserJourneyResponseDto> {
        return userService.getJourney()
    }

    override suspend fun updateUserNickname(nickname: String): BaseResponse<UserNicknameChangeResponseDto> {
        val request = UserNicknameRequestDto(name = nickname)
        return userService.updateUserNickname(request)
    }
}
