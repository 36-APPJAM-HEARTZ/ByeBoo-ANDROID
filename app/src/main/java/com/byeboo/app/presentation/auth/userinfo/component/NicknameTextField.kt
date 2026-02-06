package com.byeboo.app.presentation.auth.userinfo.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.noRippleClickable
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.auth.userinfo.model.UserInfoValidationState

@Composable
fun NicknameTextField(
    value: String,
    validationState: UserInfoValidationState,
    onValueChange: (String) -> Unit,
    onClearClick: () -> Unit,
    modifier: Modifier = Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    focusRequester: FocusRequester? = null,
    showValidMessage: Boolean = true,
) {
    val focusState = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val shape = remember { RoundedCornerShape(12.dp) }

    val borderColor =
        if (focusState.value) {
            when (validationState) {
                UserInfoValidationState.Valid -> ByeBooTheme.colors.primary300
                UserInfoValidationState.Invalid -> ByeBooTheme.colors.error300
                UserInfoValidationState.Empty -> ByeBooTheme.colors.whiteAlpha10
            }
        } else {
            Color.Transparent
        }
    val guideColor =
        when (validationState) {
            UserInfoValidationState.Valid -> ByeBooTheme.colors.primary300
            UserInfoValidationState.Invalid -> ByeBooTheme.colors.error300
            UserInfoValidationState.Empty -> ByeBooTheme.colors.gray400
        }

    var cursorText by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(value, selection = TextRange(value.length)))
    }

    LaunchedEffect(value) {
        if (cursorText.text != value) {
            val newSelEnd = minOf(value.length, cursorText.selection.end)
            cursorText = cursorText.copy(text = value, selection = TextRange(newSelEnd))
        }
    }
    Column(modifier = modifier.padding(vertical = screenHeightDp(8.dp))) {
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .border(1.dp, borderColor, shape)
                    .clip(shape)
                    .background(ByeBooTheme.colors.whiteAlpha10)
                    .padding(horizontal = screenWidthDp(24.dp), vertical = screenHeightDp(18.dp)),
            contentAlignment = Alignment.Center,
        ) {
            BasicTextField(
                value = cursorText.copy(text = value),
                onValueChange = { newValue ->
                    cursorText = newValue
                    onValueChange(newValue.text)
                },
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .then(
                            if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier,
                        ).onFocusChanged { focus ->
                            focusState.value = focus.isFocused
                            if (focus.isFocused && cursorText.composition == null) {
                                val end = cursorText.text.length
                                if (cursorText.selection.end != end) {
                                    cursorText = cursorText.copy(selection = TextRange(end))
                                }
                            }
                        },
                textStyle = ByeBooTheme.typography.body3.copy(color = ByeBooTheme.colors.white),
                singleLine = true,
                cursorBrush = SolidColor(ByeBooTheme.colors.white),
                visualTransformation = VisualTransformation.None,
                keyboardOptions = keyboardOptions,
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.CenterStart,
                        ) {
                            if (value.isEmpty()) {
                                Text(
                                    text = "닉네임을 입력해주세요",
                                    style = ByeBooTheme.typography.body3,
                                    color = ByeBooTheme.colors.gray300,
                                )
                            }
                            innerTextField()
                        }
                    }
                },
            )

            if (value.isNotEmpty() && focusState.value) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_delete),
                    contentDescription = "Clear text",
                    tint = Color.Unspecified,
                    modifier =
                        Modifier
                            .align(Alignment.CenterEnd)
                            .noRippleClickable { onClearClick() },
                )
            } else {
                Spacer(modifier = Modifier.size(25.dp))
            }
        }

        Spacer(modifier = Modifier.padding(bottom = screenHeightDp(16.dp)))

        when (validationState) {
            UserInfoValidationState.Valid ->
                if (showValidMessage) {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "* 설정 가능한 닉네임이에요!",
                            style = ByeBooTheme.typography.cap2,
                            color = guideColor,
                            modifier = Modifier.weight(1f),
                        )

                        Text(
                            text = "${value.length}/5",
                            style = ByeBooTheme.typography.cap2,
                            color = guideColor,
                        )
                    }
                }

            UserInfoValidationState.Invalid -> {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_error),
                        contentDescription = "에러",
                        tint = Color.Unspecified,
                        modifier =
                            Modifier
                                .padding(
                                    horizontal = screenWidthDp(2.dp),
                                    vertical = screenHeightDp(2.dp),
                                ).size(12.dp),
                    )

                    Text(
                        text = "2자 이상 · 공백 제외 · 영어 숫자 한글 구성",
                        style = ByeBooTheme.typography.cap2,
                        color = guideColor,
                        modifier =
                            Modifier
                                .padding(start = screenWidthDp(3.dp))
                                .weight(1f),
                    )

                    Text(
                        text = "${value.length}/5",
                        style = ByeBooTheme.typography.cap2,
                        color = guideColor,
                    )
                }
            }

            UserInfoValidationState.Empty -> {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_default_error),
                        contentDescription = "기본",
                        tint = Color.Unspecified,
                        modifier =
                            Modifier
                                .padding(
                                    horizontal = screenWidthDp(2.dp),
                                    vertical = screenHeightDp(2.dp),
                                ).size(12.dp),
                    )

                    Text(
                        text = "2자 이상 · 공백 제외 · 영어 숫자 한글 구성",
                        style = ByeBooTheme.typography.cap2,
                        color = guideColor,
                        modifier =
                            Modifier
                                .padding(start = screenWidthDp(3.dp))
                                .weight(1f),
                    )

                    Text(
                        text = "${value.length}/5",
                        style = ByeBooTheme.typography.cap2,
                        color = guideColor,
                    )
                }
            }
        }
    }
}
