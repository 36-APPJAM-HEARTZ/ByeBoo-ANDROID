package com.byeboo.app.presentation.quest.component.chip

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
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
    onChipClick: ((LargeTagType) -> Unit)? = null
) {
    val backgroundColor = if (isSelected) {
        ByeBooTheme.colors.primary300
    } else {
        ByeBooTheme.colors.whiteAlpha10
    }

    val textColor = if (isSelected) {
        ByeBooTheme.colors.primary200
    } else {
        ByeBooTheme.colors.gray400
    }

    val textStyle = if (isSelected) {
        ByeBooTheme.typography.body4
    } else {
        ByeBooTheme.typography.body5
    }

    Column(
        modifier = modifier.then(
            if (onChipClick != null && enabled) {
                Modifier.noRippleClickable { onChipClick(emotionType) }
            } else {
                Modifier
            }
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(emotionType.titleIcon),
            contentDescription = null,
            modifier = Modifier
                .width(screenWidthDp(72.dp))
                .height(screenHeightDp(56.dp))
        )

        Spacer(modifier = modifier.height(screenHeightDp(8.dp)))

        Text(
            text = stringResource(emotionType.titleResId),
            color = textColor,
            style = textStyle
        )


    }
}


@Preview
@Composable
private fun QuestEmotionChipPreview() {
    ByeBooTheme {
        EmotionChip(emotionType = LargeTagType.EMOTION_NEUTRAL)
    }
}