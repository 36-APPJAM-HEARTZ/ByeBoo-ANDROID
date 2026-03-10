package com.byeboo.app.presentation.quest.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.byeboo.app.core.designsystem.component.button.ByeBooButton
import com.byeboo.app.core.designsystem.component.topbar.BackTopbar
import com.byeboo.app.core.designsystem.event.LocalSnackBarTrigger
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.quest.component.modal.GuideContent

@Composable
fun QuestStartRoute(
    navigateToQuest: () -> Unit,
    navigateToHome: () -> Unit,
    paddingValues: PaddingValues,
    viewModel: QuestStartViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val showSnackBar = LocalSnackBarTrigger.current

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is QuestStartSideEffect.NavigateToQuest -> navigateToQuest()
                is QuestStartSideEffect.NavigateToHome -> navigateToHome()
                is QuestStartSideEffect.ShowSnackBar -> showSnackBar(effect.snackBarType)
            }
        }
    }

    QuestStartScreen(
        uiState = uiState,
        onBackClick = viewModel::onBackClicked,
        onStartClick = viewModel::onStartClicked,
        paddingValues = paddingValues,
    )
}

@Composable
private fun QuestStartScreen(
    uiState: QuestStartState,
    onBackClick: () -> Unit,
    onStartClick: () -> Unit,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier =
            modifier
                .fillMaxSize()
                .background(ByeBooTheme.colors.background)
                .padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                ),
        contentPadding = PaddingValues(top = screenHeightDp(43.dp), bottom = screenHeightDp(10.dp)),
    ) {
        item {
            BackTopbar(
                onBackClick = onBackClick,
                modifier = Modifier.padding(horizontal = screenWidthDp(24.dp)),
            )
        }

        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(screenHeightDp(56.dp)),
            ) {
                GuideContent(
                    userName = uiState.nickname,
                    guideText = "님의 상황에 꼭 맞춘\n${uiState.journeyName} 여정의 퀘스트 30개를 드릴게요.\n\n제가 드리는 퀘스트와 함께\n이별을 극복해 나가요!",
                )
            }

            ByeBooButton(
                onClick = onStartClick,
                buttonText = "시작하기",
                buttonStyle = ByeBooTheme.typography.body2,
                buttonTextColor = ByeBooTheme.colors.white,
                buttonBackgroundColor = ByeBooTheme.colors.primary300,
                modifier = Modifier.padding(horizontal = screenWidthDp(24.dp)),
            )
        }
    }
}
