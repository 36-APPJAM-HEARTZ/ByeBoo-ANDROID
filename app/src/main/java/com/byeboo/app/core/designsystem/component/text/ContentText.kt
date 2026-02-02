package com.byeboo.app.core.designsystem.component.text

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp

@Composable
fun ContentText(
    text: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier =
        modifier
            .fillMaxWidth()
            .background(
                color = ByeBooTheme.colors.whiteAlpha10,
                shape = RoundedCornerShape(12.dp)
            ).padding(horizontal = screenWidthDp(24.dp), vertical = screenHeightDp(18.dp))
    ) {
        Text(
            text = text,
            style = ByeBooTheme.typography.body6,
            color = ByeBooTheme.colors.gray300
        )
    }
}
