package com.byeboo.app.presentation.mypage.blockedusers

import com.byeboo.app.presentation.mypage.type.User
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class BlockedUsersState(
    val userLists: ImmutableList<User> = persistentListOf(),
)
