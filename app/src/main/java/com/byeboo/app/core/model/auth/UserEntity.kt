package com.byeboo.app.core.model.auth

data class UserEntity(
    val userId: Long? = 0,
    val nickname: String? = null,
    val isLoggedIn: Boolean = false,
)
