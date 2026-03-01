package com.byeboo.app.presentation.quest.tip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.component.LoadingScreen
import com.byeboo.app.core.designsystem.component.tag.SmallTag
import com.byeboo.app.core.designsystem.event.LocalSnackBarTrigger
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.core.state.UiState
import com.byeboo.app.core.util.noRippleClickable
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.quest.component.text.QuestContent
import com.byeboo.app.presentation.quest.component.type.QuestContentType

@Composable
fun QuestTipRoute(
    navigateToQuest: () -> Unit,
    paddingValues: PaddingValues,
    viewModel: QuestTipViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val showSnackBar = LocalSnackBarTrigger.current

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is QuestTipSideEffect.NavigateToQuest -> navigateToQuest()
                is QuestTipSideEffect.ShowSnackBar -> showSnackBar(effect.snackBarType)
            }
        }
    }

    when (val state = uiState) {
        is UiState.Loading -> {
            LoadingScreen()
        }

        is UiState.Failure -> Unit

        is UiState.Success ->
            QuestTipScreen(
                uiState = state.data,
                onCloseClick = viewModel::onCloseClicked,
                paddingValues = paddingValues,
            )

        else -> Unit
    }
}

@Composable
private fun QuestTipScreen(
    uiState: QuestTipState,
    onCloseClick: () -> Unit,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(ByeBooTheme.colors.background)
                .padding(
                    top = paddingValues.calculateTopPadding() + screenHeightDp(43.dp),
                    bottom = paddingValues.calculateBottomPadding(),
                ),
    ) {
        QuestTipHeader(onCloseClick = onCloseClick)

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding =
                PaddingValues(
                    start = screenWidthDp(24.dp),
                    top = screenHeightDp(10.dp),
                    end = screenWidthDp(24.dp),
                    bottom = screenHeightDp(24.dp),
                ),
        ) {
            item {
                QuestTipTitle(
                    stepNumber = uiState.stepNumber,
                    questNumber = uiState.questNumber,
                    question = uiState.question,
                )
            }

            item {
                QuestTipReason(
                    questNumber = uiState.questNumber,
                    tipAnswer = uiState.tipAnswer,
                )
            }

            item {
                HorizontalDivider(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = screenHeightDp(8.dp)),
                    thickness = 1.dp,
                    color = ByeBooTheme.colors.whiteAlpha5,
                )
            }

            item {
                QuestTipThinking(
                    questType = uiState.questType,
                    tipAnswer = uiState.tipAnswer,
                )
            }

            item {
                HorizontalDivider(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = screenHeightDp(8.dp)),
                    thickness = 1.dp,
                    color = ByeBooTheme.colors.whiteAlpha5,
                )
            }

            item {
                QuestTipChange(tipAnswer = uiState.tipAnswer)
            }
        }
    }
}

@Composable
private fun QuestTipHeader(
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier =
            modifier
                .fillMaxWidth()
                .padding(horizontal = screenWidthDp(24.dp))
                .padding(bottom = screenHeightDp(16.dp)),
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_cancel),
            contentDescription = "닫기",
            tint = ByeBooTheme.colors.white,
            modifier =
                Modifier
                    .size(24.dp)
                    .align(Alignment.CenterEnd)
                    .noRippleClickable(onCloseClick),
        )

        Text(
            text = "퀘스트 작성 TIP",
            style = ByeBooTheme.typography.sub1,
            color = ByeBooTheme.colors.white,
            modifier = Modifier.align(Alignment.Center),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun QuestTipTitle(
    stepNumber: Long,
    questNumber: Long,
    question: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SmallTag(
                tagText = "STEP $stepNumber",
                tagColor = ByeBooTheme.colors.gray500,
            )

            Spacer(modifier = Modifier.width(screenWidthDp(8.dp)))

            Text(
                text = "${questNumber}번째 퀘스트",
                style = ByeBooTheme.typography.body6,
                color = ByeBooTheme.colors.gray500,
            )
        }

        Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))

        Text(
            text = question,
            style = ByeBooTheme.typography.head1,
            color = ByeBooTheme.colors.gray100,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun QuestTipReason(
    questNumber: Long,
    tipAnswer: QuestTipAnswers,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(screenHeightDp(34.dp)))

        QuestContent(
            titleIcon = QuestContentType.QUEST_REASON,
            titleText = "${questNumber}번째 퀘스트로 드리는 이유",
            contentText = tipAnswer.reason,
        )

        Spacer(modifier = Modifier.height(screenHeightDp(14.dp)))
    }
}

@Composable
private fun QuestTipThinking(
    questType: QuestType,
    tipAnswer: QuestTipAnswers,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(screenHeightDp(24.dp)))

        if (questType.questStyle == "RECORDING") {
            QuestContent(
                titleIcon = QuestContentType.THINKING,
                titleText = "이런 걸 생각해 보며 작성해 주세요.",
                contentText = tipAnswer.suggestion,
            )
        } else {
            QuestContent(
                titleIcon = QuestContentType.BEHAVIOR,
                titleText = "이렇게 해보면 좋아요.",
                contentText = tipAnswer.suggestion,
            )
        }

        Spacer(modifier = Modifier.height(screenHeightDp(18.dp)))
    }
}

@Composable
private fun QuestTipChange(
    tipAnswer: QuestTipAnswers,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(screenHeightDp(24.dp)))

        QuestContent(
            titleIcon = QuestContentType.FEELING_CHANGE,
            titleText = "이 퀘스트가 끝나면 어떤 변화가 생길까요?",
            contentText = tipAnswer.change,
        )
    }
}
