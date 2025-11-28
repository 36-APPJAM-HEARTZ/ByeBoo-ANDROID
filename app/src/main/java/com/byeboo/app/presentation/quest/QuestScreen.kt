package com.byeboo.app.presentation.quest

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.byeboo.app.core.designsystem.component.tag.MiddleTag
import com.byeboo.app.core.designsystem.component.text.DescriptionText
import com.byeboo.app.core.designsystem.event.LocalSnackBarTrigger
import com.byeboo.app.core.designsystem.type.MiddleTagType
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.quest.component.chip.QuestBox
import com.byeboo.app.presentation.quest.component.modal.QuestModal
import com.byeboo.app.presentation.quest.component.text.QuestStepTitle
import com.byeboo.app.presentation.quest.model.QuestSideEffect
import kotlinx.coroutines.flow.collectLatest

@Composable
fun QuestRoute(
    navigateToQuestTip: (Long, QuestType) -> Unit,
    navigateToQuestRecording: (Long) -> Unit,
    navigateToQuestBehavior: (Long) -> Unit,
    navigateToQuestReview: (Long) -> Unit,
    navigateToOffboardingCompleteGuide: () -> Unit,
    paddingValues: PaddingValues,
    viewModel: QuestViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val showSnackBar = LocalSnackBarTrigger.current

    LaunchedEffect(uiState.currentStepIndex) {
        if (uiState.questGroups.isNotEmpty() && uiState.currentStepIndex >= 0) {
            val scrollIndex = uiState.questGroups
                .take(uiState.currentStepIndex)
                .sumOf { 1 + (it.quests.size + 2) / 3 }
            listState.animateScrollToItem(index = scrollIndex)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is QuestSideEffect.NavigateToQuestTip -> navigateToQuestTip(effect.questId, effect.questType)
                is QuestSideEffect.NavigateToQuestRecording -> navigateToQuestRecording(effect.questId)
                is QuestSideEffect.NavigateToQuestBehavior -> navigateToQuestBehavior(effect.questId)
                is QuestSideEffect.NavigateToQuestReview -> navigateToQuestReview(effect.questId)
                is QuestSideEffect.NavigateToOffboardingCompletedGuide -> navigateToOffboardingCompleteGuide()
                is QuestSideEffect.ShowSnackBar -> showSnackBar(effect.message)
            }
        }
    }

    QuestScreen(
        uiState = uiState,
        listState = listState,
        paddingValues = paddingValues,
        onQuestClick = viewModel::onQuestClick,
        onDismissModal = viewModel::onQuitDismissModal,
        onTipClick = viewModel::onTipClick,
        onQuestStart = viewModel::onQuestStart
    )
}

@Composable
private fun QuestScreen(
    uiState: QuestUiState,
    listState: LazyListState,
    paddingValues: PaddingValues,
    onQuestClick: (Long) -> Unit,
    onDismissModal: () -> Unit,
    onTipClick: () -> Unit,
    onQuestStart: () -> Unit
) {
    if (uiState.showQuitModal) {
        QuestModal(
            onDismissRequest = onDismissModal,
            questNumber = uiState.selectedQuest?.questNumber ?: 0L,
            questQuestion = uiState.selectedQuest?.questQuestion.orEmpty(),
            navigateToTip = onTipClick,
            progressButton = onQuestStart,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = screenWidthDp(48.dp)),
            dialogProperties = DialogProperties(usePlatformDefaultWidth = false)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ByeBooTheme.colors.black)
            .padding(
                top = paddingValues.calculateTopPadding() + screenHeightDp(27.dp),
                bottom = paddingValues.calculateBottomPadding()
            )
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = screenWidthDp(24.dp))) {
            MiddleTag(
                middleTagType = MiddleTagType.QUEST_START_DAY,
                text = uiState.progressPeriod.toString(),
                textStyle = ByeBooTheme.typography.cap1
            )

            Spacer(modifier = Modifier.height(screenHeightDp(8.dp)))

            DescriptionText(
                nicknameText = "${uiState.userName}님, 지금",
                title = "${uiState.journeyTitle} 여정",
                guideText = "을 진행 중이에요",
                contentText = "오늘도 한 걸음 나아가 볼까요?",
                bottom = 18.dp
            )
        }

        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(screenHeightDp(20.dp)),
            contentPadding = PaddingValues(
                start = screenWidthDp(24.dp),
                end = screenWidthDp(24.dp),
                bottom = screenHeightDp(37.dp)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(ByeBooTheme.colors.black)
        ) {
            uiState.questGroups.forEachIndexed { stepIndex, group ->
                item("header_$stepIndex") {
                    Column {
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = ByeBooTheme.colors.whiteAlpha10,
                            modifier = Modifier.padding(vertical = screenHeightDp(8.dp))
                        )

                        Spacer(modifier = Modifier.padding(top = screenHeightDp(24.dp)))

                        QuestStepTitle(
                            stepNumber = (stepIndex + 1).toLong(),
                            stepTitle = group.stepTitle
                        )

                        Spacer(modifier = Modifier.padding(top = screenHeightDp(8.dp)))
                    }
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
                    }
                }
            }
        }
    }
}
