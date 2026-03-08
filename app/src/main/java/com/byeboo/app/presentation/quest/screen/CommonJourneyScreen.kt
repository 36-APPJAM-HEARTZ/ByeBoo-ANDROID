package com.byeboo.app.presentation.quest.screen

import androidx.compose.foundation.LocalOverscrollFactory
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.byeboo.app.core.designsystem.component.button.ByeBooButton
import com.byeboo.app.core.designsystem.component.tag.MiddleTag
import com.byeboo.app.core.designsystem.component.text.DescriptionText
import com.byeboo.app.core.designsystem.type.MiddleTagType
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.TimeUtil
import com.byeboo.app.core.util.noRippleClickable
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.quest.CommonJourneyState
import com.byeboo.app.presentation.quest.component.card.CommonAnswerItem
import com.byeboo.app.presentation.quest.component.tab.QuestDateSelector
import java.time.LocalDate

@Composable
fun CommonJourneyScreen(
    state: CommonJourneyState,
    isLoading: Boolean,
    onMyAnswersClick: () -> Unit,
    onDateChange: (LocalDate) -> Unit,
    onAnswerClick: (Long) -> Unit,
    onCommonQuestClick: (Long) -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()

    val shouldLoadMore =
        remember {
            derivedStateOf {
                val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                lastVisibleItem != null && lastVisibleItem.index >= listState.layoutInfo.totalItemsCount - 5
            }
        }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value && state.hasNext && !isLoading) {
            onLoadMore()
        }
    }

    LaunchedEffect(state.selectedDate) {
        listState.scrollToItem(0)
    }

    CompositionLocalProvider(
        LocalOverscrollFactory provides null,
    ) {
        LazyColumn(
            state = listState,
            contentPadding = PaddingValues(bottom = screenHeightDp(24.dp)),
            modifier = modifier.fillMaxSize(),
        ) {
            item {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = screenWidthDp(24.dp)),
                ) {
                    DescriptionText(
                        title = "함께 이별을 극복하는 공간이에요",
                        guideText = "",
                        contentText = "공통 퀘스트를 통해 나의 이야기를 솔직히 털어놓고,\n타인의 답변도 확인해 보세요",
                        bottom = screenHeightDp(16.dp),
                    )
                    MiddleTag(
                        middleTagType = MiddleTagType.MY_ANSWERS,
                        textStyle = ByeBooTheme.typography.cap1,
                        modifier = Modifier.noRippleClickable(onClick = onMyAnswersClick),
                    )
                    Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))
                    HorizontalDivider(
                        thickness = 1.dp,
                        color = ByeBooTheme.colors.gray800,
                        modifier = Modifier.padding(top = screenHeightDp(8.dp)),
                    )
                }
            }

            stickyHeader {
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .background(ByeBooTheme.colors.background)
                            .padding(horizontal = screenWidthDp(24.dp)),
                ) {
                    QuestDateSelector(
                        selectedDate = state.selectedDate,
                        onDateChange = onDateChange,
                        modifier = Modifier.padding(vertical = screenHeightDp(12.dp)),
                    )
                }
            }

            if (isLoading && state.answers.isEmpty()) {
                item {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .fillParentMaxHeight(0.5f),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(color = ByeBooTheme.colors.primary500)
                    }
                }
            } else {
                item {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = screenWidthDp(24.dp)),
                    ) {
                        Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))

                        val isToday = state.selectedDate.isEqual(TimeUtil.getNowKst())

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.Top,
                        ) {
                            Text(
                                text = "Q. ",
                                style = ByeBooTheme.typography.body1,
                                color = ByeBooTheme.colors.primary200,
                            )
                            Text(
                                text = state.question,
                                style = ByeBooTheme.typography.sub3,
                                color = ByeBooTheme.colors.gray50,
                                modifier = Modifier.weight(1f),
                            )
                        }

                        if (isToday && !state.isMyAnswerDone && state.question.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))
                            Text(
                                text = "23:59까지 답변 가능해요",
                                style = ByeBooTheme.typography.cap2,
                                color = ByeBooTheme.colors.gray400,
                            )
                            Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))
                            ByeBooButton(
                                onClick = { onCommonQuestClick(state.questId) },
                                buttonText = "답변 작성하기",
                                buttonStyle = ByeBooTheme.typography.body2,
                                buttonTextColor = ByeBooTheme.colors.primary500,
                                buttonBackgroundColor = ByeBooTheme.colors.primary100,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }

                        Spacer(modifier = Modifier.height(screenHeightDp(24.dp)))
                        Text(
                            text = "${state.answerCount}개의 답변",
                            style = ByeBooTheme.typography.cap2,
                            color = ByeBooTheme.colors.gray400,
                        )
                        Spacer(modifier = Modifier.height(screenHeightDp(24.dp)))
                    }
                }

                if (state.answers.isEmpty()) {
                    item {
                        Box(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .fillParentMaxHeight(0.5f),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "아직 작성된 답변이 없어요!",
                                style = ByeBooTheme.typography.body6,
                                color = ByeBooTheme.colors.gray400,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                } else {
                    items(
                        items = state.answers,
                        key = { it.answerId },
                    ) { answer ->
                        CommonAnswerItem(
                            answer = answer,
                            onClick = { onAnswerClick(answer.answerId) },
                            modifier =
                                Modifier
                                    .padding(horizontal = screenWidthDp(24.dp))
                                    .padding(bottom = screenHeightDp(24.dp)),
                        )
                    }

                    if (isLoading) {
                        item {
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = screenHeightDp(16.dp)),
                                contentAlignment = Alignment.Center,
                            ) {
                                CircularProgressIndicator(color = ByeBooTheme.colors.primary500)
                            }
                        }
                    }
                }
            }
        }
    }
}
