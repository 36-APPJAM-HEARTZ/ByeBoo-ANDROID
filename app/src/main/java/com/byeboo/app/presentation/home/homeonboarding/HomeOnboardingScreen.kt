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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeOnboardingRoute(
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    bottomPadding: Dp
) {
    HomeOnboardingScreen(
        navigateToHome = navigateToHome,
        modifier = modifier,
        bottomPadding = bottomPadding
    )
}

@Composable
private fun HomeOnboardingScreen(
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    bottomPadding: Dp
) {
    var showSpeechBubble by remember { mutableStateOf(false) }
    var showInstructionText by remember { mutableStateOf(false) }
    var isTransitioning by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current

    val transitionAlpha by animateFloatAsState(
        targetValue = if (isTransitioning) 0.85f else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "fadeBlack"
    )

    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.bori_onboarding)
    )
    val isLottieReady = composition != null

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = isLottieReady && !isTransitioning,
        speed = 1.0f,
        restartOnPlay = false
    )


    LaunchedEffect(isLottieReady) {
        if (isLottieReady) {
            showSpeechBubble = true
            delay(2000)
            showInstructionText = true
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.bg_userinfo),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ByeBooTheme.colors.blackAlpha80)
        )

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = screenWidthDp(48.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            AnimatedVisibility(
                visible = showInstructionText,
                enter = slideInVertically(
                    animationSpec = tween(1000),
                    initialOffsetY = { it }
                )
            ) {
                Text(
                    text = "보리를 꾸욱 눌러주세요!",
                    style = ByeBooTheme.typography.body3,
                    color = ByeBooTheme.colors.whiteAlpha50,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                )
            }

            AnimatedVisibility(
                visible = showSpeechBubble,
                enter = fadeIn(animationSpec = tween(1000))
            ) {
                SpeechBubbleWithText(
                    firstText = "바이부에 오신 걸 환영해요!",
                    secondText = "저는 보리라고 해요.",
                    thirdText = "여정을 시작하러 가볼까요?"
                )
            }

            if (isLottieReady) {
                LottieAnimation(
                    composition = composition,
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = screenHeightDp(89.dp) + bottomPadding)
                        .then(
                            if (showInstructionText) {
                                Modifier.noRippleCombineClickable(
                                    onLongClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                        isTransitioning = true
                                        scope.launch {
                                            delay(500)
                                            navigateToHome()
                                        }
                                    }
                                )
                            } else Modifier
                        )
                        .aspectRatio(1f)
                )
            }
        }

        if (isTransitioning) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ByeBooTheme.colors.black.copy(alpha = transitionAlpha))
            )
        }
    }
}
