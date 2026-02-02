package com.byeboo.app.presentation.quest.component.card

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp

@Composable
fun QuestCardContainer(
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color.Transparent,
    borderColor: Color = Color.Transparent,
    cornerRadius: Dp = 12.dp,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier =
        modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(cornerRadius))
            .background(backgroundColor)
            .then(
                if (borderColor != Color.Transparent) {
                    Modifier.border(
                        width = 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(cornerRadius)
                    )
                } else {
                    Modifier
                }
            ).padding(horizontal = screenWidthDp(8.dp), vertical = screenHeightDp(8.dp))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}

@Composable
fun BackgroundImageLayer(imageResId: Int) {
    Image(
        painter = painterResource(id = imageResId),
        contentDescription = null,
        modifier =
        Modifier
            .fillMaxSize()
            .padding(horizontal = screenWidthDp(3.dp))
            .padding(top = screenHeightDp(6.dp))
    )
}

@Composable
fun QuestNumberLabel(
    questNumber: Long,
    color: Color,
    modifier: Modifier = Modifier
) {
    Text(
        text = String.format("%02d", questNumber),
        style = ByeBooTheme.typography.cap1,
        color = color,
        modifier = modifier
    )
}
