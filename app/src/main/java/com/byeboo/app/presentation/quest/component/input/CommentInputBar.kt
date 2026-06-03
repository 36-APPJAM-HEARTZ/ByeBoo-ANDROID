package com.byeboo.app.presentation.quest.component.input

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.noRippleClickable
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp

@Composable
fun CommentInputBar(
    commentText: String,
    isKeyboardVisible: Boolean,
    isKeyboardVisibleDelayed: Boolean,
    focusRequester: FocusRequester,
    isCompleteEnabled: Boolean,
    maxLength: Int,
    onTextChange: (String) -> Unit,
    onCompleteClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "댓글로 위로를 남겨보세요.",
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .background(ByeBooTheme.colors.background),
    ) {
        HorizontalDivider(
            thickness = 1.dp,
            color = ByeBooTheme.colors.gray800,
        )

        // 키보드 올라온 상태
        if (isKeyboardVisible) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = screenWidthDp(24.dp),
                            vertical = screenHeightDp(8.dp),
                        ),
            ) {
                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .defaultMinSize(minHeight = screenHeightDp(42.dp)),
                ) {
                    BasicTextField(
                        value = commentText,
                        onValueChange = onTextChange,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(align = Alignment.Top, unbounded = true)
                                .focusRequester(focusRequester),
                        textStyle =
                            ByeBooTheme.typography.body6.copy(
                                color = ByeBooTheme.colors.gray100,
                            ),
                        cursorBrush = SolidColor(ByeBooTheme.colors.white),
                        minLines = 1,
                        maxLines = 5,
                        keyboardOptions =
                            KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Default,
                            ),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.TopStart,
                            ) {
                                if (commentText.isEmpty()) {
                                    Text(
                                        text = placeholder,
                                        style = ByeBooTheme.typography.body6,
                                        color = ByeBooTheme.colors.gray600,
                                    )
                                }
                                innerTextField()
                            }
                        },
                    )
                }

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = screenHeightDp(4.dp)),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "${commentText.length}/$maxLength",
                        style = ByeBooTheme.typography.cap2,
                        color = ByeBooTheme.colors.gray400,
                    )
                    Spacer(modifier = Modifier.width(screenWidthDp(12.dp)))
                    Text(
                        text = "완료",
                        style = ByeBooTheme.typography.body2,
                        color =
                            if (isCompleteEnabled) {
                                ByeBooTheme.colors.primary300
                            } else {
                                ByeBooTheme.colors.gray600
                            },
                        modifier =
                            if (isCompleteEnabled) {
                                Modifier.noRippleClickable(onClick = onCompleteClick)
                            } else {
                                Modifier
                            },
                    )
                }
            }
        } else {
            // 키보드 내려간 상태
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = screenWidthDp(24.dp))
                        .padding(
                            top = screenHeightDp(8.dp),
                            bottom = screenHeightDp(2.dp),
                        ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(screenWidthDp(20.dp)),
            ) {
                Box(
                    modifier =
                        Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(ByeBooTheme.colors.whiteAlpha5)
                            .padding(
                                horizontal = screenWidthDp(12.dp),
                                vertical = screenHeightDp(9.5.dp),
                            ).noRippleClickable { focusRequester.requestFocus() },
                ) {
                    if (commentText.isNotEmpty()) {
                        Text(
                            text = commentText,
                            style =
                                ByeBooTheme.typography.body6.copy(
                                    color = ByeBooTheme.colors.gray100,
                                ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    } else {
                        Text(
                            text = placeholder,
                            style = ByeBooTheme.typography.body6,
                            color = ByeBooTheme.colors.gray600,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    BasicTextField(
                        value = commentText,
                        onValueChange = onTextChange,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(0.dp)
                                .focusRequester(focusRequester),
                        textStyle =
                            ByeBooTheme.typography.body6.copy(
                                color = ByeBooTheme.colors.gray100,
                            ),
                        cursorBrush = SolidColor(ByeBooTheme.colors.white),
                        keyboardOptions =
                            KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Default,
                            ),
                    )
                }

                Text(
                    text = "완료",
                    style = ByeBooTheme.typography.body2,
                    color =
                        if (isCompleteEnabled) {
                            ByeBooTheme.colors.primary300
                        } else {
                            ByeBooTheme.colors.gray600
                        },
                    modifier =
                        if (isCompleteEnabled) {
                            Modifier.noRippleClickable(onClick = onCompleteClick)
                        } else {
                            Modifier
                        },
                )
            }
        }
    }
}
