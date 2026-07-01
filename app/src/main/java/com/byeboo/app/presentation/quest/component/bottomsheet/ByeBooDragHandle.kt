package com.byeboo.app.presentation.quest.component.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp

@Composable
fun ByeBooDragHandle(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier =
            modifier
                .padding(top = screenHeightDp(5.dp), bottom = screenHeightDp(16.dp))
                .clip(shape = RoundedCornerShape(12.dp)),
    ) {
        Box(
            modifier =
                Modifier
                    .width(screenWidthDp(35.dp))
                    .height(screenHeightDp(5.dp))
                    .background(ByeBooTheme.colors.whiteAlpha5),
        )
        content()
    }
}
