package com.byeboo.app.presentation.offboarding.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.noRippleClickable
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.domain.model.offboarding.JourneyType

@Composable
fun JourneyCard(
    journeyType: JourneyType,
    onJourneyCardClick: () -> Unit,
    chipBackgroundColor: Color,
    chipTextColor: Color,
    journeyTitleTextColor: Color,
    modifier: Modifier = Modifier,
    borderColor: Color = Color.Unspecified
) {
    val journeyTypeText = when (journeyType) {
        JourneyType.QUESTION -> "질문형"
        JourneyType.ACTION -> "행동형"
    }

    val journeyTitle = when (journeyType) {
        JourneyType.QUESTION -> "감정 정리 여정"
        JourneyType.ACTION -> "감정 직면 여정"
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .noRippleClickable(onClick = onJourneyCardClick)
            .background(color = ByeBooTheme.colors.whiteAlpha10)
            .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 24.dp, vertical = 18.dp)
    ) {
        Row(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(color = chipBackgroundColor)
                .padding(horizontal = 12.dp, vertical = 4.dp)
        ) {
            Text(
                text = journeyTypeText,
                color = chipTextColor,
                style = ByeBooTheme.typography.cap1
            )
        }

        Spacer(modifier = Modifier.width(screenWidthDp(12.dp)))

        Text(
            text = journeyTitle,
            color = journeyTitleTextColor,
            style = ByeBooTheme.typography.body2
        )
    }
}
