package com.byeboo.app.presentation.offboarding.offboardingcompletedjourney

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.event.LocalSnackBarTrigger
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.core.util.noRippleClickable
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.offboarding.OffboardingJourneySideEffect
import com.byeboo.app.presentation.offboarding.OffboardingJourneyState
import com.byeboo.app.presentation.offboarding.OffboardingJourneyViewModel
import com.byeboo.app.presentation.offboarding.component.JourneyCard

@Composable
fun OffboardingCompletedJourneyRoute(
    navigateUp: () -> Unit,
    navigateToOffboardingQuestCompleted: (QuestType) -> Unit,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: OffboardingJourneyViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val showSnackBar = LocalSnackBarTrigger.current

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is OffboardingJourneySideEffect.NavigateUp -> navigateUp()
                is OffboardingJourneySideEffect.NavigateToOffboardingQuestCompleted ->
                    navigateToOffboardingQuestCompleted(
                        effect.journey,
                    )
                is OffboardingJourneySideEffect.ShowSnackBar -> showSnackBar(effect.message)
            }
        }
    }

    OffboardingCompletedJourneyScreen(
        uiState = uiState,
        paddingValues = paddingValues,
        onBackClick = viewModel::onBackClicked,
        onJourneyCompletedCardClick = viewModel::onJourneyCompletedCardClicked,
        modifier = modifier,
    )
}

@Composable
private fun OffboardingCompletedJourneyScreen(
    uiState: OffboardingJourneyState,
    paddingValues: PaddingValues,
    onBackClick: () -> Unit,
    onJourneyCompletedCardClick: (QuestType) -> Unit,
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
            modifier =
                Modifier
                    .size(24.dp)
                    .noRippleClickable(onClick = onBackClick),
            tint = ByeBooTheme.colors.gray50,
        )

        Spacer(modifier = Modifier.height(screenHeightDp(36.dp)))

        Text(
            text = "내가 완료한 여정이에요",
            color = ByeBooTheme.colors.gray50,
            style = ByeBooTheme.typography.head1,
        )

        Spacer(modifier = Modifier.height(screenHeightDp(6.dp)))

        HorizontalDivider(
            modifier =
                Modifier
                    .fillMaxWidth(),
            thickness = 1.dp,
            color = ByeBooTheme.colors.whiteAlpha5,
        )

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = screenHeightDp(20.dp)),
            verticalArrangement = Arrangement.spacedBy(screenHeightDp(16.dp)),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "완료",
                    color = ByeBooTheme.colors.gray300,
                    style = ByeBooTheme.typography.cap2,
                )

                Spacer(modifier = Modifier.width(screenWidthDp(8.dp)))

                Text(
                    text = "${uiState.completedCount}개",
                    color = ByeBooTheme.colors.gray500,
                    style = ByeBooTheme.typography.body2,
                )
            }

            (uiState.completedCards).forEach { card ->
                key(card) {
                    JourneyCard(
                        journeyType = card.journeyType,
                        onJourneyCardClick = { onJourneyCompletedCardClick(card.journeyType) },
                        chipBackgroundColor = ByeBooTheme.colors.whiteAlpha5,
                        chipTextColor = ByeBooTheme.colors.gray300,
                        journeyTitleTextColor = ByeBooTheme.colors.gray300,
                        journeyCardTextStyle = ByeBooTheme.typography.body3,
                    )
                }
            }

            if (uiState.completedCount == 0) {
                Spacer(modifier = Modifier.height(screenHeightDp(176.5.dp)))

                Text(
                    text = "아직 완료된 여정이 없어요!",
                    color = ByeBooTheme.colors.gray300,
                    style = ByeBooTheme.typography.body3,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
