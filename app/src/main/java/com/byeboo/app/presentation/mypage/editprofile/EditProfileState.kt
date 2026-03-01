package com.byeboo.app.presentation.mypage.editprofile

import com.byeboo.app.core.designsystem.type.CustomSnackBarType
import com.byeboo.app.domain.model.auth.NicknameValidationResult

data class EditProfileState(
    val nickname: String = "",
    val initialNickname: String = "",
    val nicknameValidation: NicknameValidationResult = NicknameValidationResult.Valid,
    val isInitial: Boolean = true,
)

sealed interface EditProfileSideEffect {
    data class NavigateToMyPage(
        val nickname: String,
    ) : EditProfileSideEffect

    data class ShowSnackBar(
        val snackBarType: CustomSnackBarType,
    ) : EditProfileSideEffect
}
