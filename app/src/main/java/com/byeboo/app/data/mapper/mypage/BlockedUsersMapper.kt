package com.byeboo.app.data.mapper.mypage

import com.byeboo.app.data.dto.response.mypage.BlockedUserDto
import com.byeboo.app.data.dto.response.mypage.BlockedUsersResponseDto
import com.byeboo.app.domain.model.mypage.BlockedUserModel
import com.byeboo.app.domain.model.mypage.BlockedUsersModel

fun BlockedUsersResponseDto.toDomain(): BlockedUsersModel =
    BlockedUsersModel(
        blockedUsers = blockList.map { it.toDomain() },
    )

fun BlockedUserDto.toDomain(): BlockedUserModel =
    BlockedUserModel(
        blockedUserName = this.name,
        blockedUserId = this.blockId,
    )
