package com.byeboo.app.presentation.quest.component.text

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.byeboo.app.core.designsystem.component.tag.MiddleTag
import com.byeboo.app.core.designsystem.type.MiddleTagType
import com.byeboo.app.core.designsystem.ui.theme.ByeBooTheme
import com.byeboo.app.core.model.quest.QuestType
import com.byeboo.app.core.util.screenHeightDp

@Composable
fun QuestJourneyTitle(
    dayCount: Int,
    nickname: String,
    questTitle: QuestType,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = screenHeightDp(19.dp)),
    ) {
        MiddleTag(
            middleTagType = MiddleTagType.QUEST_START_DAY,
            text = dayCount.toString(),
            textStyle = ByeBooTheme.typography.cap2,
        )

        Spacer(modifier = Modifier.height(screenHeightDp(8.dp)))

        Text(
            buildAnnotatedString {
                append("${nickname}님, 지금\n")

                withStyle(style = SpanStyle(color = ByeBooTheme.colors.primary300)) {
                    append(questTitle.questStyle)
                }

                append(" 여정을 진행 중이에요.")
            },
            color = ByeBooTheme.colors.gray50,
            style = ByeBooTheme.typography.head1,
        )

        Spacer(modifier = Modifier.height(screenHeightDp(8.dp)))

        Text(
            text = "오늘도 한 걸음 나아가 볼까요?",
            color = ByeBooTheme.colors.gray400,
            style = ByeBooTheme.typography.body5,
        )
    }
}
