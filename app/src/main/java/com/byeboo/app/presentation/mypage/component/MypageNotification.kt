package com.byeboo.app.presentation.mypage.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.noRippleClickable

@Composable
fun NotificationToggle(
    isToggleOn: Boolean,
    onToggleClicked: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val toggleWidth = 48.dp
    val toggleHeight = 28.dp
    val togglePadding = 2.dp
    val thumbSize = toggleHeight - togglePadding * 2

    val onOffset = remember(toggleWidth, thumbSize, togglePadding) {
        toggleWidth - thumbSize - togglePadding * 2
    }

    val toggleOffset by animateDpAsState(
        targetValue = if (isToggleOn) onOffset else 0.dp
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isToggleOn) ByeBooTheme.colors.primary300 else ByeBooTheme.colors.gray600
    )

    Box(
        modifier = modifier
            .width(toggleWidth)
            .height(toggleHeight)
            .clip(CircleShape)
            .background(backgroundColor)
            .noRippleClickable { onToggleClicked(!isToggleOn) }
            .padding(togglePadding)
    ) {
        Box(
            modifier = Modifier
                .offset(toggleOffset)
                .size(thumbSize)
                .clip(CircleShape)
                .background(ByeBooTheme.colors.white)
        )


    }
}


