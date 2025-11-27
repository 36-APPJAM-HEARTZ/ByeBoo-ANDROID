package com.byeboo.app.presentation.auth.userinfo.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp

@Composable
fun StepProgressBar(
    currentStep: Int,
    modifier: Modifier = Modifier,
    activeColor: Color = ByeBooTheme.colors.primary300,
    inactiveColor: Color = ByeBooTheme.colors.primary300Alpha20
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(screenWidthDp(6.dp)),
        modifier = modifier.padding(vertical = screenHeightDp(13.dp))
    ) {
        repeat(3) { index ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(screenHeightDp(6.dp))
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (index == currentStep - 1) activeColor else inactiveColor)
            )
        }
    }
}
