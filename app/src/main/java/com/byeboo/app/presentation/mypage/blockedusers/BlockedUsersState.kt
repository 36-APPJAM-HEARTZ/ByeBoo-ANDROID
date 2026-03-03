package com.byeboo.app.presentation.mypage.blockedusers

import androidx.compose.runtime.Immutable
import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.domain.model.mypage.BlockedUserModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Immutable
data class BlockedUsersState(
    val blockedUserLists: ImmutableList<BlockedUserModel> = persistentListOf(),
    val showBlockedModal: Boolean = false,
)

sealed interface BlockedUsersSideEffect {
    data object NavigateUp : BlockedUsersSideEffect

    data class ShowSnackBar(
        val snackBarType: CustomSnackBarType,
    ) : BlockedUsersSideEffect
}
