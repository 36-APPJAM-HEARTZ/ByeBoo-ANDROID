package com.byeboo.app.core.designsystem.component.tag

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.byeboo.app.core.designsystem.type.MiddleTagType
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme

@Composable
fun MiddleTag(
    middleTagType: MiddleTagType,
    text: String,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    val (backgroundColor, textColor, borderColor) = getTagColors(middleTagType)

    val tagText =
        if (middleTagType.dynamicContent) {
            stringResource(middleTagType.titleResId, text)
        } else {
            stringResource(middleTagType.titleResId)
        }

    Box(
        modifier =
            modifier
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(middleTagType.roundedCorner),
                ).then(
                    if (borderColor != null) {
                        Modifier.border(
                            width = 1.dp,
                            color = borderColor,
                            shape = RoundedCornerShape(middleTagType.roundedCorner),
                        )
                    } else {
                        Modifier
                    },
                ).padding(
                    horizontal = middleTagType.horizontalPadding,
                    vertical = middleTagType.verticalPadding,
                ),
    ) {
        Text(
            text = tagText,
            color = textColor,
            style = textStyle,
        )
    }
}

@Composable
fun getTagColors(type: MiddleTagType): Triple<Color, Color, Color?> =
    when(type) {
        MiddleTagType.QUEST_START_DAY -> Triple(
            ByeBooTheme.colors.whiteAlpha5,
            ByeBooTheme.colors.gray300,
            null
        )
        MiddleTagType.QUEST_TIP -> Triple(
            ByeBooTheme.colors.whiteAlpha5,
            ByeBooTheme.colors.primary200,
            ByeBooTheme.colors.gray800
        )
        MiddleTagType.QUEST_ESSENTIAL -> Triple(
            ByeBooTheme.colors.whiteAlpha5,
            ByeBooTheme.colors.gray300,
            null
        )
        MiddleTagType.QUEST_OPTIONAL -> Triple(
            ByeBooTheme.colors.whiteAlpha5,
            ByeBooTheme.colors.gray300,
            null
        )
        MiddleTagType.QUEST_PERIOD -> Triple(
            ByeBooTheme.colors.whiteAlpha5,
            ByeBooTheme.colors.gray300,
            null
        )
    }

