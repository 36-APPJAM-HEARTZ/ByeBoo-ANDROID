package com.byeboo.app.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.RenderMode
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.presentation.home.component.HomeProgressCard
import com.byeboo.app.presentation.home.component.HomeQuestCard
import com.byeboo.app.presentation.home.component.HomeTextCard


@Composable
fun HomeRoute(
    navigateToQuest: () -> Unit,
    navigateToQuestStart: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                HomeSideEffect.NavigateToQuest -> navigateToQuest()
                HomeSideEffect.NavigateToQuestStart -> navigateToQuestStart()
            }
        }
    }

    HomeScreen(
        uiState = uiState,
        onClickQuest = viewModel::onClickQuest,
        onClickQuestStart = viewModel::onClickQuestStart,
        modifier = modifier
    )
}

@Composable
private fun HomeScreen(
    uiState: HomeUiState,
    onClickQuest: () -> Unit,
    onClickQuestStart: () -> Unit,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.bori_home))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ByeBooTheme.colors.black)
    ) {
        LottieAnimation(
            composition = composition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            renderMode = RenderMode.AUTOMATIC,
            enableMergePaths = true
        )
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = screenHeightDp(24.dp))
                .padding(top = screenHeightDp(67.dp))
        ) {
            if (uiState.isQuestStarted == true) {
                HomeQuestCard(
                    title = "오늘의 퀘스트 하러가기",
                    subtitle = "퀘스트를 하고나면 한층 더 성장할 거에요.",
                    onClick = onClickQuest
                )
                Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))
                HomeProgressCard(
                    title = "${uiState.nickname}님의 ${uiState.journey} 여정",
                    currentStep = uiState.currentStep,
                    totalSteps = uiState.totalSteps
                )
            } else {
                HomeQuestCard(
                    title = "${uiState.journey} 여정 시작하기",
                    subtitle = "제가 옆에서 함께할게요!",
                    onClick = onClickQuestStart
                )
            }

            Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

            HomeTextCard(title = uiState.dialogue)
        }
    }
}
