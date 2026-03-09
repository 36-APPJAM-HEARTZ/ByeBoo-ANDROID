package com.byeboo.app.domain.model.mypage

data class BlockedUsersModel(
    val blockedUsers: List<BlockedUserModel>,
)

data class BlockedUserModel(
    val blockedUserName: String,
    val blockedUserId: Long,
)
