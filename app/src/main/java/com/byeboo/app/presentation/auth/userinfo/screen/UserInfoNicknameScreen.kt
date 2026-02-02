package com.byeboo.app.presentation.auth.userinfo.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.byeboo.app.core.designsystem.component.text.DescriptionText
import com.byeboo.app.presentation.auth.userinfo.component.NicknameTextField
import com.byeboo.app.presentation.auth.userinfo.model.UserInfoValidationState

@Composable
fun UserInfoNicknameScreen(
    nickname: String,
    validationState: UserInfoValidationState,
    onTextChange: (String) -> Unit,
) {
    Column {
        DescriptionText(
            title = "닉네임",
            guideText = "을 입력해 주세요",
            contentText = "어떤 이름으로 불러드릴까요?",
            bottom = 20.dp,
        )
        NicknameTextField(
            value = nickname,
            validationState = validationState,
            onValueChange = onTextChange,
            onClearClick = { onTextChange("") },
        )
    }
}
