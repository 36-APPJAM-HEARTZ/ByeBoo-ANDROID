package com.byeboo.app.presentation.quest.record.complete

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.byeboo.app.core.designsystem.component.LoadingScreen
import com.byeboo.app.core.designsystem.component.button.ByeBooButton
import com.byeboo.app.core.designsystem.component.text.ContentText
import com.byeboo.app.core.designsystem.component.topbar.CloseTopbar
import com.byeboo.app.core.designsystem.event.LocalSnackBarTrigger
import com.byeboo.app.core.designsystem.type.EmotionChipType
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.findActivity
import com.byeboo.app.core.util.inAppReview
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.quest.component.card.QuestCompleteDialog
import com.byeboo.app.presentation.quest.component.card.QuestEmotionDescriptionCard
import com.byeboo.app.presentation.quest.component.text.QuestTitle
import com.byeboo.app.presentation.quest.navigation.AiAnswerOrigin
import kotlinx.coroutines.delay

@Composable
fun QuestRecordingCompleteRoute(
    navigateToQuest: () -> Unit,
    navigateToOffboardingCompletedGuide: () -> Unit,
    navigateToQuestAiAnswer: (Long, Boolean, AiAnswerOrigin) -> Unit,
    paddingValues: PaddingValues,
    viewModel: QuestRecordingCompleteViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val showSnackBar = LocalSnackBarTrigger.current
    val context = LocalContext.current
    val activity = context.findActivity()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is QuestRecordingCompleteSideEffect.NavigateToQuest -> navigateToQuest()
                is QuestRecordingCompleteSideEffect.NavigateToOffboardingCompletedGuide -> navigateToOffboardingCompletedGuide()
                is QuestRecordingCompleteSideEffect.NavigateToQuestAiAnswer ->
                    navigateToQuestAiAnswer(
                        effect.questId,
                        effect.isExistedAiAnswer,
                        effect.aiAnswerOrigin,
                    )
                is QuestRecordingCompleteSideEffect.ShowInAppReview -> {
                    activity?.let { activity ->
                        inAppReview(activity)
                    }
                }

                is QuestRecordingCompleteSideEffect.ShowSnackBar -> showSnackBar(effect.snackBarType)
            }
        }
    }

    if (uiState.showCompleteModal) {
        QuestCompleteDialog(
            modifier = Modifier.padding(horizontal = screenWidthDp(24.dp)),
        )

        LaunchedEffect(Unit) {
            delay(2000L)
            viewModel.closeCompleteModal()
        }
    }

    BackHandler { viewModel.onCloseClicked() }

    if (uiState.isLoading) {
        LoadingScreen()
    } else {
        QuestRecordingCompleteScreen(
            uiState = uiState,
            paddingValues = paddingValues,
            onCloseClick = viewModel::onCloseClicked,
            onAiAnswerClick = viewModel::onAiAnswerClicked,
        )
    }
}

@Composable
private fun QuestRecordingCompleteScreen(
    uiState: QuestRecordingCompleteState,
    paddingValues: PaddingValues,
    onCloseClick: () -> Unit,
    onAiAnswerClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()
    val canScroll by remember {
        derivedStateOf { scrollState.maxValue > 0 }
    }

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(ByeBooTheme.colors.background)
                .padding(
                    top = paddingValues.calculateTopPadding() + screenHeightDp(43.dp),
                    bottom = paddingValues.calculateBottomPadding(),
                ),
    ) {
        CloseTopbar(
            onCloseClick = onCloseClick,
            modifier = Modifier.padding(horizontal = screenWidthDp(24.dp)),
        )

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(state = scrollState)
                    .padding(horizontal = screenWidthDp(24.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(screenHeightDp(20.dp)),
        ) {
            QuestTitle(
                stepNumber = uiState.stepNumber,
                questNumber = uiState.questNumber,
                createdAt = uiState.createdAt,
                questQuestion = uiState.question,
            )

            ContentText(
                text = uiState.answer,
            )

            QuestEmotionDescriptionContent(
                questEmotionDescription = uiState.emotionDescription,
                emotionType = uiState.selectedEmotion,
            )

            if (canScroll) {
                Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            ByeBooButton(
                buttonText = if (uiState.isExistedAiAnswer) "보리의 답장 보러가기" else "보리에게 답장받기",
                buttonTextColor = ByeBooTheme.colors.white,
                buttonStyle = ByeBooTheme.typography.body2,
                buttonBackgroundColor = ByeBooTheme.colors.primary300,
                onClick = onAiAnswerClick,
                modifier =
                    Modifier
                        .padding(bottom = screenHeightDp(10.dp)),
            )
        }
    }
}

@Composable
private fun QuestEmotionDescriptionContent(
    questEmotionDescription: String,
    emotionType: EmotionChipType,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        QuestEmotionDescriptionCard(
            questEmotionDescription = questEmotionDescription,
            emotionType = emotionType,
        )
    }
}
