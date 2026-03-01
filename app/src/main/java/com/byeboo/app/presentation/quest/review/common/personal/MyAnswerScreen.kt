package com.byeboo.app.presentation.quest.review.common.personal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.noRippleClickable
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.presentation.quest.component.card.MyAnswerItem
import kotlinx.coroutines.flow.collectLatest

@Composable
fun MyAnswerRoute(
    navigateToQuestMyAnswerDetail: (Long) -> Unit,
    paddingValues: PaddingValues,
    viewModel: MyAnswerViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is MyAnswerSideEffect.NavigateToQuestMyAnswerDetail ->
                    navigateToQuestMyAnswerDetail(effect.answerId)
            }
        }
    }

    MyAnswerScreen(
        uiState = uiState,
        paddingValues = paddingValues,
        onMyAnswerContentClick = viewModel::onMyAnswerContentClicked,
    )
}

@Composable
fun MyAnswerScreen(
    uiState: MyAnswerState,
    onMyAnswerContentClick: (Long) -> Unit,
    paddingValues: PaddingValues,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(ByeBooTheme.colors.background)
                .padding(horizontal = 24.dp)
                .padding(
                    top = paddingValues.calculateTopPadding() + screenHeightDp(43.dp),
                    bottom = paddingValues.calculateBottomPadding(),
                ),
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(screenHeightDp(20.dp)),
            contentPadding =
                PaddingValues(bottom = screenHeightDp(25.dp)),
            modifier = Modifier.fillMaxWidth(),
        ) {
            item {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_left),
                    contentDescription = null,
                    tint = ByeBooTheme.colors.gray50,
                    modifier =
                        Modifier.noRippleClickable(
                            // Todo: 뒤로가기
                        ),
                )

                Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

                Text(
                    text =
                        buildAnnotatedString {
                            append("하츠핑하츠님의")
                            append("\n")
                            append("공통퀘스트 답변이에요")
                        },
                    color = ByeBooTheme.colors.gray50,
                    style = ByeBooTheme.typography.head2,
                    modifier = Modifier.padding(vertical = screenHeightDp(10.dp)),
                )
            }

            if (uiState.answers.isEmpty()) {
                item {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(top = screenHeightDp(184.dp)),
                    ) {
                        Text(
                            text = "아직 작성한 답변이 없어요!",
                            style = ByeBooTheme.typography.body6,
                            color = ByeBooTheme.colors.gray400,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            } else {
                items(
                    items = uiState.answers,
                    key = { it.answerId },
                ) { answer ->
                    MyAnswerItem(
                        answer = answer,
                        onMyAnswerContentClick = { onMyAnswerContentClick(answer.answerId) },
                    )
                }
            }
        }
    }
}
