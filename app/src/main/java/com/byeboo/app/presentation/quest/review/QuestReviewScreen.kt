package com.byeboo.app.presentation.quest.review

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.component.text.ContentText
import com.byeboo.app.core.designsystem.event.LocalSnackBarTrigger
import com.byeboo.app.core.designsystem.type.LargeTagType
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.quest.component.card.QuestEmotionDescriptionCard
import com.byeboo.app.presentation.quest.component.text.QuestContent
import com.byeboo.app.presentation.quest.component.text.QuestTitle
import com.byeboo.app.presentation.quest.component.type.QuestContentType
import kotlinx.coroutines.flow.collectLatest

@Composable
fun QuestReviewRoute(
    questId: Long,
    bottomPadding: Dp,
    navigateToBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: QuestReviewViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val showSnackBar = LocalSnackBarTrigger.current

    LaunchedEffect(questId) {
        viewModel.setQuestId(questId)
        viewModel.getQuestRecordedDetail(questId)
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is QuestReviewSideEffect.NavigateToQuest -> navigateToBack()
                is QuestReviewSideEffect.ShowSnackBar -> showSnackBar(effect.message)
            }
        }
    }

    QuestReviewScreen(
        uiState = uiState,
        bottomPadding = bottomPadding,
        navigateToQuest = {},
        navigateToBack = navigateToBack,
        modifier = modifier
    )
}

@Composable
private fun QuestReviewScreen(
    uiState: QuestReviewState,
    bottomPadding: Dp,
    navigateToQuest: () -> Unit,
    navigateToBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = ByeBooTheme.colors.black)
            .padding(horizontal = screenWidthDp(24.dp))
            .padding(bottom = bottomPadding)
    ) {
        Spacer(modifier = Modifier.height(screenHeightDp(67.dp)))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_edit),
                contentDescription = "edit content",
                tint = ByeBooTheme.colors.white,
                modifier = Modifier.clickable(onClick = navigateToQuest) // TODO: 분기 처리할 예정
            )

            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_cancel),
                contentDescription = "cancel button",
                tint = ByeBooTheme.colors.white,
                modifier = Modifier.clickable(onClick = navigateToBack)
            )
        }

        Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                Spacer(modifier = Modifier.height(screenHeightDp(10.dp)))

                QuestTitle(
                    stepNumber = uiState.stepNumber,
                    questNumber = uiState.questNumber,
                    createdAt = uiState.createdAt,
                    questQuestion = uiState.question
                )

                Spacer(modifier = Modifier.height(screenHeightDp(24.dp)))
            }

            if (uiState.imageUrl.isNullOrBlank()) {
                item {
                    QuestContent(
                        titleIcon = QuestContentType.THINKING,
                        titleText = "이렇게 생각했어요",
                        contentText = uiState.answer
                    )
                }
            } else {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_shoe),
                            contentDescription = "title icon",
                            tint = Color.Unspecified
                        )

                        Spacer(modifier = Modifier.width(screenWidthDp(8.dp)))

                        Text(
                            text = "이렇게 완료했어요",
                            color = ByeBooTheme.colors.gray200,
                            style = ByeBooTheme.typography.body2
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(312 / 312f)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        SubcomposeAsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f),
                            model = ImageRequest
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
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        )
                    }
                    if (uiState.answer.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        ContentText(uiState.answer)
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(screenHeightDp(24.dp)))

                QuestEmotionDescriptionContent(
                    questEmotionDescription = uiState.emotionDescription,
                    emotionType = uiState.selectedEmotion
                )

                Spacer(modifier = Modifier.height(screenHeightDp(28.dp)))
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

        Spacer(modifier = Modifier.height(screenHeightDp(24.dp)))
    }
}
