package com.byeboo.app.presentation.quest.behavior.complete

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.component.tag.SmallTag
import com.byeboo.app.core.designsystem.component.text.ContentText
import com.byeboo.app.core.designsystem.event.LocalSnackBarTrigger
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.findActivity
import com.byeboo.app.core.util.inAppReview
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.quest.component.card.QuestCompleteCard
import com.byeboo.app.presentation.quest.component.card.QuestEmotionDescriptionCard
import com.byeboo.app.presentation.quest.component.text.CreatedText

@Composable
fun QuestBehaviorCompleteRoute(
    navigateToQuest: () -> Unit,
    navigateToOffboardingCompletedGuide: () -> Unit,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
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
                is QuestBehaviorCompleteSideEffect.ShowInAppReview -> {
                    activity?.let { activity ->
                        inAppReview(activity)
                    }
                }
                is QuestBehaviorCompleteSideEffect.ShowSnackBar -> showSnackBar(effect.message)
            }
        }
    }

    BackHandler { viewModel.onCloseClicked() }

    QuestBehaviorCompleteScreen(
        uiState = uiState,
        paddingValues = paddingValues,
        onCloseClick = viewModel::onCloseClicked,
        imageUri = imageUri,
        modifier = modifier,
    )
}

@Composable
private fun QuestBehaviorCompleteScreen(
    uiState: QuestBehaviorCompleteState,
    paddingValues: PaddingValues,
    onCloseClick: () -> Unit,
    imageUri: Uri?,
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
                contentDescription = "back button",
                tint = ByeBooTheme.colors.white,
                modifier = modifier.clickable(onClick = onCloseClick),
            )
        }

        Spacer(modifier = modifier.height(screenHeightDp(16.dp)))

        LazyColumn(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding =
                PaddingValues(
                    start = screenWidthDp(24.dp),
                    top = screenHeightDp(8.dp),
                    end = screenWidthDp(24.dp),
                    bottom = screenHeightDp(24.dp),
                ),
        ) {
            item {
                QuestCompleteCard(
                    modifier = modifier.fillMaxWidth(),
                )

                Spacer(modifier = modifier.height(screenHeightDp(32.dp)))
            }

            item {
                Column(
                    modifier = modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    Row(
                        modifier = modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        SmallTag(
                            tagText = "STEP ${uiState.stepNumber}",
                            tagColor = ByeBooTheme.colors.gray500,
                        )

                        Spacer(modifier = modifier.width(screenWidthDp(8.dp)))

                        Text(
                            text = "${uiState.questNumber}번째 퀘스트",
                            style = ByeBooTheme.typography.body6,
                            color = ByeBooTheme.colors.gray500,
                        )
                    }

                    Spacer(modifier = modifier.height(screenHeightDp(12.dp)))

                    CreatedText(uiState.createdAt)

                    Spacer(modifier = modifier.height(screenHeightDp(12.dp)))

                    Text(
                        text = uiState.question,
                        style = ByeBooTheme.typography.head1,
                        color = ByeBooTheme.colors.gray100,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )

                    Spacer(modifier = modifier.height(screenHeightDp(24.dp)))
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_shoe),
                        contentDescription = "title icon",
                        tint = Color.Unspecified,
                    )

                    Spacer(modifier = Modifier.width(screenWidthDp(8.dp)))

                    Text(
                        text = "이렇게 완료했어요",
                        color = ByeBooTheme.colors.gray200,
                        style = ByeBooTheme.typography.body2,
                    )
                }

                Spacer(modifier = modifier.height(screenHeightDp(12.dp)))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Column(
                        modifier =
                            modifier
                                .fillMaxWidth()
                                .aspectRatio(312 / 312f)
                                .clip(RoundedCornerShape(12.dp)),
                    ) {
                        if (imageUri != null) {
                            SubcomposeAsyncImage(
                                model =
                                    ImageRequest
                                        .Builder(LocalContext.current)
                                        .data(imageUri)
                                        .memoryCachePolicy(CachePolicy.DISABLED)
                                        .diskCachePolicy(CachePolicy.DISABLED)
                                        .build(),
                                contentDescription = "uploaded image",
                                modifier = modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop,
                                loading = {
                                    Box(
                                        modifier = modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        CircularProgressIndicator()
                                    }
                                },
                            )
                        }
                    }

                    if (uiState.questAnswer.isNotBlank()) {
                        Spacer(modifier = modifier.height(screenHeightDp(12.dp)))

                        ContentText(uiState.questAnswer)
                    }

                    Spacer(modifier = modifier.height(screenHeightDp(24.dp)))
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_change),
                        contentDescription = "title icon",
                        tint = Color.Unspecified,
                    )

                    Spacer(modifier = Modifier.width(screenWidthDp(8.dp)))

                    Text(
                        text = "퀘스트 완료 후, 이런 감정을 느꼈어요",
                        color = ByeBooTheme.colors.gray200,
                        style = ByeBooTheme.typography.body2,
                    )
                }

                Spacer(modifier = modifier.height(screenHeightDp(12.dp)))

                uiState.selectedEmotion?.let { emotion ->
                    QuestEmotionDescriptionCard(
                        questEmotionDescription = uiState.emotionDescription,
                        emotionType = emotion,
                    )
                }
            }
        }
    }
}
