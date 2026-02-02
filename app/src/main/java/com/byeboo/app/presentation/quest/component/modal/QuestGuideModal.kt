package com.byeboo.app.presentation.quest.component.modal

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp

@Composable
fun GuideContent(
    userName: String?,
    guideText: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = screenWidthDp(49.dp), vertical = screenHeightDp(26.dp)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(color = ByeBooTheme.colors.primary300)) {
                    append("QUEST JOURNEY")
                }

                append("\n")

                withStyle(style = SpanStyle(color = ByeBooTheme.colors.primary100)) {
                    append("START!")
                }
            },
            style = ByeBooTheme.typography.head1,
            textAlign = TextAlign.Center,
        )

        Spacer(modifier = Modifier.height(screenHeightDp(32.dp)))

        Image(
            painter = painterResource(id = R.drawable.bori_clover),
            contentDescription = "이미지",
            modifier =
                Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
            contentScale = ContentScale.Fit,
        )

        Spacer(modifier = Modifier.height(screenHeightDp(32.dp)))

        Text(
            buildAnnotatedString {
                withStyle(
                    style =
                        ByeBooTheme.typography.body1
                            .toSpanStyle()
                            .copy(color = ByeBooTheme.colors.primary300),
                ) {
                    append(userName)
                }
                withStyle(
                    style =
                        ByeBooTheme.typography.body3
                            .toSpanStyle()
                            .copy(color = ByeBooTheme.colors.gray300),
                ) {
                    append(guideText)
                }
            },
            textAlign = TextAlign.Center,
        )
    }
}
