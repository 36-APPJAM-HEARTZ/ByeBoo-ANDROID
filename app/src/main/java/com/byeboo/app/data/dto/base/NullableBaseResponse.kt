package com.byeboo.app.data.dto.base

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NullableBaseResponse<T>(
    @SerialName("success")
    val success: Boolean,
    @SerialName("message")
    val message: String,
    @SerialName("data")
    val data: T? = null,
)
