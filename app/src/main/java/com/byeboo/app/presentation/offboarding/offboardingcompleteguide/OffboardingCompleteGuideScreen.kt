package com.byeboo.app.presentation.offboarding.offboardingcompleteguide

import androidx.compose.animation.core.animateFloat
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
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
import kotlinx.coroutines.delay

@Composable
fun OffboardingCompleteGuideRoute(
    bottomPadding: Dp,
    modifier: Modifier = Modifier,
    viewModel: OffboardingCompleteGuideViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // TODO: 클릭 기능 추후 네비 세팅할 때 할 예정
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

                Spacer(modifier = Modifier.height(screenHeightDp(8.dp)))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(159.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🎉${uiState.journeyName}을 완료했어요!🎉",
                        color = ByeBooTheme.colors.secondary300,
                        style = ByeBooTheme.typography.sub2
                    )
                    SubTextSequence(
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

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(screenHeightDp(348.dp))
                        .padding(horizontal = 16.dp, vertical = 34.dp)
                ) {
                    LottieAnimation(
                        composition = composition,
                        progress = progress
                        )
                }

                Spacer(modifier = Modifier.weight(1f))

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
fun SubTextSequence(
    paragraphs: List<String>,
    index: Int,
    gap: Dp,
    topGap: Dp,
    onAdvance: (Int) -> Unit = {}
) {
    val current = paragraphs.getOrNull(index) ?: ""
    // 이번 사이클 프리뷰는 동결
    val frozenNext = remember(index) { paragraphs.getOrNull(index + 1) }

    var animating by remember(index) { mutableStateOf(false) }

    // 대기 후 페이드 시작
    LaunchedEffect(index) {
        val firstHold = 1000L
        val hold = 2000L
        delay(if (index == 0) firstHold else hold)
        if (frozenNext != null) animating = true
    }

    LiftUpTextsFadeOutOnly(
        current = current,
        next = frozenNext,
        topGap = topGap,                 // 32.dp 유지
        gap = gap,                       // 16.dp 유지
        durationMillis = 280,
        textStyleTop = ByeBooTheme.typography.body3,   // 위: body3
        textStyleBottom = ByeBooTheme.typography.cap2, // 아래: cap2
        colorTop = ByeBooTheme.colors.white,
        colorBottom = ByeBooTheme.colors.secondary50,
        animating = animating
    ) {
        // 중요: 페이드 끝난 뒤 교체
        animating = false
        if (frozenNext != null) onAdvance(index + 1)
    }
}

@Composable
private fun LiftUpTextsFadeOutOnly(
    current: String,            // 현재 1번째 문장
    next: String?,              // 2번째(프리뷰)
    topGap: Dp,                 // 32.dp
    gap: Dp,                    // 16.dp
    durationMillis: Int = 280,
    textStyleTop: TextStyle,    // body3 (메인)
    textStyleBottom: TextStyle, // cap2 (프리뷰)
    colorTop: Color,
    colorBottom: Color,
    animating: Boolean,
    onFadeOutFinished: (() -> Unit)? = null
) {
    // 위 여백 고정
    Spacer(Modifier.height(topGap))

    // animating: true일 때만 current 알파가 1→0으로 감소
    val t = androidx.compose.animation.core.updateTransition(
        targetState = animating,
        label = "fade_only"
    )
    val currentAlpha by t.animateFloat(
        transitionSpec = { tween(durationMillis) },
        label = "currentAlpha"
    ) { isAnimating -> if (isAnimating) 0f else 1f }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Top (현재 문장) — 위치 고정, 알파만 변화
        Text(
            text = current,
            color = colorTop.copy(alpha = currentAlpha),
            style = textStyleTop,               // body3
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        // 고정 간격
        Spacer(Modifier.height(gap))

        // Bottom (프리뷰) — 애니 동안에도 그대로 보여줌
        if (next != null) {
            Text(
                text = next,
                color = colorBottom,            // cap2 색/스타일 그대로
                style = textStyleBottom,        // cap2
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    // 페이드 아웃 종료 후 콜백
    LaunchedEffect(animating) {
        if (animating) {
            kotlinx.coroutines.delay(durationMillis.toLong())
            onFadeOutFinished?.invoke()
        }
    }
}