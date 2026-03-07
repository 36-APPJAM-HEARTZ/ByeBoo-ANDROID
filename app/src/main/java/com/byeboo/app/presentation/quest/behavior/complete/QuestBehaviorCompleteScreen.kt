package com.byeboo.app.presentation.quest.behavior.complete

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
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
import com.byeboo.app.presentation.quest.component.card.QuestEmotionDescriptionCard
import com.byeboo.app.presentation.quest.component.text.QuestTitle
import com.byeboo.app.presentation.quest.navigation.AiAnswerOrigin

@Composable
fun QuestBehaviorCompleteRoute(
    navigateToQuest: () -> Unit,
    navigateToOffboardingCompletedGuide: () -> Unit,
    navigateToQuestAiAnswer: (Long, Boolean, AiAnswerOrigin) -> Unit,
    paddingValues: PaddingValues,
    viewModel: QuestBehaviorCompleteViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val showSnackBar = LocalSnackBarTrigger.current
    val context = LocalContext.current
    val activity = context.findActivity()

    val imageUri =
        when {
            uiState.selectedImageUri != null -> uiState.selectedImageUri
            uiState.imageUrl.isNotBlank() -> uiState.imageUrl.toUri()
            else -> null
        }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is QuestBehaviorCompleteSideEffect.NavigateToQuest -> navigateToQuest()
                is QuestBehaviorCompleteSideEffect.NavigateToOffboardingCompletedGuide -> navigateToOffboardingCompletedGuide()
                is QuestBehaviorCompleteSideEffect.NavigateToQuestAiAnswer ->
                    navigateToQuestAiAnswer(
                        effect.questId,
                        effect.isExistedAiAnswer,
                        effect.aiAnswerOrigin,
                    )
                is QuestBehaviorCompleteSideEffect.ShowInAppReview -> {
                    activity?.let { activity ->
                        inAppReview(activity)
                    }
                }

                is QuestBehaviorCompleteSideEffect.ShowSnackBar -> showSnackBar(effect.snackBarType)
            }
        }
    }

    BackHandler { viewModel.onCloseClicked() }

    QuestBehaviorCompleteScreen(
        uiState = uiState,
        paddingValues = paddingValues,
        onCloseClick = viewModel::onCloseClicked,
        imageUri = imageUri,
        onAiAnswerClick = viewModel::onAiAnswerClicked,
    )
}

@Composable
private fun QuestBehaviorCompleteScreen(
    uiState: QuestBehaviorCompleteState,
    paddingValues: PaddingValues,
    onCloseClick: () -> Unit,
    imageUri: Uri?,
    onAiAnswerClick: () -> Unit,
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
        CloseTopbar(
            onCloseClick = onCloseClick,
        )

        LazyColumn(
            modifier = modifier.fillMaxWidth(),
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
                        questQuestion = uiState.question,
                    )
                }
            }

            item {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(12.dp)),
                ) {
                    if (imageUri != null) {
                        SubcomposeAsyncImage(
                            modifier =
                                Modifier
                                    .fillMaxWidth(),
                            model =
                                ImageRequest
                                    .Builder(LocalContext.current)
                                    .data(imageUri)
                                    .memoryCachePolicy(CachePolicy.DISABLED)
                                    .diskCachePolicy(CachePolicy.DISABLED)
                                    .build(),
                            contentDescription = "uploaded image",
                            contentScale = ContentScale.Crop,
                            loading = {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    CircularProgressIndicator()
                                }
                            },
                        )
                    }
                }
                if (uiState.questAnswer.isNotBlank()) {
                    Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))

                    ContentText(uiState.questAnswer)
                }
            }

            item {
                Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))

                QuestEmotionDescriptionContent(
                    questEmotionDescription = uiState.emotionDescription,
                    emotionType = uiState.selectedEmotion,
                )
            }

            item {
                Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))

                ByeBooButton(
                    buttonText = if (uiState.isExistedAiAnswer) "보리의 답장 보러가기" else "보리에게 답장받기",
                    buttonTextColor = ByeBooTheme.colors.white,
                    buttonStyle = ByeBooTheme.typography.body2,
                    buttonBackgroundColor = ByeBooTheme.colors.primary300,
                    onClick = onAiAnswerClick,
                )
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

        Spacer(modifier = Modifier.height(screenHeightDp(24.dp)))
    }
}
