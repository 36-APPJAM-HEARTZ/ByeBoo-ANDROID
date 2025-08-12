package com.byeboo.app.presentation.offboarding.offboardingnewjourney

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.noRippleClickable
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.offboarding.component.JourneyCard

@Composable
fun OffboardingNewJourneyRoute(
    bottomPadding: Dp,
    modifier: Modifier = Modifier,
    viewModel: OffboardingNewJourneyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // TODO: 클릭 시 이동 관련 추후에 구현할 예정
    OffboardingNewJourneyScreen(
        uiState = uiState,
        bottomPadding = bottomPadding,
        onBackClick = {},
        onJourneyUncompletedCardClick = {},
        onJourneyCompletedCardClick = {},
        modifier = modifier
    )
}

@Composable
private fun OffboardingNewJourneyScreen(
    uiState: OffboardingNewJourneyState,
    bottomPadding: Dp,
    onBackClick: () -> Unit,
    onJourneyUncompletedCardClick: () -> Unit,
    onJourneyCompletedCardClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = ByeBooTheme.colors.black)
            .padding(horizontal = 24.dp)
            .padding(top = 67.dp, bottom = bottomPadding)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_left),
            contentDescription = "",
            tint = ByeBooTheme.colors.gray50,
            modifier = Modifier
                .size(24.dp)
                .noRippleClickable(onClick = onBackClick)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "어떤 여정을 시작해 볼까요?",
            color = ByeBooTheme.colors.gray50,
            style = ByeBooTheme.typography.head1
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "각 여정 당 30개의 퀘스트를 제공해 드려요",
            color = ByeBooTheme.colors.gray400,
            style = ByeBooTheme.typography.body6
        )

        Spacer(modifier = Modifier.height(4.dp))

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            thickness = 1.dp,
            color = ByeBooTheme.colors.whiteAlpha10
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "미완료",
                    color = ByeBooTheme.colors.gray300
                )

                Spacer(modifier = Modifier.width(screenWidthDp(8.dp)))

                Text(
                    text = "${uiState.unCompleted}개",
                    color = ByeBooTheme.colors.gray500,
                    style = ByeBooTheme.typography.body2
                )
            }

            repeat(uiState.unCompleted) {
                JourneyCard(
                    journeyType = uiState.journeyType,
                    onJourneyCardClick = onJourneyUncompletedCardClick,
                    chipBackgroundColor = ByeBooTheme.colors.primary300,
                    chipTextColor = ByeBooTheme.colors.white,
                    journeyTitleTextColor = ByeBooTheme.colors.gray50,
                )
            }

            PreparingCard()
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            thickness = 1.dp,
            color = ByeBooTheme.colors.whiteAlpha10
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "완료",
                    color = ByeBooTheme.colors.gray300,
                    style = ByeBooTheme.typography.cap2
                )

                Spacer(modifier = Modifier.width(screenWidthDp(8.dp)))

                Text(
                    text = "${uiState.completed}개",
                    color = ByeBooTheme.colors.gray500,
                    style = ByeBooTheme.typography.body2
                )
            }

            repeat(uiState.completed) {
                JourneyCard(
                    journeyType = uiState.journeyType,
                    onJourneyCardClick = onJourneyCompletedCardClick,
                    chipBackgroundColor = ByeBooTheme.colors.whiteAlpha10,
                    chipTextColor = ByeBooTheme.colors.gray300,
                    journeyTitleTextColor = ByeBooTheme.colors.gray300,
                )
            }
        }
    }


}

@Composable
private fun PreparingCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(12.dp))
            .background(color = ByeBooTheme.colors.whiteAlpha10)
            .padding(vertical = 22.dp)
    ) {
        Text(
            text = "준비 중",
            color = ByeBooTheme.colors.gray600,
            style = ByeBooTheme.typography.body6,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            )
    }
}
