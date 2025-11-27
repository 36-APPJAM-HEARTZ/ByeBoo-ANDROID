package com.byeboo.app.presentation.home.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.unit.dp
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp

@Composable
fun HomeProgressCard(
    title: String,
    modifier: Modifier = Modifier,
    currentStep: Long = 0L,
    totalSteps: Int = 30
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(
                ByeBooTheme.colors.whiteAlpha10,
                RoundedCornerShape(12.dp)
            )
            .padding(horizontal = screenWidthDp(24.dp), vertical = screenHeightDp(16.dp))
    ) {
        Column {
            Text(
                text = title,
                style = ByeBooTheme.typography.sub2,
                color = ByeBooTheme.colors.gray50
            )

            Spacer(modifier = Modifier.height(screenHeightDp(8.dp)))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomProgressBar(
                    progress = if (totalSteps > 0) currentStep.toFloat() / totalSteps else 0f,
                    modifier = Modifier
                        .weight(1f)
                        .height(screenHeightDp(6.dp))
                )

                Spacer(modifier = Modifier.width(screenWidthDp(8.dp)))

                Text(
                    text = "($currentStep/$totalSteps)",
                    style = ByeBooTheme.typography.cap2,
                    color = ByeBooTheme.colors.gray400
                )
            }
        }
    }
}

@Composable
fun CustomProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    backgroundColor: Color = ByeBooTheme.colors.primary300Alpha20,
    progressColor: Color = ByeBooTheme.colors.primary300
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(progress.coerceIn(0f, 1f))
                .background(progressColor)
        )
    }
}
