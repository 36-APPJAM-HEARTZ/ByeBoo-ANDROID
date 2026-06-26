package com.byeboo.app.presentation.quest.review.common.personal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.byeboo.app.core.designsystem.component.topbar.BackTopbar
import com.byeboo.app.core.designsystem.event.LocalSnackBarTrigger
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.quest.component.card.MyAnswerItem
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MyAnswerRoute(
    navigateUp: () -> Unit,
    navigateToQuest: () -> Unit,
    navigateToQuestCommonAnswer: (Long) -> Unit,
    paddingValues: PaddingValues,
    viewModel: MyAnswerViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val showSnackBar = LocalSnackBarTrigger.current

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is MyAnswerSideEffect.NavigateUp -> navigateUp()
                is MyAnswerSideEffect.NavigateToQuest -> navigateToQuest()
                is MyAnswerSideEffect.NavigateToQuestCommonAnswer ->
                    navigateToQuestCommonAnswer(effect.answerId)
                is MyAnswerSideEffect.ShowSnackBar -> showSnackBar(effect.snackBarType)
            }
        }
    }

    MyAnswerScreen(
        uiState = uiState,
        paddingValues = paddingValues,
        onLoadAnswers = viewModel::loadMyAnswers,
        onBackClick = viewModel::onBackClicked,
        onMyAnswerContentClick = viewModel::onMyAnswerContentClicked,
        onHeartClick = viewModel::onHeartClicked,
    )
}

@Composable
fun MyAnswerScreen(
    uiState: MyAnswerState,
    onLoadAnswers: () -> Unit,
    onBackClick: () -> Unit,
    onMyAnswerContentClick: (Long) -> Unit,
    paddingValues: PaddingValues,
    onHeartClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    val hasMyAnswers =
        remember {
            derivedStateOf {
                val totalItems = listState.layoutInfo.totalItemsCount
                val lastVisibleItemIndex =
                    listState.layoutInfo.visibleItemsInfo
                        .lastOrNull()
                        ?.index ?: 0

                totalItems > 0 && lastVisibleItemIndex >= totalItems - 2
            }
        }

    LaunchedEffect(hasMyAnswers.value) {
        if (hasMyAnswers.value) {
            onLoadAnswers()
        }
    }

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
        LazyColumn(
            state = listState,
            contentPadding =
                PaddingValues(
                    start = screenWidthDp(24.dp),
                    end = screenWidthDp(24.dp),
                    bottom = screenHeightDp(25.dp),
                ),
            modifier = Modifier.fillMaxWidth(),
        ) {
            item {
                BackTopbar(onBackClick = onBackClick)
            }

            item {
                Text(
                    text =
                        buildAnnotatedString {
                            append("${uiState.userName}님의")
                            append("\n")
                            append("공통퀘스트 답변이에요")
                        },
                    color = ByeBooTheme.colors.gray50,
                    style = ByeBooTheme.typography.head2,
                    modifier = Modifier.padding(vertical = screenHeightDp(10.dp)),
                )
            }

            when {
                uiState.isLoading && uiState.answers.isEmpty() -> {
                    item {
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .fillParentMaxHeight(0.7f),
                            contentAlignment = Alignment.Center,
                        ) {
                            CircularProgressIndicator(
                                color = ByeBooTheme.colors.primary500,
                            )
                        }
                    }
                }

                uiState.answers.isEmpty() -> {
                    item {
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .fillParentMaxHeight(0.7f),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "아직 작성한 답변이 없어요!",
                                style = ByeBooTheme.typography.body6,
                                color = ByeBooTheme.colors.gray400,
                            )
                        }
                    }
                }

                else -> {
                    items(
                        items = uiState.answers,
                        key = { it.answerId },
                    ) { answer ->
                        Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))

                        MyAnswerItem(
                            answer = answer,
                            onHeartClick = onHeartClick,
                            onCommentClick = { onMyAnswerContentClick(answer.answerId) },
                            onMyAnswerContentClick = { onMyAnswerContentClick(answer.answerId) },
                        )
                    }
                }
            }
        }
    }
}
