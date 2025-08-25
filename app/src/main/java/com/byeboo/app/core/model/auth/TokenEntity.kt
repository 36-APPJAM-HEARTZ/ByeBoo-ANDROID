package com.byeboo.app.core.model.auth

data class TokenEntity(
    val accessToken: String,
    val refreshToken: String
)
