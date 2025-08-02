package com.byeboo.app.presentation.quest.tip

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.component.tag.SmallTag
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.core.util.noRippleClickable
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.quest.QuestViewModel
import com.byeboo.app.presentation.quest.component.text.QuestContent
import com.byeboo.app.presentation.quest.component.type.QuestContentType
import kotlinx.collections.immutable.ImmutableList

@Composable
fun QuestTipRoute(
    questId: Long,
    navigateToQuest: () -> Unit,
    questType: QuestType,
    bottomPadding: Dp,
    viewModel: QuestTipViewModel = hiltViewModel(),
    questViewModel: QuestViewModel = hiltViewModel()
){
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val questUiState by questViewModel.uiState.collectAsStateWithLifecycle()
    val questGroups = questUiState.questGroups

    LaunchedEffect(questId, questGroups) {
        val quest = questGroups
            .flatMap { it.quests }
            .find { it.questId == questId }

        quest?.let { viewModel.loadQuestTip(it) }
    }

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is QuestTipSideEffect.NavigateToQuest -> {
                    navigateToQuest()
                }
            }
        }
    }

    QuestTipScreen(
        onCloseClick = viewModel::onCloseClick,
        stepNumber = uiState.stepNumber,
        questNumber = uiState.questNumber,
        question = uiState.question,
        tipAnswers = uiState.tipAnswer,
        questType = questType,
        bottomPadding = bottomPadding
    )
}

@Composable
fun QuestTipScreen(
    onCloseClick: () -> Unit,
    stepNumber: Long,
    questNumber: Long,
    question: String,
    tipAnswers: ImmutableList<String>,
    questType: QuestType,
    bottomPadding: Dp,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = ByeBooTheme.colors.black)
            .padding(horizontal = screenWidthDp(24.dp))
            .padding(bottom = bottomPadding)
    ) {
        Spacer(modifier = Modifier.height(screenHeightDp(67.dp)))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = screenHeightDp(16.dp))
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_cancel),
                contentDescription = "닫기",
                tint = ByeBooTheme.colors.white,
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.CenterEnd)
                    .noRippleClickable(onCloseClick)
            )

            Text(
                text = "퀘스트 작성 TIP",
                style = ByeBooTheme.typography.sub1,
                color = ByeBooTheme.colors.white,
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center
            )
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            item {
                Spacer(modifier = Modifier.height(screenHeightDp(10.dp)))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SmallTag(tagText = "STEP ${stepNumber}")

                    Spacer(modifier = Modifier.width(screenWidthDp(8.dp)))

                    Text(
                        text = "${questNumber}번째 퀘스트",
                        style = ByeBooTheme.typography.body2,
                        color = ByeBooTheme.colors.gray500
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))

                Text(
                    text = question,
                    style = ByeBooTheme.typography.head1,
                    color = ByeBooTheme.colors.gray100,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            item {
                Spacer(modifier = Modifier.height(screenHeightDp(34.dp)))

                QuestContent(
                    titleIcon = QuestContentType.QUEST_REASON,
                    titleText = "${questNumber}번째 퀘스트로 드리는 이유",
                    contentText = tipAnswers[0]
                )
            }

            item {
                Spacer(modifier = Modifier.height(screenHeightDp(24.dp)))

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = screenHeightDp(8.dp)),
                    thickness = 1.dp,
                    color = ByeBooTheme.colors.whiteAlpha10
                )
            }

            item {
                Spacer(modifier = Modifier.height(screenHeightDp(24.dp)))

                if (questType.questName == "RECORDING") {
                    QuestContent(
                        titleIcon = QuestContentType.THINKING,
                        titleText = "이런 걸 생각해보며 작성해 주세요.",
                        contentText = tipAnswers[1]
                    )
                } else {
                    QuestContent(
                        titleIcon = QuestContentType.THINKING,
                        titleText = "이렇게 해보면 좋아요.",
                        contentText = tipAnswers[1]
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(screenHeightDp(24.dp)))

                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    thickness = 1.dp,
                    color = ByeBooTheme.colors.whiteAlpha10
                )
            }

            item {
                Spacer(modifier = Modifier.height(screenHeightDp(24.dp)))

                QuestContent(
                    titleIcon = QuestContentType.FEELING_CHANGE,
                    titleText = "이 퀘스트가 끝나면 어떤 변화가 생길까요?",
                    contentText = tipAnswers[2]
                )

                Spacer(modifier = Modifier.height(screenHeightDp(24.dp)))
            }
        }
    }
}
