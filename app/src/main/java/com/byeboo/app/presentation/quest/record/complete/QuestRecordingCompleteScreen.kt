package com.byeboo.app.presentation.quest.record.complete

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.component.button.ByeBooActivationButton
import com.byeboo.app.core.designsystem.component.button.ByeBooButton
import com.byeboo.app.core.designsystem.component.tag.SmallTag
import com.byeboo.app.core.designsystem.component.text.ContentText
import com.byeboo.app.core.designsystem.event.LocalSnackBarTrigger
import com.byeboo.app.core.designsystem.type.EmotionChipType
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.findActivity
import com.byeboo.app.core.util.inAppReview
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.quest.component.card.QuestCompleteCard
import com.byeboo.app.presentation.quest.component.card.QuestEmotionDescriptionCard
import com.byeboo.app.presentation.quest.component.text.CreatedText
import com.byeboo.app.presentation.quest.component.text.QuestContent
import com.byeboo.app.presentation.quest.component.text.QuestTitle
import com.byeboo.app.presentation.quest.component.type.QuestContentType

@Composable
fun QuestRecordingCompleteRoute(
    navigateToQuest: () -> Unit,
    navigateToOffboardingCompletedGuide: () -> Unit,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
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
                is QuestRecordingCompleteSideEffect.ShowInAppReview -> {
                    activity?.let { activity ->
                        inAppReview(activity)
                    }
                }
                is QuestRecordingCompleteSideEffect.ShowSnackBar -> showSnackBar(effect.message)
            }
        }
    }

    BackHandler { viewModel.onCloseClicked() }

    QuestRecordingCompleteScreen(
        uiState = uiState,
        paddingValues = paddingValues,
        onCloseClick = viewModel::onCloseClicked,
        modifier = modifier,
    )
}

@Composable
private fun QuestRecordingCompleteScreen(
    uiState: QuestRecordingCompleteState,
    paddingValues: PaddingValues,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
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
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = screenWidthDp(24.dp)),
            horizontalArrangement = Arrangement.End,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_cancel),
                contentDescription = null,
                tint = ByeBooTheme.colors.white,
                modifier = Modifier.clickable(onClick = onCloseClick),
            )
        }

        Spacer(modifier = Modifier.height(screenHeightDp(24.dp)))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding =
                PaddingValues(
                    start = screenWidthDp(24.dp),
                    end = screenWidthDp(24.dp),
                    bottom = screenHeightDp(24.dp),
                ),
        ) {


            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(screenHeightDp(20.dp)),
                ) {
                    QuestTitle(
                        stepNumber = uiState.stepNumber,
                        questNumber = uiState.questNumber,
                        createdAt = uiState.createdAt,
                        questQuestion = uiState.question
                    )

                    ContentText(
                        text = uiState.answer
                    )

                    QuestEmotionDescriptionContent(
                        questEmotionDescription = uiState.emotionDescription,
                        emotionType = uiState.selectedEmotion,
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    ByeBooButton(
                        buttonText = "보리에게 답장 받기",
                        buttonTextColor = ByeBooTheme.colors.white,
                        buttonStyle = ByeBooTheme.typography.body2,
                        buttonBackgroundColor = ByeBooTheme.colors.primary300,
                        onClick = {}
                    )
                }
            }
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
