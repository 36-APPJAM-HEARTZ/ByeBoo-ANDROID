package com.byeboo.app.data.dto.response.mypage

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BlockedUsersResponseDto(
    @SerialName("blockList")
    val blockList: List<BlockedUserDto>,
)

@Serializable
data class BlockedUserDto(
    @SerialName("name")
    val name: String,
    @SerialName("blockId")
    val blockId: Long,
)
