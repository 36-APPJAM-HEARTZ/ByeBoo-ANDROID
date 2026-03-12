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
import com.byeboo.app.core.model.quest.JourneyType
import com.byeboo.app.core.util.screenHeightDp
import com.byeboo.app.core.util.screenWidthDp
import com.byeboo.app.presentation.auth.userinfo.component.UserInfoQuestCard
import kotlinx.collections.immutable.persistentListOf

@Composable
fun UserInfoQuestScreen(
    selectedQuest: JourneyType?,
    onQuestSelect: (JourneyType) -> Unit,
) {
    val quests =
        persistentListOf(
            JourneyType.RECORDING,
            JourneyType.REUNION,
        )

    Column {
        DescriptionText(
            title = "퀘스트 방식",
            guideText = "을 골라주세요",
            contentText = "나에게 맞는 방식으로 퀘스트를 받아볼 수 있어요.",
            bottom = screenHeightDp(20.dp),
        )
        Row(
            modifier = Modifier.padding(vertical = screenHeightDp(8.dp)),
            horizontalArrangement = Arrangement.spacedBy(screenWidthDp(12.dp)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            quests.forEach { quest ->
                val onCardClick =
                    remember(quest) {
                        { onQuestSelect(quest) }
                    }

                UserInfoQuestCard(
                    title = quest.journeyName,
                    content =
                        when (quest) {
                            JourneyType.RECORDING -> "질문과 미션을 통해\n나만의 삶을\n회복해 나가요"
                            JourneyType.REUNION -> "X와의 재회를 위해\n나를 먼저 돌아보고\n상대를 이해해요"
                            else -> ""
                        },
                    imageRes =
                        when (quest) {
                            JourneyType.RECORDING -> R.drawable.img_bori_overcome
                            JourneyType.REUNION -> R.drawable.img_bori_reunion
                            else -> R.drawable.img_bori_reunion
                        },
                    isSelected = selectedQuest == quest,
                    onCardClick = onCardClick,
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}
