package com.byeboo.app.presentation.auth.userinfo

import com.byeboo.app.domain.model.auth.NicknameValidationResult
import com.byeboo.app.domain.model.auth.QuestStyle

data class UserInfoState(
    val nickname: String = "",
    val nicknameValidation: NicknameValidationResult = NicknameValidationResult.Empty,
    val selectedQuest: QuestStyle? = null,
    val currentStep: Int = 0,
)

sealed interface UserInfoSideEffect {
    data object NavigateToLoading : UserInfoSideEffect

    data class ShowSnackBar(
        val message: String,
    ) : UserInfoSideEffect
}
