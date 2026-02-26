package com.byeboo.app.presentation.mypage

import androidx.compose.runtime.Immutable
import com.byeboo.app.core.designsystem.type.CustomSnackBarType

@Immutable
data class MyPageState(
    val nickname: String = "",
    val isAlarmEnabled: Boolean? = null,
    val showLogoutModal: Boolean = false,
    val showDeleteAccountModal: Boolean = false,
    val showPermissionModal: Boolean = false,
)

enum class ModalType {
    LOGOUT,
    DELETE_ACCOUNT,
    PERMISSION,
}

sealed interface MyPageSideEffect {
    data class OpenUrl(
        val url: String,
    ) : MyPageSideEffect

    data object NavigateToEditProfile : MyPageSideEffect

    data object NavigateToOffboardingCompletedJourney : MyPageSideEffect

    data object NavigateToTutorial : MyPageSideEffect

    data object NavigateToSplash : MyPageSideEffect

    data object RequestNotificationPermission : MyPageSideEffect

    data object NavigateToSetting : MyPageSideEffect

    data object NavigateToBlockedUsers : MyPageSideEffect

    data class ShowSnackBar(
        val message: String,
        val iconType: CustomSnackBarType,
    ) : MyPageSideEffect
}
