package com.byeboo.app.core.designsystem.component.tag

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme

@Composable
fun SmallTag(
    modifier: Modifier = Modifier,
    tagText: String,
    tagColor: Color = ByeBooTheme.colors.secondary300

) {
    Text(
        text = tagText,
        style = ByeBooTheme.typography.cap1.copy(color = tagColor),
        modifier = modifier
    )
}
