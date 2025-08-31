package com.byeboo.app.data.dto.request.auth

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KakaoLoginRequestDto(
    @SerialName("platform")
    val platform: String
)