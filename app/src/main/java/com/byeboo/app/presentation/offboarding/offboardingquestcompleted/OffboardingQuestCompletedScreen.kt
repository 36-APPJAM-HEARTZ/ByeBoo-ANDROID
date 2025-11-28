package com.byeboo.app.presentation.offboarding.offboardingquestcompleted

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.component.tag.MiddleTag
import com.byeboo.app.core.designsystem.component.text.DescriptionText
import com.byeboo.app.core.designsystem.event.LocalSnackBarTrigger
import com.byeboo.app.core.designsystem.type.MiddleTagType
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.quest.component.chip.QuestBox
import com.byeboo.app.presentation.quest.component.text.QuestStepTitle

@Composable
fun OffboardingQuestCompletedRoute(
    journey: QuestType,
    navigateUp: () -> Unit,
    navigateToQuestReview: (Long) -> Unit,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: OffboardingQuestCompletedViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val showSnackBar = LocalSnackBarTrigger.current

    LaunchedEffect(journey) {
        viewModel.setJourney(journey)
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is QuestCompletedSideEffect.NavigateUp -> navigateUp()
                is QuestCompletedSideEffect.NavigateToQuestReview -> navigateToQuestReview(
                    effect.questId
                )
                is QuestCompletedSideEffect.ShowSnackBar -> showSnackBar(effect.message)
            }
        }
    }

    OffboardingQuestCompletedScreen(
        uiState = uiState,
        onCancelClick = viewModel::onCancelClicked,
        paddingValues = paddingValues,
        onQuestClick = viewModel::onQuestClicked,
        modifier = modifier
    )
}

@Composable
private fun OffboardingQuestCompletedScreen(
    uiState: QuestCompletedState,
    onCancelClick: () -> Unit,
    paddingValues: PaddingValues,
    onQuestClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ByeBooTheme.colors.black)
            .padding(horizontal = screenWidthDp(24.dp))
            .padding(
                top = paddingValues.calculateTopPadding() + screenHeightDp(43.dp),
                bottom = paddingValues.calculateBottomPadding()
            )
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_cancel),
            contentDescription = null,
            tint = ByeBooTheme.colors.white,
            modifier = Modifier
                .align(Alignment.End)
                .clickable(onClick = onCancelClick)
        )

        Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

        MiddleTag(
            middleTagType = MiddleTagType.QUEST_PERIOD,
            text = uiState.progressPeriod,
            textStyle = ByeBooTheme.typography.cap2
        )

        Spacer(modifier = Modifier.height(screenHeightDp(8.dp)))

        DescriptionText(
            nicknameText = "${uiState.userName}님의",
            title = "${uiState.journeyType.journeyName} 여정",
            guideText = "이에요",
            contentText = "30개의 퀘스트를 돌아보며 성장을 체감할 수 있어요.",
            bottom = 18.dp
        )

        LazyColumn(
            contentPadding = PaddingValues(bottom = screenHeightDp(21.dp)),
            modifier = Modifier.fillMaxWidth()
        ) {
            uiState.questGroups.forEachIndexed { stepIndex, group ->
                item("header_$stepIndex") {
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = ByeBooTheme.colors.whiteAlpha10,
                        modifier = Modifier.padding(vertical = screenHeightDp(8.dp))
                    )

                    Spacer(modifier = Modifier.height(screenHeightDp(24.dp)))

                    QuestStepTitle(
                        stepNumber = (stepIndex + 1).toLong(),
                        stepTitle = group.stepTitle
                    )

                    Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))
                }

                val questChunks = group.quests.chunked(3)
                questChunks.forEachIndexed { chunkIndex, questChunk ->
                    item("quest_row_${stepIndex}_$chunkIndex") {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(screenWidthDp(21.dp))
                        ) {
                            questChunk.forEach { quest ->
                                QuestBox(
                                    modifier = Modifier.weight(1f),
                                    questId = quest.questId,
                                    questNumber = quest.questNumber,
                                    state = quest.state,
                                    onQuestClick = { onQuestClick(quest.questId) }
                                )
                            }
                            repeat(3 - questChunk.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }

                        Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))
                    }
                }
            }
        }
    }
}
