package com.byeboo.app.presentation.home.homeonboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.noRippleCombineClickable
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.home.component.SpeechBubbleWithText

@Composable
fun HomeOnboardingRoute(
    navigateToHome: () -> Unit,
    paddingValues: PaddingValues,
    viewModel: HomeOnboardingViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.startOnboardingAnimation()
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is HomeOnboardingSideEffect.NavigateToHome -> navigateToHome()
            }
        }
    }

    HomeOnboardingScreen(
        uiState = uiState,
        onHomeClick = viewModel::onHomeLongClick,
        paddingValues = paddingValues,
    )
}

@Composable
private fun HomeOnboardingScreen(
    uiState: HomeOnboardingUiState,
    onHomeClick: () -> Unit,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val haptic = LocalHapticFeedback.current

    val transitionAlpha by animateFloatAsState(
        targetValue = if (uiState.isTransitioning) 0.85f else 0f,
        animationSpec = tween(500),
        label = "fadeBlack",
    )

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.bori_onboarding),
    )
    val isLottieReady = composition != null
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = isLottieReady && !uiState.isTransitioning,
        speed = 1f,
    )

    val clickableModifier =
        if (uiState.showInstructionText) {
            Modifier.noRippleCombineClickable(
                onLongClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    onHomeClick()
                },
            )
        } else {
            Modifier
        }

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.img_bg_userinfo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )

        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .background(ByeBooTheme.colors.blackAlpha80),
        )

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = screenWidthDp(48.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.weight(1f))

            AnimatedVisibility(
                visible = uiState.showInstructionText,
                enter = slideInVertically(animationSpec = tween(1000)) { it },
            ) {
                Text(
                    text = "보리를 꾸욱 눌러주세요!",
                    style = ByeBooTheme.typography.body3,
                    color = ByeBooTheme.colors.whiteAlpha50,
                    textAlign = TextAlign.Center,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = screenHeightDp(16.dp)),
                )
            }

            if (isLottieReady) {
                AnimatedVisibility(
                    visible = uiState.showSpeechBubble,
                    enter = fadeIn(animationSpec = tween(1000)),
                ) {
                    SpeechBubbleWithText(
                        firstText = "바이부에 오신 걸 환영해요!",
                        secondText = "저는 보리라고 해요.",
                        thirdText = "여정을 시작하러 가볼까요?",
                    )
                }
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                bottom = screenHeightDp(89.dp) + paddingValues.calculateBottomPadding(),
                            ).then(clickableModifier)
                            .aspectRatio(1f),
                )
            }
        }

        if (uiState.isTransitioning) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(ByeBooTheme.colors.black.copy(alpha = transitionAlpha)),
            )
        }
    }
}
