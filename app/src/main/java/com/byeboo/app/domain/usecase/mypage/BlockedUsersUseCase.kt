package com.byeboo.app.domain.usecase.mypage

import com.byeboo.app.domain.model.mypage.BlockedUsersModel
import com.byeboo.app.domain.repository.mypage.BlockedUsersRepository
import javax.inject.Inject

class BlockedUsersUseCase
    @Inject
    constructor(
        private val blockedUsersRepository: BlockedUsersRepository,
    ) {
        suspend operator fun invoke(): Result<BlockedUsersModel> = blockedUsersRepository.getBlockedUsers()
    }
