package com.byeboo.app.presentation.quest.record

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
import com.byeboo.app.core.designsystem.component.tag.SmallTag
import com.byeboo.app.core.designsystem.event.LocalSnackBarTrigger
import com.byeboo.app.core.designsystem.type.LargeTagType
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.findActivity
import com.byeboo.app.core.util.inAppReview
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.quest.component.card.QuestCompleteCard
import com.byeboo.app.presentation.quest.component.card.QuestEmotionDescriptionCard
import com.byeboo.app.presentation.quest.component.text.CreatedText
import com.byeboo.app.presentation.quest.component.text.QuestContent
import com.byeboo.app.presentation.quest.component.type.QuestContentType

@Composable
fun QuestRecordingCompleteRoute(
    navigateToQuest: () -> Unit,
    navigateToOffboardingCompletedGuide: () -> Unit,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
    viewModel: QuestRecordingCompleteViewModel = hiltViewModel()
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
        modifier = modifier
    )
}

@Composable
private fun QuestRecordingCompleteScreen(
    uiState: QuestRecordingCompleteState,
    paddingValues: PaddingValues,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = ByeBooTheme.colors.black)
            .padding(
                top = paddingValues.calculateTopPadding() + screenHeightDp(27.dp),
                bottom = paddingValues.calculateBottomPadding()
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = screenHeightDp(24.dp)),
            horizontalArrangement = Arrangement.End
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_cancel),
                contentDescription = null,
                tint = ByeBooTheme.colors.white,
                modifier = Modifier.clickable(onClick = onCloseClick)
            )
        }

        Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(
                start = screenWidthDp(24.dp),
                top = screenHeightDp(8.dp),
                end = screenWidthDp(24.dp),
                bottom = screenHeightDp(24.dp)
            )
        ) {
            item {
                QuestCompleteCard(
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                Spacer(modifier = Modifier.height(screenHeightDp(32.dp)))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        SmallTag(
                            tagText = "STEP ${uiState.stepNumber}",
                            tagColor = ByeBooTheme.colors.gray500
                        )

                        Spacer(modifier = Modifier.width(screenWidthDp(8.dp)))

                        Text(
                            text = "${uiState.questNumber}번째 퀘스트",
                            style = ByeBooTheme.typography.body6,
                            color = ByeBooTheme.colors.gray500
                        )
                    }

                    Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))

                    CreatedText(uiState.createdAt)

                    Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))

                    Text(
                        text = uiState.question,
                        style = ByeBooTheme.typography.head1,
                        color = ByeBooTheme.colors.gray100,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(screenHeightDp(24.dp)))

                    QuestContent(
                        titleIcon = QuestContentType.THINKING,
                        titleText = "이렇게 생각했어요",
                        contentText = uiState.answer
                    )

                    Spacer(modifier = Modifier.height(screenHeightDp(24.dp)))

                    QuestEmotionDescriptionContent(
                        questEmotionDescription = uiState.emotionDescription,
                        emotionType = uiState.selectedEmotion
                    )
                }
            }
        }
    }
}

@Composable
private fun QuestEmotionDescriptionContent(
    questEmotionDescription: String,
    emotionType: LargeTagType,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_change),
                contentDescription = "title icon",
                tint = Color.Unspecified
            )

            Spacer(modifier = Modifier.width(screenWidthDp(8.dp)))

            Text(
                text = "퀘스트 완료 후, 이런 감정을 느꼈어요",
                color = ByeBooTheme.colors.gray200,
                style = ByeBooTheme.typography.body2
            )
        }

        Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))

        QuestEmotionDescriptionCard(
            questEmotionDescription = questEmotionDescription,
            emotionType = emotionType
        )
    }
}
