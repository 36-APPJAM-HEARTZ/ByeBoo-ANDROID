package com.byeboo.app.presentation.quest.component.chip

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.byeboo.app.core.designsystem.type.LargeTagType
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.noRippleClickable
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp

@Composable
fun EmotionChip(
    modifier: Modifier = Modifier,
    emotionType: LargeTagType,
    isSelected: Boolean = false,
    enabled: Boolean = true,
    isDimmed: Boolean = false,
    onChipClick: ((LargeTagType) -> Unit)? = null
) {
    val backgroundColor =
        if (isSelected) {
            ByeBooTheme.colors.primary300Alpha20
        } else {
            ByeBooTheme.colors.whiteAlpha10
        }

    val textColor =
        if (isSelected) {
            ByeBooTheme.colors.primary200
        } else {
            ByeBooTheme.colors.gray500
        }

    val textStyle =
        if (isSelected) {
            ByeBooTheme.typography.body4
        } else {
            ByeBooTheme.typography.body6
        }

    val shape = RoundedCornerShape(12.dp)

    val baseModifier =
        modifier
            .then(
                if (onChipClick != null && enabled) {
                    Modifier.noRippleClickable { onChipClick(emotionType) }
                } else {
                    Modifier
                }
            ).clip(shape)
            .background(
                color = backgroundColor,
                shape = shape
            ).then(
                if (isSelected) {
                    Modifier.border(1.dp, ByeBooTheme.colors.primary300, shape)
                } else {
                    Modifier
                }
            )

    Box(
        modifier = baseModifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = screenWidthDp(14.dp),
                vertical = screenHeightDp(8.dp)
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = emotionType.titleIcon),
                contentDescription = null,
                modifier = Modifier.size(56.dp)
            )

            Spacer(modifier = Modifier.height(screenHeightDp(8.dp)))

            Text(
                text = stringResource(emotionType.titleResId),
                color = textColor,
                style = textStyle
            )
        }

        if (isDimmed) {
            Box(
                modifier =
                Modifier
                    .matchParentSize()
                    .background(color = ByeBooTheme.colors.black.copy(alpha = 0.4f))
            )
        }
    }
}
