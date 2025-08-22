package com.byeboo.app.presentation.mypage.editprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.component.button.ByeBooActivationButton
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.domain.model.auth.NicknameValidationResult
import com.byeboo.app.presentation.auth.userinfo.component.NicknameTextField

@Composable
fun EditProfileRoute(
    navigateToMyPage: () -> Unit,
    bottomPadding: Dp,
    modifier: Modifier = Modifier,
    viewModel: EditProfileViewModel = hiltViewModel()
){

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect {
            if (it is EditProfileSideEffect.NavigateToMyPage) {
                navigateToMyPage()
            }
        }
    }

    EditProfileScreen(
        uiState = uiState,
        bottomPadding = bottomPadding,
        onNicknameChange= viewModel::updateNickname,
        onBackClick = viewModel::onBackClicked,
        onCompleteClick = viewModel::finishEditProfile
    )
}

@Composable
private fun EditProfileScreen(
    uiState: EditProfileState,
    bottomPadding: Dp,
    onNicknameChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onCompleteClick: () -> Unit,
    modifier: Modifier = Modifier
){
    val isNicknameValid = uiState.nicknameValidation == NicknameValidationResult.Valid

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = ByeBooTheme.colors.black)
            .padding(horizontal = 24.dp)
            .padding(top = 67.dp, bottom = bottomPadding)

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_left),
                contentDescription = "",
                tint = ByeBooTheme.colors.gray50,
                modifier = Modifier
                    .clickable(onClick = onBackClick)
            )

            Text(
                text = "프로필 수정",
                style = ByeBooTheme.typography.sub1,
                color = ByeBooTheme.colors.white,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "닉네임",
            style = ByeBooTheme.typography.body1,
            color = ByeBooTheme.colors.gray300
        )

        Spacer(modifier = Modifier.height(8.dp))

        NicknameTextField(
            value = uiState.nickname,
            validationState = uiState.nicknameValidation.toValidationState(),
            onValueChange = onNicknameChange,
            onClearClick = { onNicknameChange("") }
        )

        Spacer(modifier = Modifier.weight(1f))

        ByeBooActivationButton(
            buttonDisableColor = ByeBooTheme.colors.whiteAlpha10,
            buttonText = "완료",
            buttonDisableTextColor = ByeBooTheme.colors.gray300,
            isEnabled = isNicknameValid,
            onClick = onCompleteClick

        )

    }
}


@Preview
@Composable
private fun EditProfileScreenPreview() {
    ByeBooTheme {
        EditProfileScreen(
            uiState = EditProfileState(),
            bottomPadding = 0.dp,
            onNicknameChange = {},
            onBackClick = {},
            onCompleteClick = {}
        )
    }
}