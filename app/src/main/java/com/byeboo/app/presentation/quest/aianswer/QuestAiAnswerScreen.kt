package com.byeboo.app.presentation.quest.aianswer

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.component.topbar.CloseTopbar
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.state.UiState
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.quest.aianswer.type.QuestAiAnswerStatusType
import kotlinx.coroutines.flow.collectLatest

@Composable
fun QuestAiAnswerRoute(
    paddingValues: PaddingValues,
    navigateToQuest: () -> Unit,
    viewModel: QuestAiAnswerViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is QuestAiAnswerSideEffect.NavigateToQuest -> navigateToQuest()
            }
        }
    }

    BackHandler {
        navigateToQuest()
    }

    when (val state = uiState) {
        is UiState.Loading ->
            QuestAiAnswerStatusScreen(
                paddingValues = paddingValues,
                onCloseClick = viewModel::onCloseClicked,
                statusType = QuestAiAnswerStatusType.LOADING,
            )

        is UiState.Failure ->
            QuestAiAnswerStatusScreen(
                paddingValues = paddingValues,
                onCloseClick = viewModel::onCloseClicked,
                statusType = QuestAiAnswerStatusType.FAIL,
            )

        is UiState.Success ->
            QuestAiAnswerScreen(
                uiState = state.data,
                paddingValues = paddingValues,
                onCloseClick = viewModel::onCloseClicked,
            )

        else -> Unit
    }
}

@Composable
private fun QuestAiAnswerScreen(
    uiState: QuestAiAnswerState,
    paddingValues: PaddingValues,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(color = ByeBooTheme.colors.background)
                .padding(horizontal = screenWidthDp(24.dp))
                .padding(
                    top = paddingValues.calculateTopPadding() + screenHeightDp(43.dp),
                    bottom = paddingValues.calculateBottomPadding(),
                ),
        verticalArrangement = Arrangement.spacedBy(screenHeightDp(35.dp)),
    ) {
        CloseTopbar(
            onCloseClick = onCloseClick,
        )

        QuestAiAnswer(
            questAiAnswer = uiState.questAiAnswer,
        )
    }
}

@Composable
private fun QuestAiAnswer(
    questAiAnswer: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .aspectRatio(312 / 547f),
    ) {
        Image(
            painter = painterResource(id = R.drawable.img_bori_letter),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )

        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = screenWidthDp(24.dp))
                    .padding(top = screenHeightDp(184.dp), bottom = screenHeightDp(22.dp)),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = questAiAnswer,
                style = ByeBooTheme.typography.bori,
                color = ByeBooTheme.colors.primary50,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(264 / 270f)
                        .verticalScroll(state = rememberScrollState()),
            )

            Text(
                text = "보리의 답장",
                style = ByeBooTheme.typography.bori,
                color = ByeBooTheme.colors.primary50,
                textAlign = TextAlign.End,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}
