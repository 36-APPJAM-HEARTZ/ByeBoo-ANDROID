package com.byeboo.app.data.datasource.remote.mypage

import com.byeboo.app.data.dto.base.NullableBaseResponse
import com.byeboo.app.data.dto.response.mypage.BlockedUsersResponseDto

interface BlockedUsersDataSource {
    suspend fun getBlockedUsers(): NullableBaseResponse<BlockedUsersResponseDto>
}
