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
import com.byeboo.app.core.designsystem.type.TagColorType
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme

@Composable
fun MiddleTag(
    middleTagType: MiddleTagType,
    text: String,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    val borderColor = middleTagType.borderColor?.let { middleTagColor(it) }
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
                    color = middleTagColor(middleTagType.backgroundColor),
                    shape = RoundedCornerShape(middleTagType.roundedCorner),
                )
                .then(
                    if (borderColor != null) {
                        Modifier.border(
                            width = 1.dp,
                            color = borderColor,
                            shape = RoundedCornerShape(middleTagType.roundedCorner)
                        )
                    } else {
                        Modifier
                    }
                )
                .padding(
                    horizontal = middleTagType.horizontalPadding,
                    vertical = middleTagType.verticalPadding,
                )
                ,
    ) {
        Text(
            text = tagText,
            color = middleTagColor(middleTagType.textColor),
            style = textStyle,
        )
    }
}

@Composable
fun middleTagColor(colorType: TagColorType): Color =
    when (colorType) {
        TagColorType.WHITE_ALPHA_5 -> ByeBooTheme.colors.whiteAlpha5
        TagColorType.SECONDARY_300_ALPHA_10 -> ByeBooTheme.colors.secondary300Alpha10
        TagColorType.GRAY_300 -> ByeBooTheme.colors.gray300
        TagColorType.GRAY_800 -> ByeBooTheme.colors.gray800
        TagColorType.SECONDARY_300 -> ByeBooTheme.colors.secondary300
        TagColorType.PRIMARY_50 -> ByeBooTheme.colors.primary50
        TagColorType.PRIMARY_200 -> ByeBooTheme.colors.primary200
        TagColorType.PRIMARY_300 -> ByeBooTheme.colors.primary300
    }
