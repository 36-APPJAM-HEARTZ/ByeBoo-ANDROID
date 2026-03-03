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
                blockedUsersDataSource.getBlockedUsers().data?.toDomain() ?: throw IllegalStateException()
            }
    }
