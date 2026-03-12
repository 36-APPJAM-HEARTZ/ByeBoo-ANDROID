package com.byeboo.app.data.dto.request.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfoRequestDto(
    @SerialName("name")
    val name: String,
    @SerialName("questStyle")
    val questStyle: String,
)
