package com.byeboo.app.presentation.offboarding.offboardingcompletedguide

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector4D
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.component.button.ByeBooButton
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.noRippleClickable
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.presentation.offboarding.component.OffboardingNewJourneyButton
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OffboardingCompletedGuideRoute(
    navigateToHome: () -> Unit,
    navigateToOffboardingNewJourney: () -> Unit,
    navigateToOffboardingCompletedJourney: () -> Unit,
    bottomPadding: Dp,
    modifier: Modifier = Modifier,
    viewModel: OffboardingCompletedGuideViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isInitialAnimation by viewModel.isInitialAnimation.collectAsStateWithLifecycle()
    val animation = !isInitialAnimation

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is OffboardingCompletedGuideSideEffect.NavigateToHome -> navigateToHome()
                is OffboardingCompletedGuideSideEffect.NavigateToOffboardingNewJourney -> navigateToOffboardingNewJourney()
                is OffboardingCompletedGuideSideEffect.NavigateToOffboardingCompletedJourney -> navigateToOffboardingCompletedJourney()
            }
        }
    }

    BackHandler {
        viewModel.onCloseClicked()
    }

    OffboardingCompleteGuideScreen(
        uiState = uiState,
        bottomPadding = bottomPadding,
        onCloseClick = viewModel::onCloseClicked,
        onNewJourneyClick = viewModel::onNewJourneyClicked,
        onCompletedJourneyClick = viewModel::onCompletedJourneyClicked,
        animation = animation,
        modifier = modifier
    )
}

@Composable
private fun OffboardingCompleteGuideScreen(
    uiState: OffboardingCompletedGuideState,
    bottomPadding: Dp,
    onCloseClick: () -> Unit,
    onNewJourneyClick: () -> Unit,
    onCompletedJourneyClick: () -> Unit,
    animation: Boolean,
    modifier: Modifier = Modifier
) {
    var index by remember { mutableIntStateOf(0) }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.bori_cake))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_userinfo),
            contentDescription = "",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = ByeBooTheme.colors.blackAlpha80)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
                    .padding(top = 67.dp, bottom = bottomPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_cancel),
                    contentDescription = "",
                    tint = ByeBooTheme.colors.white,
                    modifier = Modifier
                        .size(24.dp)
                        .align(Alignment.End)
                        .noRippleClickable(onCloseClick)
                )

                Spacer(modifier = Modifier.height(screenHeightDp(34.dp)))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(156.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🎉${uiState.journeyName} 여정을 완료했어요!🎉",
                        color = ByeBooTheme.colors.secondary300,
                        style = ByeBooTheme.typography.sub2
                    )

                    if (animation) {
                        TextSequence(
                            paragraphs = listOf(
                                "무려 30개의 퀘스트를 완료했어요.\n끝까지 포기하지 않고 극복하기 위해 노력한\n${uiState.nickname}님이 너무 대단해요.",
                                "지금의 ${uiState.nickname}님은, 처음보다 성장했을 거예요.",
                                "만약 아직 정리되지 못한 감정이 남아있다면,\n또 다른 새로운 여정을 시작해 볼까요?"
                            ),
                            index = index,
                            gap = 16.dp,
                            topGap = 32.dp,
                            onAdvance = { nextIndex -> index = nextIndex })
                    } else {
                        Spacer(Modifier.height(32.dp))
                        Text(
                            text = "만약 아직 정리되지 못한 감정이 남아있다면,\n또 다른 새로운 여정을 시작해 볼까요?",
                            style = ByeBooTheme.typography.body3,
                            color = ByeBooTheme.colors.secondary50,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    LottieAnimation(
                        composition = composition,
                        progress = progress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 34.dp)
                    )
                }

                OffboardingNewJourneyButton(onClick = onNewJourneyClick)

                Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

                ByeBooButton(
                    onClick = onCompletedJourneyClick,
                    buttonText = "완료한 여정 다시보기",
                    buttonStyle = ByeBooTheme.typography.body2,
                    buttonTextColor = ByeBooTheme.colors.primary400,
                    buttonBackgroundColor = ByeBooTheme.colors.primary50,
                )
                Spacer(modifier = Modifier.height(screenHeightDp(8.dp)))
            }
        }
    }
}

@Composable
fun TextSequence(
    paragraphs: List<String>,
    index: Int,
    gap: Dp,
    topGap: Dp,
    modifier: Modifier = Modifier,
    onAdvance: (Int) -> Unit = {}
) {
    val firstSentence = paragraphs.getOrNull(index) ?: return
    val secondSentence = paragraphs.getOrNull(index + 1)
    val thirdSentence = paragraphs.getOrNull(index + 2)

    if (secondSentence == null || thirdSentence == null) {
        Spacer(modifier = Modifier.height(topGap))

        Text(
            text = firstSentence,
            style = ByeBooTheme.typography.body3,
            color = ByeBooTheme.colors.secondary50,
            textAlign = TextAlign.Center
        )
        return
    }

    Spacer(modifier = Modifier.height(topGap))

    Animation(
        firstSentence = firstSentence,
        secondSentence = secondSentence,
        thirdSentence = thirdSentence,
        gap = gap,
        mainSentenceColor = ByeBooTheme.colors.secondary50,
        subSentenceColor = ByeBooTheme.colors.secondary50.copy(alpha = 0.5f),
        onFinished = {
            if (index + 3 < paragraphs.size) {
                onAdvance(index + 1)
            }
        },
        modifier = modifier
    )
}

@Composable
private fun Animation(
    firstSentence: String,
    secondSentence: String,
    thirdSentence: String,
    gap: Dp,
    mainSentenceColor: Color,
    subSentenceColor: Color,
    onFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val measurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val gapPx = with(density) { gap.toPx() }

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val availableWidth = with(density) { (screenWidth - 24.dp * 2).roundToPx() }


    val textStyle = ByeBooTheme.typography.cap2.copy(
        platformStyle = PlatformTextStyle(includeFontPadding = false),
        lineHeightStyle = LineHeightStyle(
            alignment = LineHeightStyle.Alignment.Top,
            trim = LineHeightStyle.Trim.Both
        )
    )

    fun measureHeight(t: String) = measurer.measure(
        AnnotatedString(t),
        style = textStyle,
        constraints = Constraints(maxWidth = availableWidth)
    ).size.height.toFloat()

    val height1 = measureHeight(firstSentence)
    val height2 = measureHeight(secondSentence)
    val height3 = measureHeight(thirdSentence)

    val activeScale = 1.3f
    val maxH = maxOf(height1, height2, height3)
    val lineBoxH = maxH * activeScale
    val totalH = lineBoxH * 3 + gapPx * 2
    val totalHdp = with(density) { totalH.toDp() }

    val base1 = 0f
    val base2 = lineBoxH + gapPx

    val shift1 = remember { Animatable(0f) }
    val alpha1 = remember { Animatable(1f) }
    val scale1 = remember { Animatable(activeScale) }

    val colorToVector = TwoWayConverter(
        { c: Color -> AnimationVector4D(c.red, c.green, c.blue, c.alpha) },
        { v: AnimationVector4D -> Color(v.v1, v.v2, v.v3, v.v4) }
    )
    val color2 = remember { Animatable(subSentenceColor, colorToVector) }
    val color3 = remember { Animatable(subSentenceColor, colorToVector) }

    val shift2 = remember { Animatable(0f) }
    val alpha2 = remember { Animatable(1f) }
    val scale2 = remember { Animatable(1f) }

    val shift3 = remember { Animatable(0f) }
    val alpha3 = remember { Animatable(0f) }
    val scale3 = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        delay(2000)
        coroutineScope {
            launch { shift1.animateTo(-20f, tween(500, easing = FastOutSlowInEasing)) }
            launch { alpha1.animateTo(0f, tween(500, easing = FastOutSlowInEasing)) }

            launch { shift2.animateTo(-base2, tween(1000, easing = FastOutSlowInEasing)) }
            launch { scale2.animateTo(activeScale, tween(1000, easing = FastOutSlowInEasing)) }
            launch {
                color2.animateTo(
                    mainSentenceColor,
                    tween(1000, easing = FastOutSlowInEasing)
                )
            }

            launch { alpha3.animateTo(1f, tween(1000, easing = FastOutSlowInEasing)) }
            launch { shift3.snapTo(0f) }
        }

        delay(1000)
        val anchorForThird = (base2 + shift2.value) + (height2 * scale2.value) + gapPx

        coroutineScope {
            launch { shift2.animateTo(-base2 - 20f, tween(500, easing = FastOutSlowInEasing)) }
            launch { alpha2.animateTo(0f, tween(500, easing = FastOutSlowInEasing)) }

            launch { shift3.animateTo(-anchorForThird, tween(1000, easing = FastOutSlowInEasing)) }
            launch { scale3.animateTo(activeScale, tween(1000, easing = FastOutSlowInEasing)) }
            launch {
                color3.animateTo(
                    mainSentenceColor,
                    tween(1000, easing = FastOutSlowInEasing)
                )
            }
        }

        onFinished()
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(totalHdp)
    ) {
        @Composable
        fun Line(
            text: String,
            baseTop: Float,
            shift: Float,
            alpha: Float,
            scale: Float,
            color: Color
        ) {
            Text(
                text = text,
                style = ByeBooTheme.typography.cap2,
                color = color,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        translationY = baseTop + shift
                        scaleX = scale
                        scaleY = scale
                        this.alpha = alpha
                        transformOrigin = TransformOrigin(0.5f, 0f)
                    }
            )
        }

        val line2Top = base2 + shift2.value

        Line(firstSentence, base1, shift1.value, alpha1.value, scale1.value, mainSentenceColor)

        Line(secondSentence, base2, shift2.value, alpha2.value, scale2.value, color2.value)

        Text(
            text = thirdSentence,
            style = ByeBooTheme.typography.cap2,
            color = color3.value,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    translationY = line2Top + (height2 * scale2.value) + gapPx + shift3.value
                    scaleX = scale3.value
                    scaleY = scale3.value
                    alpha = alpha3.value
                    transformOrigin = TransformOrigin(0.5f, 0f)
                }
        )
    }
}
