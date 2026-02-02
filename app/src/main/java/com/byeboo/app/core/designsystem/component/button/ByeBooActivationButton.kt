package com.byeboo.app.core.designsystem.component.button

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.noRippleClickable
import com.byeboo.app.core.util.screenHeightDp

@Composable
fun ByeBooActivationButton(
    buttonDisableColor: Color,
    buttonText: String,
    buttonDisableTextColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = false
) {
    val buttonBackgroundColor = if (isEnabled) ByeBooTheme.colors.primary300 else buttonDisableColor
    val buttonTextColor = if (isEnabled) ByeBooTheme.colors.white else buttonDisableTextColor

    Row(
        modifier =
        modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(color = buttonBackgroundColor)
            .noRippleClickable(onClick = { if (isEnabled) onClick() })
            .padding(vertical = screenHeightDp(16.dp)),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = buttonText,
            style = ByeBooTheme.typography.body3,
            color = buttonTextColor
        )
    }
}
