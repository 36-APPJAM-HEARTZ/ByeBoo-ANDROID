package com.byeboo.app.data.service.mypage

import com.byeboo.app.data.dto.base.NullableBaseResponse
import com.byeboo.app.data.dto.response.mypage.BlockedUsersResponseDto
import retrofit2.http.GET

interface BlockedUsersService {
    @GET("/api/v1/blocks")
    suspend fun getBlockedUsers(): NullableBaseResponse<BlockedUsersResponseDto>
}
