package com.byeboo.app.presentation.home.homeonboarding

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.noRippleCombineClickable
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.home.component.HomeTextCard
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
    var showFirstText by remember { mutableStateOf(false) }
    var showSecondText by remember { mutableStateOf(false) }
    var showSpeechBubble by remember { mutableStateOf(false) }
    var isTransitioning by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current

    val transitionAlpha by animateFloatAsState(
        targetValue = if (isTransitioning) 0.85f else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "fadeBlack"
    )

    LaunchedEffect(Unit) {
        delay(700)
        showFirstText = true
        delay(700)
        showSecondText = true
        delay(700)
        showSpeechBubble = true
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
                .padding(horizontal = screenWidthDp(24.dp))
                .padding(top = screenHeightDp(67.dp)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedVisibility(
                visible = showFirstText,
                enter = fadeIn(animationSpec = tween(durationMillis = 1000))
            ) {
                HomeTextCard(title = "BYE BOO에 오신 걸 환영해요 :)")
            }

            Spacer(modifier = Modifier.padding(vertical = screenHeightDp(8.dp)))

            AnimatedVisibility(
                visible = showSecondText,
                enter = fadeIn(animationSpec = tween(durationMillis = 1000))
            ) {
                HomeTextCard(title = "저는 보리라고 해요.")
            }

            Spacer(modifier = Modifier.weight(1f))

            AnimatedVisibility(
                visible = showSpeechBubble,
                enter = fadeIn(animationSpec = tween(durationMillis = 1000))
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "보리를 꾹 눌러주세요!",
                        style = ByeBooTheme.typography.body3,
                        color = ByeBooTheme.colors.whiteAlpha50,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.speech_bubble),
                        contentDescription = "말풍선",
                        modifier = Modifier
                            .wrapContentSize()
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }

            Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

            Image(
                painter = painterResource(id = R.drawable.home_onboarding),
                contentDescription = "보리 캐릭터",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = screenHeightDp(bottomPadding + 89.dp))
                    .then(
                        if (showSpeechBubble) {
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
                        } else {
                            Modifier
                        }
                    ),
                contentScale = ContentScale.Crop
            )
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
