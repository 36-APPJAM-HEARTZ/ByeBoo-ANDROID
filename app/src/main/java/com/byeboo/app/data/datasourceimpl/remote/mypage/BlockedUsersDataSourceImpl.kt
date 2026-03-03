package com.byeboo.app.data.datasourceimpl.remote.mypage

import com.byeboo.app.data.datasource.remote.mypage.BlockedUsersDataSource
import com.byeboo.app.data.dto.base.NullableBaseResponse
import com.byeboo.app.data.dto.response.mypage.BlockedUsersResponseDto
import com.byeboo.app.data.service.mypage.BlockedUsersService
import javax.inject.Inject

class BlockedUsersDataSourceImpl
    @Inject
    constructor(
        private val blockedUsersService: BlockedUsersService,
    ) : BlockedUsersDataSource {
        override suspend fun getBlockedUsers(): NullableBaseResponse<BlockedUsersResponseDto> = blockedUsersService.getBlockedUsers()

        override suspend fun unblockUser(blockId: Long): NullableBaseResponse<Unit> = blockedUsersService.unblockUser(blockId)
    }
