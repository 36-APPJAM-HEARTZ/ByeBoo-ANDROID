package com.byeboo.app.presentation.quest.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.byeboo.app.core.designsystem.component.tag.MiddleTag
import com.byeboo.app.core.designsystem.component.text.DescriptionText
import com.byeboo.app.core.designsystem.type.MiddleTagType
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.quest.MyJourneyState
import com.byeboo.app.presentation.quest.component.chip.QuestBox
import com.byeboo.app.presentation.quest.component.text.QuestStepTitle

@Composable
fun MyJourneyScreen(
    state: MyJourneyState,
    userName: String,
    listState: LazyListState,
    onQuestClick: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = screenWidthDp(24.dp)),
        ) {
            MiddleTag(
                middleTagType = MiddleTagType.QUEST_START_DAY,
                text = state.progressPeriod.toString(),
                textStyle = ByeBooTheme.typography.cap1,
            )

            Spacer(modifier = Modifier.height(screenHeightDp(8.dp)))

            DescriptionText(
                nicknameText = "${userName}님, 지금",
                title = "${state.journeyTitle} 여정",
                guideText = "을 진행 중이에요",
                contentText = "오늘도 한 걸음 나아가 볼까요?",
                bottom = screenHeightDp(16.dp),
            )
        }

        LazyColumn(
            state = listState,
            verticalArrangement = Arrangement.spacedBy(screenHeightDp(20.dp)),
            contentPadding = PaddingValues(
                start = screenWidthDp(24.dp),
                end = screenWidthDp(24.dp),
                bottom = screenHeightDp(37.dp),
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            state.questGroups.forEachIndexed { stepIndex, group ->
                item("header_$stepIndex") {
                    Column {
                        HorizontalDivider(
                            thickness = 1.dp,
                            color = ByeBooTheme.colors.whiteAlpha10,
                            modifier = Modifier.padding(vertical = screenHeightDp(8.dp)),
                        )

                        Spacer(modifier = Modifier.padding(top = screenHeightDp(24.dp)))

                        QuestStepTitle(
                            stepNumber = (stepIndex + 1).toLong(),
                            stepTitle = group.stepTitle,
                        )

                        Spacer(modifier = Modifier.padding(top = screenHeightDp(8.dp)))
                    }
                }

                val questChunks = group.quests.chunked(3)
                questChunks.forEachIndexed { chunkIndex, questChunk ->
                    item("quest_row_${stepIndex}_$chunkIndex") {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(screenWidthDp(21.dp)),
                        ) {
                            questChunk.forEach { quest ->
                                QuestBox(
                                    modifier = Modifier.weight(1f),
                                    questId = quest.questId,
                                    questNumber = quest.questNumber,
                                    state = quest.state,
                                    onQuestClick = { onQuestClick(quest.questId) },
                                )
                            }

                            repeat(3 - questChunk.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}