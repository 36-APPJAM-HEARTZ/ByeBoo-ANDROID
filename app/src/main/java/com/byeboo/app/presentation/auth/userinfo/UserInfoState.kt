package com.byeboo.app.presentation.auth.userinfo

import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.domain.model.auth.Feeling
import com.byeboo.app.domain.model.auth.NicknameValidationResult
import com.byeboo.app.domain.model.auth.QuestStyle
import com.byeboo.app.presentation.quest.component.type.OptionType

data class UserInfoState(
    val nickname: String = "",
    val nicknameValidation: NicknameValidationResult = NicknameValidationResult.Empty,
    val selectedEmotion: Feeling? = null,
    val selectedQuest: QuestStyle? = null,
    val currentStep: Int = 0,
)

sealed interface UserInfoSideEffect {
    data object NavigateToLoading : UserInfoSideEffect

    data class ShowSnackBar(
        val message: String,
        val iconType: CustomSnackBarType,
    ) : UserInfoSideEffect
}
