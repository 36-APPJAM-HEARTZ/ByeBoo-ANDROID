package com.byeboo.app.presentation.offboarding.offboardingcompleteguide

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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
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
fun OffboardingCompleteGuideRoute(
    bottomPadding: Dp,
    modifier: Modifier = Modifier,
    viewModel: OffboardingCompleteGuideViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    OffboardingCompleteGuideScreen(
        uiState = uiState,
        bottomPadding = bottomPadding,
        onCloseClick = {},
        onNewJourneyClick = {},
        onCompletedJourneyClick = {},
        modifier = modifier
    )
}

@Composable
private fun OffboardingCompleteGuideScreen(
    uiState: OffboardingCompleteGuideState,
    bottomPadding: Dp,
    onCloseClick: () -> Unit,
    onNewJourneyClick: () -> Unit,
    onCompletedJourneyClick: () -> Unit,
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
                        .requiredHeight(200.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🎉${uiState.journeyName}을 완료했어요!🎉",
                        color = ByeBooTheme.colors.secondary300,
                        style = ByeBooTheme.typography.sub2
                    )

                    TextSequence(
                        paragraphs = listOf(
                            "무려 30개의 퀘스트를 완료했어요.\n끝까지 포기하지 않고 극복하기 위해 노력한\n${uiState.nickname}님이 너무 대단해요.",
                            "지금의 ${uiState.nickname}님은, 처음보다 성장했을 거예요.",
                            "만약 아직 정리되지 못한 감정이 남아있다면,\n또 다른 새로운 여정을 시작해 볼까요?"
                        ),
                        index = index,
                        gap = 16.dp,
                        topGap = 32.dp,
                        onAdvance = { idx -> index = idx }
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    LottieAnimation(
                        composition = composition,
                        progress = progress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    )
                }

                OffboardingNewJourneyButton(
                    onClick = onNewJourneyClick
                )

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
            textAlign = TextAlign.Center,
            softWrap = true
        )
        return
    }

    Spacer(modifier = Modifier.height(topGap))

    ThreeLineAnimation(
        firstSentence = firstSentence,
        secondSentence = secondSentence,
        thirdSentence = thirdSentence,
        gap = gap,
        mainSentenceStyle = ByeBooTheme.typography.body3,
        subSentenceStyle = ByeBooTheme.typography.cap2,
        colorStrong = ByeBooTheme.colors.secondary50,
        colorWeak = ByeBooTheme.colors.secondary50.copy(alpha = 0.5f),
        onFinished = {
            if (index + 3 < paragraphs.size) {
                onAdvance(index + 1)
            }
        },
        modifier = modifier
    )
}

@Composable
private fun ThreeLineAnimation(
    firstSentence: String,
    secondSentence: String,
    thirdSentence: String,
    gap: Dp,
    mainSentenceStyle: TextStyle,
    subSentenceStyle: TextStyle,
    colorStrong: Color,
    colorWeak: Color,
    onFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val measurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val gapPx = with(density) { gap.roundToPx().toFloat() }

    val screenW = LocalConfiguration.current.screenWidthDp.dp
    val availW = with(density) { (screenW - 24.dp * 2).roundToPx() }

    fun measureH(text: String, style: TextStyle) =
        measurer.measure(
            text = AnnotatedString(text),
            style = style,
            constraints = Constraints(maxWidth = availW)
        ).size.height.toFloat()

    val hFirst = measureH(firstSentence, mainSentenceStyle)
    val hSecond = measureH(secondSentence, subSentenceStyle)

    val secondTop = hFirst + gapPx
    val thirdTop = hFirst + gapPx + hSecond + gapPx

    val firstAlpha = remember { Animatable(1f) }
    val firstTY = remember { Animatable(0f) }

    val colorToVector = TwoWayConverter(
        { c: Color -> AnimationVector4D(c.red, c.green, c.blue, c.alpha) },
        { v: AnimationVector4D -> Color(v.v1, v.v2, v.v3, v.v4) }
    )

    val secondColor = remember { Animatable(colorWeak, colorToVector) }
    val thirdColor = remember { Animatable(colorWeak, colorToVector) }

    val secondAlpha = remember { Animatable(1f) }
    val secondTY = remember { Animatable(0f) }
    val secondScale = remember { Animatable(1f) }

    val thirdAlpha = remember { Animatable(0f) }
    val thirdTY = remember { Animatable(0f) }
    val thirdScale = remember { Animatable(1f) }

    LaunchedEffect(Unit) {
        launch { secondColor.animateTo(colorStrong, tween(durationMillis = 2100)) }

        delay(1000)

        coroutineScope {
            launch {
                firstTY.animateTo(
                    -20f,
                    tween(durationMillis = 1000, easing = FastOutSlowInEasing)
                )
            }
            launch {
                firstAlpha.animateTo(
                    0f,
                    tween(durationMillis = 1000, easing = FastOutSlowInEasing)
                )
            }

            launch {
                secondTY.animateTo(
                    -secondTop,
                    tween(durationMillis = 1000, easing = FastOutSlowInEasing)
                )
            }
            launch {
                secondScale.animateTo(
                    1.2f,
                    tween(durationMillis = 1000, easing = FastOutSlowInEasing)
                )
            }

            launch {
                thirdAlpha.animateTo(
                    1f,
                    tween(durationMillis = 1000, easing = FastOutSlowInEasing)
                )
            }
            launch {
                thirdTY.animateTo(
                    targetValue = -(hFirst + gapPx),
                    animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
                )
            }
        }

        delay(1000)

        coroutineScope {
            launch {
                secondTY.animateTo(
                    -secondTop - 20f,
                    tween(durationMillis = 1000, easing = FastOutSlowInEasing)
                )
            }
            launch {
                secondAlpha.animateTo(
                    0f,
                    tween(durationMillis = 1000, easing = FastOutSlowInEasing)
                )
            }

            launch {
                thirdTY.animateTo(
                    targetValue = -thirdTop,
                    animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
                )
            }
            launch {
                thirdScale.animateTo(
                    1.2f,
                    tween(durationMillis = 1000, easing = FastOutSlowInEasing)
                )
            }
            launch {
                thirdColor.animateTo(
                    targetValue = colorStrong,
                    animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing)
                )
            }
        }
        onFinished()
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = firstSentence,
            style = mainSentenceStyle,
            color = colorStrong,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    translationY = firstTY.value
                    alpha = firstAlpha.value
                    transformOrigin = TransformOrigin(0.5f, 0f)
                }
        )

        Spacer(modifier = Modifier.height(gap))

        Text(
            text = secondSentence,
            style = subSentenceStyle,
            color = secondColor.value,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    translationY = secondTY.value
                    scaleX = secondScale.value
                    scaleY = secondScale.value
                    alpha = secondAlpha.value
                    transformOrigin = TransformOrigin(0.5f, 0f)
                }
        )

        Spacer(modifier = Modifier.height(gap))

        Text(
            text = thirdSentence,
            style = subSentenceStyle,
            color = thirdColor.value,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    translationY = thirdTY.value + (secondScale.value - 1f) * hSecond
                    scaleX = thirdScale.value
                    scaleY = thirdScale.value
                    alpha = thirdAlpha.value
                    transformOrigin = TransformOrigin(0.5f, 0f)
                }
        )
    }
}
