package com.byeboo.app.data.repositoryimpl.mypage

import com.byeboo.app.data.datasource.remote.mypage.BlockedUsersDataSource
import com.byeboo.app.data.mapper.mypage.toDomain
import com.byeboo.app.domain.model.mypage.BlockedUsersModel
import com.byeboo.app.domain.repository.mypage.BlockedUsersRepository
import javax.inject.Inject

class BlockedUsersRepositoryImpl
    @Inject
    constructor(
        private val blockedUsersDataSource: BlockedUsersDataSource,
    ) : BlockedUsersRepository {
        override suspend fun getBlockedUsers(): Result<BlockedUsersModel> =
            runCatching {
                blockedUsersDataSource.getBlockedUsers().data?.toDomain()
                    ?: throw IllegalStateException()
            }

        override suspend fun unblockUser(blockId: Long): Result<Unit> =
            runCatching {
                val response = blockedUsersDataSource.unblockUser(blockId)

                if (!response.success) {
                    throw IllegalStateException(response.message)
                }

                Unit
            }
    }
