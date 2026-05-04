package com.byeboo.app.presentation.quest.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.component.button.ByeBooButton
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp

@Composable
fun JourneyCompleteScreen(
    username: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(horizontal = screenWidthDp(50.dp)),
    ) {
        Spacer(modifier = Modifier.weight(200f))

        Image(
            painter =
                painterResource(
                    id = R.drawable.img_bori_talk,
                ),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = screenWidthDp(61.dp))
                    .aspectRatio(1f),
        )

        Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))

        Text(
            text = "${username}님, 수고하셨어요.\n보리가 하고 싶은 말이 있다고 해요!",
            style = ByeBooTheme.typography.sub3,
            color = ByeBooTheme.colors.gray50,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))

        ByeBooButton(
            onClick = onClick,
            buttonText = "보리 만나러 가기",
            buttonStyle = ByeBooTheme.typography.body2,
            buttonTextColor = ByeBooTheme.colors.white,
            buttonBackgroundColor = ByeBooTheme.colors.primary300,
        )

        Spacer(modifier = Modifier.weight(162f))
    }
}
