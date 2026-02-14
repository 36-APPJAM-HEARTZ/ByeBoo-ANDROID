package com.byeboo.app.core.designsystem.component.tag

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.byeboo.app.core.designsystem.type.EmotionChipType
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp

@Composable
fun EmotionChip(
    emotionChipType: EmotionChipType,
    backgroundColor: Color,
    textColor: Color,
    textStyle: TextStyle,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .width(screenWidthDp(85.dp))
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(emotionChipType.roundedCorner),
                ).padding(vertical = screenHeightDp(emotionChipType.verticalPadding)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(emotionChipType.titleResId),
            color = textColor,
            style = textStyle,
        )
    }
}
