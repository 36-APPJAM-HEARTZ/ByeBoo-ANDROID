package com.byeboo.app.domain.model.auth

import com.byeboo.app.core.model.auth.TokenEntity

data class AuthResult(
    val tokens: TokenEntity,
    val isRegistered: Boolean
)
