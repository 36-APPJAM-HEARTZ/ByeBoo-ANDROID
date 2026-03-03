package com.byeboo.app.domain.usecase.mypage

import com.byeboo.app.domain.repository.mypage.BlockedUsersRepository
import javax.inject.Inject
import kotlin.Result

class UnblockUserUseCase
    @Inject
    constructor(
        private val blockedUsersRepository: BlockedUsersRepository,
    ) {
        suspend operator fun invoke(blockId: Long): Result<Unit> = blockedUsersRepository.unblockUser(blockId)
    }
