package com.byeboo.app.presentation.mypage.editprofile

import androidx.compose.foundation.background
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.nativeKeyCode
import androidx.compose.ui.input.key.onPreInterceptKeyBeforeSoftKeyboard
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.component.button.ByeBooActivationButton
import com.byeboo.app.core.designsystem.event.LocalSnackBarTrigger
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.noRippleClickable
import com.byeboo.app.domain.model.auth.NicknameValidationResult
import com.byeboo.app.presentation.auth.userinfo.component.NicknameTextField
import com.byeboo.app.presentation.auth.userinfo.model.toValidationState
import kotlinx.coroutines.delay

@Composable
fun EditProfileRoute(
    navigateToMyPage: () -> Unit,
    bottomPadding: Dp,
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current
    val showSnackBar = LocalSnackBarTrigger.current

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is EditProfileSideEffect.NavigateToMyPage -> navigateToMyPage()
                is EditProfileSideEffect.ShowSnackBar -> showSnackBar(effect.message)
            }
        }
    }

    LaunchedEffect(Unit) {
        delay(100)
        focusRequester.requestFocus()
        keyboard?.show()
    }

    EditProfileScreen(
        uiState = uiState,
        bottomPadding = bottomPadding,
        onBackClick = viewModel::onBackClicked,
        onNicknameChange = viewModel::updateNickname,
        onClearClick = { viewModel.updateNickname("") },
        focusRequester = focusRequester,
        onCompleteClick = { viewModel.finishEditProfile(it) }
    )
}

@Composable
private fun EditProfileScreen(
    uiState: EditProfileState,
    bottomPadding: Dp,
    onBackClick: () -> Unit,
    onNicknameChange: (String) -> Unit,
    onClearClick: () -> Unit,
    focusRequester: FocusRequester,
    onCompleteClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current
    val isNicknameValid = uiState.nicknameValidation == NicknameValidationResult.Valid
    val showValidMessage = !uiState.isInitial
    val isFocused = remember { mutableStateOf(false) }

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
                    .noRippleClickable(onClick = onBackClick)
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
            onClearClick = onClearClick,
            showValidMessage = showValidMessage,
            modifier = Modifier
                .focusRequester(focusRequester)
                .onPreInterceptKeyBeforeSoftKeyboard { event ->
                    if (event.key.nativeKeyCode == android.view.KeyEvent.KEYCODE_BACK) {
                        focusManager.clearFocus(force = true)
                        isFocused.value = false
                        true
                    } else {
                        false
                    }
                }
        )

        Spacer(modifier = Modifier.weight(1f))

        ByeBooActivationButton(
            buttonDisableColor = ByeBooTheme.colors.whiteAlpha10,
            buttonText = "완료",
            buttonDisableTextColor = ByeBooTheme.colors.gray300,
            isEnabled = isNicknameValid,
            onClick = { onCompleteClick(uiState.nickname) }
        )
    }
}
