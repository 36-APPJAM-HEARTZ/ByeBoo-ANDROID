package com.byeboo.app.presentation.quest.review.my

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.byeboo.app.core.designsystem.component.LoadingScreen
import com.byeboo.app.core.designsystem.component.button.ByeBooButton
import com.byeboo.app.core.designsystem.component.text.ContentText
import com.byeboo.app.core.designsystem.event.LocalSnackBarTrigger
import com.byeboo.app.core.designsystem.type.EmotionChipType
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.quest.component.card.QuestEmotionDescriptionCard
import com.byeboo.app.presentation.quest.component.text.QuestTitle
import com.byeboo.app.presentation.quest.navigation.AiAnswerOrigin
import com.byeboo.app.presentation.quest.review.my.component.QuestReviewTopbar
import kotlinx.coroutines.flow.collectLatest

@Composable
fun QuestReviewRoute(
    paddingValues: PaddingValues,
    navigateToQuest: () -> Unit,
    navigateToQuestRecordingEdit: (Long, Boolean) -> Unit,
    navigateToQuestBehaviorEdit: (Long, Boolean, String) -> Unit,
    navigateToQuestAiAnswer: (Long, Boolean, AiAnswerOrigin) -> Unit,
    viewModel: QuestReviewViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val showSnackBar = LocalSnackBarTrigger.current

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is QuestReviewSideEffect.NavigateToQuest -> navigateToQuest()
                is QuestReviewSideEffect.NavigateToQuestRecordingEdit ->
                    navigateToQuestRecordingEdit(
                        effect.questId,
                        true,
                    )

                is QuestReviewSideEffect.NavigateToQuestBehaviorEdit ->
                    navigateToQuestBehaviorEdit(
                        effect.questId,
                        true,
                        effect.imageKey,
                    )

                is QuestReviewSideEffect.NavigateToQuestAiAnswer ->
                    navigateToQuestAiAnswer(
                        effect.questId,
                        effect.isExistedAiAnswer,
                        effect.aiAnswerOrigin,
                    )

                is QuestReviewSideEffect.ShowSnackBar -> showSnackBar(effect.snackBarType)
            }
        }
    }

    BackHandler {
        navigateToQuest()
    }

    if (uiState.isLoading) {
        LoadingScreen()
    } else {
        QuestReviewScreen(
            uiState = uiState,
            paddingValues = paddingValues,
            onBackClick = viewModel::onBackClicked,
            onEditClick = { viewModel.onEditClicked(uiState.questType) },
            onAiAnswerClick = viewModel::onAiAnswerClicked,
        )
    }
}

@Composable
private fun QuestReviewScreen(
    uiState: QuestReviewState,
    paddingValues: PaddingValues,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onAiAnswerClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    val isScrollStateReady by remember {
        androidx.compose.runtime.derivedStateOf {
            listState.layoutInfo.totalItemsCount > 0
        }
    }
    val hasScroll by remember {
        androidx.compose.runtime.derivedStateOf {
            listState.canScrollForward || listState.canScrollBackward
        }
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
        QuestReviewTopbar(
            onBackClick = onBackClick,
            onEditClick = onEditClick,
        )

        Box(
            modifier =
                Modifier
                    .fillMaxSize(),
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding =
                    PaddingValues(
                        start = screenWidthDp(24.dp),
                        end = screenWidthDp(24.dp),
                        bottom = if (isScrollStateReady && hasScroll) screenHeightDp(28.dp) else screenHeightDp(90.dp),
                    ),
            ) {
                item {
                    QuestTitle(
                        stepNumber = uiState.stepNumber,
                        questNumber = uiState.questNumber,
                        createdAt = uiState.createdAt,
                        questQuestion = uiState.question,
                    )

                    Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))
                }

                if (uiState.imageUrl.isNullOrBlank()) {
                    item {
                        ContentText(text = uiState.answer)
                    }
                } else {
                    item {
                        Column(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(12.dp)),
                        ) {
                            SubcomposeAsyncImage(
                                modifier = Modifier.fillMaxWidth(),
                                model =
                                    ImageRequest
                                        .Builder(LocalContext.current)
                                        .data(uiState.imageUrl)
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

                        if (uiState.answer.isNotBlank()) {
                            Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))
                            ContentText(uiState.answer)
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))

                    QuestEmotionDescriptionContent(
                        questEmotionDescription = uiState.emotionDescription,
                        emotionType = uiState.selectedEmotion,
                    )
                }

                if (isScrollStateReady && hasScroll) {
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

            if (isScrollStateReady && !hasScroll) {
                ByeBooButton(
                    buttonText = if (uiState.isExistedAiAnswer) "보리의 답장 보러가기" else "보리에게 답장받기",
                    buttonTextColor = ByeBooTheme.colors.white,
                    buttonStyle = ByeBooTheme.typography.body2,
                    buttonBackgroundColor = ByeBooTheme.colors.primary300,
                    onClick = onAiAnswerClick,
                    modifier =
                        Modifier
                            .align(Alignment.BottomCenter)
                            .padding(horizontal = screenWidthDp(24.dp))
                            .padding(bottom = screenHeightDp(10.dp)),
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
