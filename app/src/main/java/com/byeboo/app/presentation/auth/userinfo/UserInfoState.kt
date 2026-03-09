package com.byeboo.app.presentation.auth.userinfo

import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.domain.model.auth.Feeling
import com.byeboo.app.domain.model.auth.NicknameValidationResult
import com.byeboo.app.domain.model.auth.OnboardingQuestStyle

data class UserInfoState(
    val nickname: String = "",
    val nicknameValidation: NicknameValidationResult = NicknameValidationResult.Empty,
    val selectedQuest: OnboardingQuestStyle? = null,
    val currentStep: Int = 0,
)

sealed interface UserInfoSideEffect {
    data object NavigateToLoading : UserInfoSideEffect

    data class ShowSnackBar(
        val snackBarType: CustomSnackBarType,
    ) : UserInfoSideEffect
}
