package com.byeboo.app.presentation.mypage.editprofile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.nativeKeyCode
import androidx.compose.ui.input.key.onPreInterceptKeyBeforeSoftKeyboard
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.byeboo.app.core.designsystem.component.button.ByeBooActivationButton
import com.byeboo.app.core.designsystem.component.topbar.BackTopbar
import com.byeboo.app.core.designsystem.event.LocalSnackBarTrigger
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.domain.model.auth.NicknameValidationResult
import com.byeboo.app.presentation.auth.userinfo.component.NicknameTextField
import com.byeboo.app.presentation.auth.userinfo.model.toValidationState
import kotlinx.coroutines.delay

@Composable
fun EditProfileRoute(
    navigateToMyPage: () -> Unit,
    paddingValues: PaddingValues,
    viewModel: EditProfileViewModel = hiltViewModel(),
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
        paddingValues = paddingValues,
        onBackClick = viewModel::onBackClicked,
        onNicknameChange = viewModel::updateNickname,
        onClearClick = { viewModel.updateNickname("") },
        focusRequester = focusRequester,
        onCompleteClick = { viewModel.finishEditProfile(it) },
    )
}

@Composable
private fun EditProfileScreen(
    uiState: EditProfileState,
    paddingValues: PaddingValues,
    onBackClick: () -> Unit,
    onNicknameChange: (String) -> Unit,
    onClearClick: () -> Unit,
    focusRequester: FocusRequester,
    onCompleteClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val focusManager = LocalFocusManager.current
    val isNicknameValid = uiState.nicknameValidation == NicknameValidationResult.Valid
    val showValidMessage = !uiState.isInitial
    val isFocused = remember { mutableStateOf(false) }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(ByeBooTheme.colors.background)
                .padding(horizontal = screenWidthDp(24.dp))
                .padding(
                    top = paddingValues.calculateTopPadding() + screenHeightDp(43.dp),
                    bottom =
                        screenHeightDp(
                            paddingValues.calculateBottomPadding() + screenHeightDp(10.dp),
                        ),
                ),
    ) {
        BackTopbar(
            onBackClick = onBackClick,
            title = "프로필 수정",
        )

        Spacer(modifier = Modifier.height(screenHeightDp(8.dp)))

        Text(
            text = "닉네임",
            style = ByeBooTheme.typography.body1,
            color = ByeBooTheme.colors.gray300,
        )

        Spacer(modifier = Modifier.height(screenHeightDp(8.dp)))

        NicknameTextField(
            value = uiState.nickname,
            validationState = uiState.nicknameValidation.toValidationState(),
            onValueChange = onNicknameChange,
            onClearClick = onClearClick,
            showValidMessage = showValidMessage,
            modifier =
                Modifier
                    .focusRequester(focusRequester)
                    .onPreInterceptKeyBeforeSoftKeyboard { event ->
                        if (event.key.nativeKeyCode == android.view.KeyEvent.KEYCODE_BACK) {
                            focusManager.clearFocus(force = true)
                            isFocused.value = false
                            true
                        } else {
                            false
                        }
                    },
        )

        Spacer(modifier = Modifier.weight(1f))

        ByeBooActivationButton(
            buttonDisableColor = ByeBooTheme.colors.whiteAlpha5,
            buttonText = "완료하기",
            buttonDisableTextColor = ByeBooTheme.colors.gray300,
            isEnabled = isNicknameValid,
            onClick = { onCompleteClick(uiState.nickname) },
        )
    }
}
