package com.byeboo.app.presentation.quest.component.text.textfield

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenHeightDp

@Composable
fun QuestTextField(
    value: String,
    onValueChange: (String) -> Unit,
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    placeholder: String = "",
    onFocusChanged: ((Boolean) -> Unit)? = null,
) {
    val isFocused = remember { mutableStateOf(false) }
    val lineCount = remember { mutableIntStateOf(1) }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    LaunchedEffect(isFocused.value,lineCount.intValue) {
        if (isFocused.value) {
            scrollState.animateScrollTo(scrollState.maxValue)
        }
    }

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier =
            modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = screenHeightDp(180.dp))
                .onFocusChanged { focusStateChanged ->
                    isFocused.value = focusStateChanged.isFocused
                    onFocusChanged?.invoke(focusStateChanged.isFocused)
                },
        enabled = isEnabled,
        textStyle =
            ByeBooTheme.typography.body3.copy(
                color = ByeBooTheme.colors.white,
            ),
        keyboardOptions =
            KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Default,
            ),
        keyboardActions =
            KeyboardActions(onDone = {
                keyboardController?.hide()
                focusManager.clearFocus()
            }),
        cursorBrush = SolidColor(ByeBooTheme.colors.white),
        decorationBox = { innerTextField ->
            if (value.isEmpty() && !(isFocused.value)) {
                Text(
                    text = placeholder,
                    color = ByeBooTheme.colors.gray300,
                    style = ByeBooTheme.typography.body3,
                )
            }
            innerTextField()
        },
        onTextLayout = { layoutResult ->
            lineCount.intValue = layoutResult.lineCount
        },
    )
}
