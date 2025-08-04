package com.byeboo.app.presentation.quest.behavior

import android.net.Uri
import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.component.tag.SmallTag
import com.byeboo.app.core.designsystem.component.text.ContentText
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.quest.component.card.QuestCompleteCard
import com.byeboo.app.presentation.quest.component.card.QuestEmotionDescriptionCard
import com.byeboo.app.presentation.quest.component.text.CreatedText


@Composable
fun QuestBehaviorCompleteRoute(
    questId: Long,
    navigateToQuest: () -> Unit,
    bottomPadding: Dp,
    viewModel: QuestBehaviorViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val imageUri = uiState.selectedImageUri ?: uiState.imageUrl.takeIf { it.isNotBlank() }
        ?.let { Uri.parse(it) }

    LaunchedEffect(questId) {
        viewModel.setQuestId(questId)
        viewModel.getQuestRecordedDetail(questId)
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is QuestBehaviorSideEffect.NavigateToQuest -> navigateToQuest()
                else -> ""
            }
        }
    }

    BackHandler { viewModel.onCloseClick() }

    QuestBehaviorCompleteScreen(
        uiState = uiState,
        bottomPadding = bottomPadding,
        onCloseClick = viewModel::onCloseClick,
        imageUri = imageUri
    )
}

@Composable
private fun QuestBehaviorCompleteScreen(
    uiState: QuestBehaviorState,
    bottomPadding: Dp,
    onCloseClick: () -> Unit,
    imageUri: Uri?
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(ByeBooTheme.colors.black)
            .padding(horizontal = screenWidthDp(24.dp))
            .padding(bottom = screenHeightDp(bottomPadding))
    ) {
        Spacer(modifier = Modifier.height(67.dp))

        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_cancel),
            contentDescription = "back button",
            tint = ByeBooTheme.colors.white,
            modifier = Modifier
                .align(Alignment.End)
                .clickable { onCloseClick() }
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))

                QuestCompleteCard(
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
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
                        SmallTag(tagText = "STEP ${uiState.stepNumber}")

                        Spacer(modifier = Modifier.width(screenWidthDp(8.dp)))

                        Text(
                            text = "${uiState.questNumber}번째 퀘스트",
                            style = ByeBooTheme.typography.body2,
                            color = ByeBooTheme.colors.gray500
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    CreatedText(uiState.createdAt)

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = uiState.question,
                        style = ByeBooTheme.typography.head1,
                        color = ByeBooTheme.colors.gray100,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(312 / 312f)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        if (imageUri != null) {
                            SubcomposeAsyncImage(
                                model = ImageRequest
                                    .Builder(LocalContext.current)
                                    .data(imageUri)
                                    .memoryCachePolicy(coil.request.CachePolicy.DISABLED)
                                    .diskCachePolicy(coil.request.CachePolicy.DISABLED)
                                    .build(),
                                contentDescription = "uploaded image",
                                modifier = Modifier.fillMaxSize(),
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
                    }

                    if (uiState.answer.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        ContentText(uiState.answer)
                    }
                    Spacer(modifier = Modifier.height(21.dp))
                }
            }

            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_change),
                        contentDescription = "title icon",
                        modifier = Modifier.padding(end = 8.dp),
                        tint = Color.Unspecified
                    )

                    Text(
                        text = "퀘스트 완료 후, 이런 감정을 느꼈어요",
                        color = ByeBooTheme.colors.gray200,
                        style = ByeBooTheme.typography.body2
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                QuestEmotionDescriptionCard(
                    questEmotionDescription = uiState.emotionDescription,
                    emotionType = uiState.selectedEmotion
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
