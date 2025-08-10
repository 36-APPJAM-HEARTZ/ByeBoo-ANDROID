package com.byeboo.app.presentation.offboarding.offboardingcompleteguide

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.component.button.ByeBooButton
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.noRippleClickable
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.presentation.offboarding.component.OffboardingNewJourneyButton
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
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
            modifier = Modifier.fillMaxSize()
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

                Text(
                    text = "🎉${uiState.journeyName}을 완료했어요!🎉",
                    color = ByeBooTheme.colors.secondary300,
                    style = ByeBooTheme.typography.sub2
                )

                SubTextSequence(
                    paragraphs = persistentListOf(
                        "무려 30개의 퀘스트를 완료했어요.\n끝까지 포기하지 않고 극복하기 위해 노력한\n${uiState.nickname}님이 너무 대단해요.",
                        "지금의 ${uiState.nickname}님은, 처음보다 성장했을 거예요.",
                        "만약 아직 정리되지 못한 감정이 남아있다면,\n또 다른 새로운 여정을 시작해볼까요?"
                    )
                )

                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 34.dp)
                ) {
                    // TODO: 로티 넣을 예정
                    Image(
                        painter = painterResource(id = R.drawable.bori_cake),
                        contentDescription = "",
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.FillWidth
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
                    buttonStyle = ByeBooTheme.typography.body1,
                    buttonTextColor = ByeBooTheme.colors.primary400,
                    buttonBackgroundColor = ByeBooTheme.colors.primary50,
                )

                Spacer(modifier = Modifier.height(screenHeightDp(8.dp)))
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun SubTextSequence(
    paragraphs: ImmutableList<String>,
    firstHoldMs: Long = 1000L,
    holdMs: Long = 2000L
) {
    var index by remember { mutableIntStateOf(0) }

    LaunchedEffect(index) {
        val d = if (index == 0) firstHoldMs else holdMs
        if (index < paragraphs.lastIndex) {
            delay(d)
            index++
        }
    }
    Spacer(modifier = Modifier.height(screenHeightDp(32.dp)))

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedContent(
            targetState = index,
            contentAlignment = Alignment.Center,


            transitionSpec = {
                // 등장: 살짝 작게 시작 → 제자리로 커지며 나타남
                (fadeIn(tween(240, delayMillis = 90)) +
                        scaleIn(initialScale = 0.94f, animationSpec = tween(240, delayMillis = 90)))
                    .togetherWith(
                        // 퇴장: 페이드아웃 + 중앙 기준으로 축소
                        fadeOut(tween(180)) +
                                scaleOut(targetScale = 0.82f, animationSpec = tween(180))
                    )
            }
//            transitionSpec = {
//                (fadeIn(animationSpec = tween(220, delayMillis = 90)) +
//                        scaleIn(initialScale = 0.92f, animationSpec = tween(220, delayMillis = 90)))
//                    .togetherWith(
//                        fadeOut(animationSpec = tween(220))
//                                //+ scaleOut(targetScale = 0.7f, animationSpec = tween(220))
//                    )
//            },
        ) { i ->
            Text(
                text = paragraphs[i],
                color = ByeBooTheme.colors.white,
                textAlign = TextAlign.Center,
                style = ByeBooTheme.typography.body3
            )
        }

        val next = paragraphs.getOrNull(index + 1)

        if (next != null) {
            Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

            Text(
                text = next,
                color = ByeBooTheme.colors.secondary50,
                textAlign = TextAlign.Center,
                style = ByeBooTheme.typography.cap2
            )
        }
    }
}
