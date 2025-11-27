package com.byeboo.app.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.RenderMode
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.event.LocalSnackBarTrigger
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.core.util.noRippleClickable
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.domain.model.home.HomeStatus
import com.byeboo.app.presentation.home.component.HomeProgressCard
import com.byeboo.app.presentation.home.component.HomeQuestCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@Composable
fun HomeRoute(
    navigateToQuest: () -> Unit,
    navigateToQuestStart: (QuestType?) -> Unit,
    navigateToTutorial: () -> Unit,
    navigateToOffboardingCompletedGuide: () -> Unit,
    navigateToOffboardingNewJourney: () -> Unit,
    bottomPadding: Dp,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    val showSnackBar = LocalSnackBarTrigger.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refresh()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is HomeSideEffect.NavigateToQuest -> navigateToQuest()
                is HomeSideEffect.NavigateToQuestStart -> navigateToQuestStart(null)
                is HomeSideEffect.NavigateToTutorial -> navigateToTutorial()
                is HomeSideEffect.NavigateToOffboardingCompletedGuide -> navigateToOffboardingCompletedGuide()
                is HomeSideEffect.NavigateToOffboardingNewJourney -> navigateToOffboardingNewJourney()
                is HomeSideEffect.ShowSnackBar -> {
                    showSnackBar(effect.message)
                }
            }
        }
    }

    HomeScreen(
        uiState = uiState,
        onClickQuest = viewModel::onClickQuest,
        onClickQuestStart = viewModel::onClickQuestStart,
        onHelpIconClick = viewModel::onHelpIconClicked,
        bottomPadding = bottomPadding,
        onOffboardingNewJourneyClick = viewModel::onOffboardingNewJourneyClicked,
        onLottieClick = viewModel::onLottieClicked
    )
}

@Composable
private fun HomeScreen(
    uiState: HomeUiState,
    onClickQuest: () -> Unit,
    onClickQuestStart: () -> Unit,
    onHelpIconClick: () -> Unit,
    bottomPadding: Dp,
    onOffboardingNewJourneyClick: () -> Unit,
    onLottieClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.bori_home))
    val isReady = composition != null

    var showBubble by remember { mutableStateOf(false) }

    val bottomBubbleText = if (!uiState.isBubbleClicked) {
        when (uiState.status) {
            HomeStatus.INITIAL_START -> "${uiState.nickname}님의 이별 극복을 도와드릴게요"
            HomeStatus.TODAY_INCOMPLETE -> "${uiState.nickname}님만의 속도로 나아가봐요"
            HomeStatus.TODAY_COMPLETE -> "오늘도 잘 이겨내셨어요!"
            HomeStatus.JOURNEY_COMPLETE -> "저는 언제나 여기에 있어요!"
        }
    } else {
        when (uiState.status) {
            HomeStatus.INITIAL_START -> "저는 ${uiState.nickname}님을 도와드릴 보리예요"
            HomeStatus.TODAY_INCOMPLETE -> "앗! 저를 부르셨나요?"
            HomeStatus.TODAY_COMPLETE -> "저는 항상 ${uiState.nickname}님을 응원하고 있어요!"
            HomeStatus.JOURNEY_COMPLETE -> "힘들 때 언제나 저를 찾아주세요"
        }
    }

    LaunchedEffect(isReady, uiState.status, uiState.hasSeenAboutHelp) {
        if (isReady && uiState.status == HomeStatus.INITIAL_START && !uiState.hasSeenAboutHelp) {
            delay(300)
            showBubble = true
        } else {
            showBubble = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.bg_home),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        val canShowContent = isReady && !uiState.isLoading
        val canInteract = !uiState.isLoading && !uiState.hasError
        if (!canShowContent) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            return@Box
        }
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(tween(300, easing = FastOutSlowInEasing)) +
                    scaleIn(
                        initialScale = 0.98f,
                        animationSpec = tween(400, easing = FastOutSlowInEasing)
                    )
        ) {
            Box(Modifier.fillMaxSize()) {
                Column(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(horizontal = screenWidthDp(24.dp))
                        .padding(top = screenHeightDp(67.dp))
                ) {
                    when (uiState.status) {
                        HomeStatus.INITIAL_START -> {
                            HomeQuestCard(
                                title = "${uiState.journey} 여정 시작하기",
                                subtitle = "제가 옆에서 함께할게요!",
                                onClick = { if (canInteract) onClickQuestStart() }
                            )
                            Spacer(Modifier.height(screenHeightDp(16.dp)))
                            Icon(
                                imageVector = ImageVector.vectorResource(
                                    id = R.drawable.ic_home_question
                                ),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .noRippleClickable {
                                        onHelpIconClick()
                                        showBubble = false
                                    }
                            )
                            Spacer(Modifier.height(screenHeightDp(4.dp)))
                            AnimatedVisibility(
                                visible = showBubble && !uiState.hasSeenAboutHelp,
                                enter = fadeIn(tween(220)) +
                                        scaleIn(
                                            initialScale = 0.96f,
                                            animationSpec = tween(300, easing = FastOutSlowInEasing)
                                        ),
                                modifier = Modifier.align(Alignment.End)
                            ) {
                                Image(
                                    imageVector = ImageVector.vectorResource(
                                        id = R.drawable.ic_home_about_bori
                                    ),
                                    contentDescription = "보리 소개 말풍선"
                                )
                            }
                        }

                        HomeStatus.TODAY_INCOMPLETE -> {
                            HomeQuestCard(
                                title = "오늘의 퀘스트 하러가기",
                                subtitle = "퀘스트를 하고나면 한층 더 성장할 거에요.",
                                onClick = onClickQuest
                            )

                            Spacer(Modifier.height(screenHeightDp(16.dp)))

                            HomeProgressCard(
                                title = "${uiState.nickname}님의 ${uiState.journey} 여정",
                                currentStep = uiState.currentStep,
                                totalSteps = uiState.totalSteps
                            )
                        }

                        HomeStatus.TODAY_COMPLETE -> {
                            HomeQuestCard(
                                title = "오늘의 퀘스트 완료!",
                                subtitle = "잘하셨어요! 내일 또 만나요.",
                                onClick = onClickQuest,
                                borderColor = Color.Unspecified
                            )

                            Spacer(Modifier.height(screenHeightDp(16.dp)))

                            HomeProgressCard(
                                title = "${uiState.nickname}님의 ${uiState.journey} 여정",
                                currentStep = uiState.currentStep,
                                totalSteps = uiState.totalSteps
                            )
                        }

                        HomeStatus.JOURNEY_COMPLETE -> {
                            HomeQuestCard(
                                title = "새로운 이별 극복 여정 시작하기",
                                subtitle = "다음 여정도, 제가 곁에서 함께할게요.",
                                onClick = onOffboardingNewJourneyClick
                            )
                        }
                    }
                    Spacer(Modifier.height(screenHeightDp(16.dp)))
                }

                // 하단 말풍선 + 로띠
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .padding(horizontal = screenWidthDp(24.dp))
                        .padding(bottom = maxOf(bottomPadding - screenHeightDp(20.dp), 0.dp))
                ) {
                    // 하단 말풍선
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(312f / 62f)
                    ) {
                        Image(
                            imageVector = ImageVector.vectorResource(
                                id = R.drawable.ic_home_speech_bubble
                            ),
                            contentDescription = null,
                            modifier = Modifier.matchParentSize()
                        )

                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .padding(bottom = screenHeightDp(14.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column {
                                AnimatedVisibility(
                                    visible = uiState.showBubble,
                                    enter = fadeIn(tween (durationMillis = 500, easing = LinearOutSlowInEasing)),
                                    exit = fadeOut(tween (500, easing = FastOutLinearInEasing))
                                ) {
                                    Text(
                                        text = bottomBubbleText,
                                        style = ByeBooTheme.typography.body2,
                                        color = ByeBooTheme.colors.primary50,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(screenHeightDp(34.dp)))

                    LottieAnimation(
                        composition = composition,
                        iterations = LottieConstants.IterateForever,
                        contentScale = ContentScale.Crop,
                        renderMode = RenderMode.AUTOMATIC,
                        enableMergePaths = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .noRippleClickable(onClick = {
                                if (uiState.isBubbleEnabled) {
                                    onLottieClick()
                                }
                            }
                            )
                    )
                }
            }
        }
    }
}
