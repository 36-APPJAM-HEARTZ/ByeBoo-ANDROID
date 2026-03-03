package com.byeboo.app.data.service.mypage

import com.byeboo.app.data.dto.base.NullableBaseResponse
import com.byeboo.app.data.dto.response.mypage.BlockedUsersResponseDto
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Path

interface BlockedUsersService {
    @GET("/api/v1/blocks")
    suspend fun getBlockedUsers(): NullableBaseResponse<BlockedUsersResponseDto>

    @DELETE("/api/v1/blocks/{blockId}")
    suspend fun unblockUser(
        @Path("blockId") blockId: Long,
    ): NullableBaseResponse<Unit>
}
