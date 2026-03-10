package com.byeboo.app.presentation.offboarding.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.model.quest.JourneyType
import com.byeboo.app.core.util.noRippleClickable
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp

@Composable
fun JourneyCard(
    journeyType: JourneyType,
    onJourneyCardClick: (JourneyType) -> Unit,
    chipBackgroundColor: Color,
    chipTextColor: Color,
    journeyTitleTextColor: Color,
    journeyCardTextStyle: TextStyle,
    modifier: Modifier = Modifier,
    borderColor: Color = Color.Unspecified,
) {
    val journeyTypeText =
        when (journeyType) {
            JourneyType.RECORDING -> "이별"
            JourneyType.ACTIVE -> "행동"
            JourneyType.REUNION -> "재회"
        }

    val journeyTitle =
        when (journeyType) {
            JourneyType.RECORDING -> "이별 극복 여정"
            JourneyType.ACTIVE -> "감정 정리 여정"
            JourneyType.REUNION -> "재회 준비 여정"
        }

    Row(
        modifier =
            modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .noRippleClickable { onJourneyCardClick(journeyType) }
                .background(color = ByeBooTheme.colors.whiteAlpha5)
                .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(12.dp))
                .padding(horizontal = screenWidthDp(24.dp), vertical = screenHeightDp(18.dp)),
    ) {
        Row(
            modifier =
                Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(color = chipBackgroundColor)
                    .padding(horizontal = screenWidthDp(12.dp), vertical = screenHeightDp(4.dp)),
        ) {
            Text(
                text = journeyTypeText,
                color = chipTextColor,
                style = ByeBooTheme.typography.cap1,
            )
        }

        Spacer(modifier = Modifier.width(screenWidthDp(12.dp)))

        Text(
            text = journeyTitle,
            color = journeyTitleTextColor,
            style = journeyCardTextStyle,
        )
    }
}
