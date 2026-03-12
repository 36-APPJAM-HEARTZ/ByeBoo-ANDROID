package com.byeboo.app.presentation.auth.userinfo

import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.core.model.quest.JourneyType
import com.byeboo.app.domain.model.auth.NicknameValidationResult

data class UserInfoState(
    val nickname: String = "",
    val nicknameValidation: NicknameValidationResult = NicknameValidationResult.Empty,
    val selectedQuest: JourneyType? = null,
    val currentStep: Int = 0,
)

sealed interface UserInfoSideEffect {
    data object NavigateToLoading : UserInfoSideEffect

    data object NavigateToNextPage : UserInfoSideEffect

    data class ShowSnackBar(
        val snackBarType: CustomSnackBarType,
    ) : UserInfoSideEffect
}
