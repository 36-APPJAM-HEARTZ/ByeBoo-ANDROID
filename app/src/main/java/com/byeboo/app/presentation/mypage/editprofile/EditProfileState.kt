package com.byeboo.app.presentation.mypage.editprofile

import com.byeboo.app.domain.model.auth.NicknameValidationResult
import com.byeboo.app.presentation.auth.userinfo.model.UserInfoValidationState

data class EditProfileState(
    val nickname: String ="",
    val nicknameValidation: NicknameValidationResult = NicknameValidationResult.Valid,

)


fun NicknameValidationResult.toValidationState(): UserInfoValidationState {
    return when (this) {
        is NicknameValidationResult.Valid -> UserInfoValidationState.Valid
        is NicknameValidationResult.Invalid -> UserInfoValidationState.Invalid
        is NicknameValidationResult.Empty -> UserInfoValidationState.Empty
    }
}

sealed interface EditProfileSideEffect {
    data class NavigateToMyPage(val nickname: String): EditProfileSideEffect
}