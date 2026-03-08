package com.byeboo.app.core.designsystem.component.topbar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenHeightDp

@Composable
fun ByeBooTopbar(
    modifier: Modifier = Modifier,
    title: String? = null,
    textColor: Color = ByeBooTheme.colors.gray50,
    textStyle: TextStyle = ByeBooTheme.typography.sub1,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable () -> Unit = {},
    backgroundColor: Color = ByeBooTheme.colors.background,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .background(backgroundColor)
                .padding(bottom = screenHeightDp(16.dp)),
    ) {
        Row(
            modifier = Modifier.align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            navigationIcon()
        }

        if (title != null) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = title,
                color = textColor,
                style = textStyle,
            )
        }

        Row(
            modifier = Modifier.align(Alignment.CenterEnd),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            actions()
        }
    }
}
