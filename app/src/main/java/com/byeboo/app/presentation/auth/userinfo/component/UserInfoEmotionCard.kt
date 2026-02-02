package com.byeboo.app.presentation.auth.userinfo.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.noRippleClickable
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp

@Composable
fun UserInfoEmotionCard(
    content: String,
    imageRes: Int,
    isSelected: Boolean,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val borderColor = if (isSelected) ByeBooTheme.colors.primary300 else Color.Transparent
    val textColor = if (isSelected) ByeBooTheme.colors.primary200 else ByeBooTheme.colors.gray300
    val backgroundColor =
        if (isSelected) {
            ByeBooTheme.colors.primary300Alpha10
        } else {
            ByeBooTheme.colors.whiteAlpha10
        }

    Box(
        modifier =
            modifier
                .clip(RoundedCornerShape(12.dp))
                .background(backgroundColor)
                .border(2.dp, borderColor, RoundedCornerShape(12.dp))
                .noRippleClickable(onCardClick)
                .padding(vertical = screenHeightDp(12.dp)),
    ) {
        Column(
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = screenHeightDp(11.5.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(imageRes),
                contentDescription = null,
                modifier =
                    Modifier
                        .width(screenWidthDp(73.dp))
                        .height(screenHeightDp(100.dp)),
            )

            Spacer(modifier = Modifier.height(screenHeightDp(10.dp)))

            Text(
                text = content,
                style = ByeBooTheme.typography.body5,
                color = textColor,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        }
    }
}
