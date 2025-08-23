package com.byeboo.app.presentation.mypage.editprofile

import com.byeboo.app.domain.model.auth.NicknameValidationResult
import com.byeboo.app.presentation.auth.userinfo.model.UserInfoValidationState

data class EditProfileState(
    val nickname: String = "",
    val initialNickname: String = "",
    val nicknameValidation: NicknameValidationResult = NicknameValidationResult.Valid,
    val isPristine: Boolean = true
)

sealed interface EditProfileSideEffect {
    data class NavigateToMyPage(val nickname: String): EditProfileSideEffect
}