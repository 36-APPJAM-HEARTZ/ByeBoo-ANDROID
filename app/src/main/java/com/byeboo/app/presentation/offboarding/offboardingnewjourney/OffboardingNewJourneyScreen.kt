package com.byeboo.app.presentation.offboarding.offboardingnewjourney

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.core.util.noRippleClickable
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.offboarding.OffboardingJourneyState
import com.byeboo.app.presentation.offboarding.OffboardingJourneyViewModel
import com.byeboo.app.presentation.offboarding.component.JourneyCard

@Composable
fun OffboardingNewJourneyRoute(
    navigateToQuestStart: (QuestType?) -> Unit,
    navigateUp: () -> Unit,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: OffboardingJourneyViewModel = hiltViewModel(),
    offboardingNewJourneyViewModel: OffboardingNewJourneyViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        offboardingNewJourneyViewModel.sideEffect.collect { effect ->
            when (effect) {
                is OffboardingNewJourneySideEffect.NavigateToQuestStart ->
                    navigateToQuestStart(
                        effect.journey,
                    )
                is OffboardingNewJourneySideEffect.NavigateUp -> navigateUp()
            }
        }
    }

    OffboardingNewJourneyScreen(
        uiState = uiState,
        paddingValues = paddingValues,
        onBackClick = offboardingNewJourneyViewModel::onBackClicked,
        onJourneyUncompletedCardClick = { type ->
            offboardingNewJourneyViewModel.postNewJourney(
                type,
            )
        },
        modifier = modifier,
    )
}

@Composable
private fun OffboardingNewJourneyScreen(
    uiState: OffboardingJourneyState,
    paddingValues: PaddingValues,
    onBackClick: () -> Unit,
    onJourneyUncompletedCardClick: (QuestType) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(color = ByeBooTheme.colors.black)
                .padding(horizontal = screenWidthDp(24.dp))
                .padding(
                    top = paddingValues.calculateTopPadding() + screenHeightDp(43.dp),
                    bottom = paddingValues.calculateBottomPadding(),
                ).verticalScroll(rememberScrollState()),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_left),
            contentDescription = null,
            tint = ByeBooTheme.colors.gray50,
            modifier =
                Modifier
                    .size(24.dp)
                    .noRippleClickable(onClick = onBackClick),
        )

        Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))

        Text(
            text = "어떤 여정을 시작해 볼까요?",
            color = ByeBooTheme.colors.gray50,
            style = ByeBooTheme.typography.head1,
        )

        Spacer(modifier = Modifier.height(screenHeightDp(8.dp)))

        Text(
            text = "각 여정 당 30개의 퀘스트를 제공해 드려요",
            color = ByeBooTheme.colors.gray400,
            style = ByeBooTheme.typography.body6,
        )

        Spacer(modifier = Modifier.height(screenHeightDp(4.dp)))

        HorizontalDivider(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = screenHeightDp(8.dp)),
            thickness = 1.dp,
            color = ByeBooTheme.colors.whiteAlpha10,
        )

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = screenHeightDp(16.dp)),
            verticalArrangement = Arrangement.spacedBy(screenHeightDp(16.dp)),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "미완료",
                    color = ByeBooTheme.colors.gray300,
                    style = ByeBooTheme.typography.cap2,
                )

                Spacer(modifier = Modifier.width(screenWidthDp(8.dp)))

                Text(
                    text = "${uiState.uncompletedCount}개",
                    color = ByeBooTheme.colors.gray500,
                    style = ByeBooTheme.typography.body2,
                )
            }

            (uiState.uncompletedCards).forEach { card ->
                key(card) {
                    JourneyCard(
                        journeyType = card.journeyType,
                        onJourneyCardClick = { onJourneyUncompletedCardClick(card.journeyType) },
                        chipBackgroundColor = ByeBooTheme.colors.primary300,
                        chipTextColor = ByeBooTheme.colors.white,
                        journeyTitleTextColor = ByeBooTheme.colors.white,
                        journeyCardTextStyle = ByeBooTheme.typography.body2,
                        borderColor = ByeBooTheme.colors.primary300,
                    )
                }
            }

            PreparingCard()
        }
    }
}

@Composable
private fun PreparingCard() {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .clip(shape = RoundedCornerShape(12.dp))
                .background(color = ByeBooTheme.colors.whiteAlpha10)
                .padding(vertical = screenHeightDp(22.dp)),
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
