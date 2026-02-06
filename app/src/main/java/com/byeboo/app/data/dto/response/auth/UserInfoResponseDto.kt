package com.byeboo.app.data.dto.response.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoResponseDto(
    @SerialName("id")
    val id: Long,
    @SerialName("name")
    val name: String,
)
