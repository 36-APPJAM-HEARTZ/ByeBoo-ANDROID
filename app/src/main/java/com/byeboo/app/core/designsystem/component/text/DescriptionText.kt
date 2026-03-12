package com.byeboo.app.core.designsystem.component.text

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp

@Composable
fun DescriptionText(
    nicknameText: String? = null,
    title: String,
    guideText: String,
    contentText: String,
    modifier: Modifier = Modifier,
    top: Dp = 0.dp,
    bottom: Dp = 0.dp,
    start: Dp = 0.dp,
    end: Dp = 0.dp,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(
                    top = screenHeightDp(top),
                    bottom = screenHeightDp(bottom),
                    start = screenWidthDp(start),
                    end = screenWidthDp(end),
                ),
    ) {
        Text(
            text =
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = ByeBooTheme.colors.gray50)) {
                        if (!nicknameText.isNullOrBlank()) {
                            append(nicknameText)
                            append("\n")
                        }
                        append(title)
                        append(guideText)
                    }
                },
            style = ByeBooTheme.typography.head2,
        )
        Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))

        Text(
            text = contentText,
            style = ByeBooTheme.typography.body6,
            color = ByeBooTheme.colors.gray400,
        )
    }
}
