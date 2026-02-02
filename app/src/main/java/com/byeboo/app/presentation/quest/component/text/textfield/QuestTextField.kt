package com.byeboo.app.presentation.quest.component.text.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.domain.model.quest.QuestWritingState

@Composable
fun QuestTextField(
    questWritingState: QuestWritingState,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    placeholder: String = "",
    isQuestion: Boolean = true,
    onFocusChanged: ((Boolean) -> Unit)? = null
) {
    val currentCharCount = value.length
    val maxCharCount = if (isQuestion) 500 else 200
    val byebooColor = ByeBooTheme.colors
    val focusState = remember { mutableStateOf(false) }
    val textFieldRequester = remember { BringIntoViewRequester() }

    val textFieldBorderColor by remember(questWritingState, focusState.value, value) {
        derivedStateOf {
            if (focusState.value) {
                when (questWritingState) {
                    QuestWritingState.Empty, QuestWritingState.Writing -> byebooColor.primary300
                    QuestWritingState.OverLimit -> byebooColor.error300
                    QuestWritingState.Ready -> Color.Transparent
                }
            } else {
                Color.Transparent
            }
        }
    }

    val textCountColor by remember {
        derivedStateOf {
            if (focusState.value) {
                when (questWritingState) {
                    QuestWritingState.Empty, QuestWritingState.Writing -> byebooColor.primary300
                    QuestWritingState.OverLimit -> byebooColor.error300
                    QuestWritingState.Ready -> byebooColor.gray300
                }
            } else {
                byebooColor.gray300
            }
        }
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    val lastLineBottom = remember { mutableStateOf(0) }

    LaunchedEffect(value) {
        scrollState.animateScrollTo(scrollState.maxValue)
    }

    Box(
        modifier =
        modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = textFieldBorderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .background(color = ByeBooTheme.colors.whiteAlpha10)
            .padding(horizontal = screenWidthDp(24.dp), vertical = screenHeightDp(16.dp))
            .bringIntoViewRequester(textFieldRequester)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier =
                Modifier
                    .fillMaxSize()
                    .height(screenHeightDp(275.dp))
                    .verticalScroll(scrollState)
                    .onFocusChanged { focusStateChanged ->
                        focusState.value = focusStateChanged.isFocused
                        onFocusChanged?.invoke(focusStateChanged.isFocused)
                    },
                enabled = isEnabled,
                textStyle =
                ByeBooTheme.typography.body3.copy(
                    color = ByeBooTheme.colors.white
                ),
                keyboardOptions =
                KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
                keyboardActions =
                KeyboardActions(onDone = {
                    keyboardController?.hide()
                    focusManager.clearFocus()
                }),
                cursorBrush = SolidColor(ByeBooTheme.colors.white),
                decorationBox = { innerTextField ->
                    if (value.isEmpty() && !(focusState.value)) {
                        Text(
                            text = placeholder,
                            color = ByeBooTheme.colors.gray300,
                            style = ByeBooTheme.typography.body3
                        )
                    }
                    innerTextField()
                },
                onTextLayout = { layoutResult ->
                    lastLineBottom.value =
                        layoutResult.getLineBottom(layoutResult.lineCount - 1).toInt()
                }
            )

            Text(
                text =
                buildAnnotatedString {
                    append(text = "(")

                    append(text = currentCharCount.toString())

                    append(text = "/")

                    append(text = maxCharCount.toString())

                    append(text = ")")
                },
                style = ByeBooTheme.typography.body6,
                color = textCountColor,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
