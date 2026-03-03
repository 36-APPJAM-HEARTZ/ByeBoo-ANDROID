package com.byeboo.app.domain.repository.mypage

import com.byeboo.app.domain.model.mypage.BlockedUsersModel

interface BlockedUsersRepository {
    suspend fun getBlockedUsers(): Result<BlockedUsersModel>

    suspend fun unblockUser(blockId: Long): Result<Unit>
}
