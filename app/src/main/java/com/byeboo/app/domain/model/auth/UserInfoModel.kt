package com.byeboo.app.domain.model.auth

data class UserInfoModel(
    val name: String,
    val questStyle: String,
)

data class UserJourney(
    val journey: String,
    val description: String,
)
