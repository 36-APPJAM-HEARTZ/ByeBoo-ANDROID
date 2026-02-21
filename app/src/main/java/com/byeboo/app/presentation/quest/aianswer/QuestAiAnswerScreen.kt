package com.byeboo.app.presentation.quest.aianswer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.state.UiState
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.quest.aianswer.type.QuestAiAnswerStatusType

@Composable
fun QuestAiAnswerRoute(
    paddingValues: PaddingValues,
    viewModel: QuestAiAnswerViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

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
        Row(
            modifier =
                Modifier
                    .fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_cancel),
                contentDescription = null,
                tint = ByeBooTheme.colors.white,
                modifier = Modifier.clickable(onClick = onCloseClick),
            )
        }

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
                    .padding(horizontal = 24.dp)
                    .padding(top = 184.dp, bottom = 22.dp),
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
