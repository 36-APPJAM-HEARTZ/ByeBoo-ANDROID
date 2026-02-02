package com.byeboo.app.presentation.auth.userinfo.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.byeboo.app.R
import com.byeboo.app.core.designsystem.component.text.DescriptionText
import com.byeboo.app.domain.model.auth.QuestStyle
import com.byeboo.app.presentation.auth.userinfo.component.UserInfoQuestCard
import kotlinx.collections.immutable.persistentListOf

@Composable
fun UserInfoQuestScreen(
    selectedQuest: QuestStyle?,
    onQuestSelect: (QuestStyle) -> Unit
) {
    val quests =
        persistentListOf(
            QuestStyle.RECORDING,
            QuestStyle.ACTIVE
        )

    Column {
        DescriptionText(
            title = "퀘스트 방식",
            guideText = "을 골라주세요",
            contentText = "나에게 맞는 방식으로 퀘스트를 받아볼 수 있어요.",
            bottom = 20.dp
        )
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            quests.forEach { quest ->
                val onCardClick =
                    remember(quest) {
                        { onQuestSelect(quest) }
                    }

                UserInfoQuestCard(
                    title = quest.displayText,
                    content =
                    when (quest) {
                        QuestStyle.RECORDING -> "질문을 통해\n상황과 감정을\n정리해요"
                        QuestStyle.ACTIVE -> "작은 미션을 통해\n몸과 마음을\n가볍게 해요"
                    },
                    imageRes =
                    when (quest) {
                        QuestStyle.RECORDING -> R.drawable.ic_book
                        QuestStyle.ACTIVE -> R.drawable.ic_shoes
                    },
                    isSelected = selectedQuest == quest,
                    onCardClick = onCardClick,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
