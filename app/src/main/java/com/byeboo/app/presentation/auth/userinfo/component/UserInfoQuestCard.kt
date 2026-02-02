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
import androidx.compose.foundation.layout.size
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
import com.byeboo.app.core.designsystem.ui.theme.whiteAlpha10
import com.byeboo.app.core.util.noRippleClickable
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp

@Composable
fun UserInfoQuestCard(
    title: String,
    content: String,
    imageRes: Int,
    isSelected: Boolean,
    onCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val backgroundColor = if (isSelected) ByeBooTheme.colors.primary300Alpha20 else whiteAlpha10
    val borderColor = if (isSelected) ByeBooTheme.colors.primary300 else Color.Transparent
    val textColor = if (isSelected) ByeBooTheme.colors.primary200 else ByeBooTheme.colors.gray300
    val textStyle = if (isSelected) ByeBooTheme.typography.body5 else ByeBooTheme.typography.body6

    Box(
        modifier =
        modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .border(2.dp, borderColor, RoundedCornerShape(12.dp))
            .noRippleClickable(onCardClick)
            .padding(vertical = screenHeightDp(24.dp))
    ) {
        Column(
            modifier =
            Modifier
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(imageRes),
                contentDescription = null,
                modifier =
                Modifier
                    .size(width = screenWidthDp(56.dp), height = screenHeightDp(56.dp))
            )

            Spacer(modifier = Modifier.height(screenHeightDp(10.dp)))

            Text(
                text = title,
                style = ByeBooTheme.typography.sub2,
                color = textColor
            )

            Spacer(modifier = Modifier.height(screenHeightDp(8.dp)))

            Text(
                text = content,
                style = textStyle,
                color = textColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
